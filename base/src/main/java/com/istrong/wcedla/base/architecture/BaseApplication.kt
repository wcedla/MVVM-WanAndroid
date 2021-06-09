package com.istrong.wcedla.base.architecture

import android.app.Application
import android.webkit.WebView
import com.alibaba.android.arouter.launcher.ARouter
import com.istrong.wcedla.base.utils.NetWorkStateUtil
import com.istrong.wcedla.base.utils.PlatformUtil
import com.istrong.wcedla.base.utils.TrueTimeUtils
import com.orhanobut.hawk.Hawk
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

open class BaseApplication : Application() {

    private val formatStrategy by lazy {
        PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .tag("WanAndroidLog")
            .build()
    }

    private val csvFormatStrategy by lazy {
        CsvFormatStrategy.newBuilder()
            .tag("WanAndroidFileLog")
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        PlatformUtil.init(this)
        initThirdPartDependency()
        initInThread()
    }


    private fun initThirdPartDependency() {
        if (PlatformUtil.isManifestHaveDebugFlag) {
            ARouter.openDebug()
            ARouter.openLog()
            WebView.setWebContentsDebuggingEnabled(true)
        }
        //先把日志输出初始化
        Logger.init(this)
        //添加终端日志输出
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return PlatformUtil.isManifestHaveDebugFlag
            }
        })
        //添加平台本地日志文件输出
        Logger.addLogAdapter(object : AndroidLogAdapter(csvFormatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
        Hawk.init(this).build()
        ARouter.init(this)
        TrueTimeUtils.init(this)
        NetWorkStateUtil.initNetworkListener(this)

    }

    private fun initInThread() {
//        ThreadPoolUtil.threadPoolExecutor.execute {
//
//        }
    }


}