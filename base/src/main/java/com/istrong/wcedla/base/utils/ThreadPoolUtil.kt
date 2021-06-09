package com.istrong.wcedla.base.utils

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadPoolUtil {

    private const val CORE_POOL_SIZE = 8
    private const val MAXIMUM_POOL_SIZE = 20
    private const val KEEP_ALIVE_MILLISECONDS = 200L
    private val poolQueue = LinkedBlockingQueue<Runnable>(50)
    private val threadFactory = ThreadFactory { r -> Thread(r, "WanAndroid线程池") }
    private val rejectedExecutionHandler = ThreadPoolExecutor.DiscardOldestPolicy()
    val threadPoolExecutor =
        ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_MILLISECONDS,
            TimeUnit.MILLISECONDS,
            poolQueue,
            threadFactory,
            rejectedExecutionHandler
        )

}