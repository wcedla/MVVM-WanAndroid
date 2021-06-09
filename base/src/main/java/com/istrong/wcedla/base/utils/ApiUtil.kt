package com.istrong.wcedla.base.utils

import com.google.gson.JsonParser
import com.orhanobut.hawk.Hawk
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object ApiUtil {

    interface WanAndroidApi {


    }

    val wanAndroidApi: WanAndroidApi by lazy {
        val loggingInterceptor =
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Logger.d("网络请求日志:$message")
                }

            })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val dataIntercept = Interceptor {
            val originRequest = it.request()
            val newRequest = setRequestData(originRequest)
            val response = it.proceed(newRequest)
            saveResponseData(response)
            if (PlatformUtil.isManifestHaveDebugFlag) {
                val responseBody = response.body
                val responseByteSource = responseBody?.source()
                responseByteSource?.request(Long.MAX_VALUE)
                val responseByteBuffer = responseByteSource?.buffer()
                val responseSourceCharset =
                    responseBody?.contentType()?.charset(Charset.forName("utf-8"))
                var responseBodyString: String? = ""
                if (responseSourceCharset != null) {
                    responseBodyString =
                        responseByteBuffer?.clone()?.readString(responseSourceCharset)
                }
//                    Logger.d("接口请求返回数据:" + responseBodyString)
                val jsonElement = JsonParser.parseString(responseBodyString)
                Logger.d("接口请求返回数据--->${PlatformUtil.GSONInstance.toJson(jsonElement)}")
            }
            response
        }
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(dataIntercept)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(WanAndroidApi::class.java)
    }

    private fun setRequestData(request: Request): Request {
        val requestBuilder = request.newBuilder()
        val cookieMap = Hawk.get("cookie", mutableMapOf<String, String>())
        for (cookie in cookieMap) {
            requestBuilder.addHeader("Cookie", cookie.value)
        }
        return requestBuilder.build()
    }

    private fun saveResponseData(response: Response) {
        val cookieMap = Hawk.get("cookie", mutableMapOf<String, String>())
        val responseCookieList = response.headers.values("Set-Cookie")
        if (responseCookieList.isNotEmpty()) {
            for (cookie in responseCookieList) {
                cookieMap[cookie.substring(0, cookie.indexOfFirst { it == '=' })] = cookie
            }
            Hawk.put("cookie", cookieMap)
        }
    }

}