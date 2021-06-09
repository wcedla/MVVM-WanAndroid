package com.istrong.wcedla.base.data

enum class LoadingStatus(val status: Int) {
    /**
     * 初始状态
     */
    IDLE(0),

    /**
     * 加载中状态
     */
    LOADING(1),

    /**
     * 加载成功
     */
    LOAD_SUCCESS(2),

    /**
     * 加载失败
     */
    LOAD_FAILURE(3)
}