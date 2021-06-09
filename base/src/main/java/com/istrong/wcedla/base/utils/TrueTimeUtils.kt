package com.istrong.wcedla.base.utils

import android.app.Application
import com.instacart.library.truetime.TrueTime
import com.orhanobut.hawk.Hawk
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object TrueTimeUtils {

    val longDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    val shortDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA)
    val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)

    @Synchronized
    fun init(application: Application) {
        ThreadPoolUtil.threadPoolExecutor.execute {
            try {
                if (!TrueTime.isInitialized()) {
                    TrueTime.build()
                        .withNtpHost("ntp1.aliyun.com")
                        .withSharedPreferencesCache(application)
                        .initialize()
                    updateTimeSetStatus()
                    Logger.d("时间已经初始化完成")
                } else {
                    Logger.d("时间已经初始化过了")
                    updateTimeSetStatus()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Logger.d("时间初始化失败！")
            }
        }
    }

    private fun updateTimeSetStatus() {
        if (TrueTime.isInitialized()) {
            val trueDate = TrueTime.now().time
            val systemDate = Date().time
            if (abs(systemDate - trueDate) < 10 * 60 * 1000) {
                Hawk.delete("timeSet")
            }
        }
    }

    /**
     * 获取今天的起始时间
     * eg: 如果今天是2020-09-07那么返回1599408000000(2020-09-07 00:00:00)
     */
    fun getTodayStartTimeMillis(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        Logger.d("今天开始的时间(毫秒数):${calendar.timeInMillis}")
        return calendar.timeInMillis
    }

    /**
     * 获取今天的起始时间
     * eg: 如果今天是2020-09-07那么返回1599494399000(2020-09-07 23:59:59)
     */
    fun getToadyEndTimeMillis(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        Logger.d("今天结束的时间(毫秒数):${calendar.timeInMillis}")
        return calendar.timeInMillis
    }

    fun getNowTimeMillis(): Long {
        // TODO: 2020/10/12 在获取时间之前根据这个标志位提示用户是否有修改时间
        if (TrueTime.isInitialized()) {
            return TrueTime.now().time
        }
        return Date().time
    }

    fun getNowDate(): Date {
        if (TrueTime.isInitialized()) {
            return TrueTime.now()
        }
        return Date()
    }

    fun getDuration(startTime: Long, endTime: Long): String? {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA).apply {
            timeZone = TimeZone.getTimeZone("GMT+00:00")
        }
        return simpleDateFormat.format(endTime - startTime)
    }

    fun getDuration(durationMills: Long): String? {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA).apply {
            timeZone = TimeZone.getTimeZone("GMT+00:00")
        }
        return simpleDateFormat.format(durationMills)
    }

    fun getDurationMillis(durationString: String): Long? {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA).apply {
            timeZone = TimeZone.getTimeZone("GMT+00:00")
        }
        var date: Date? = null
        kotlin.runCatching {
            date = simpleDateFormat.parse(durationString)
        }
        return date?.time
    }

}