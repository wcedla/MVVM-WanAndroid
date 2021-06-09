package com.istrong.wcedla.base.architecture

import com.istrong.wcedla.base.database.WanAndroidDatabase
import com.istrong.wcedla.base.utils.PlatformUtil

/**
 * 本地数据源
 * 1.SharePreference持久化存储
 * 2.Sqlite数据库存储
 */
open class BaseLocalDataSource {

    val wanAndroidDatabase by lazy { WanAndroidDatabase.getDatabase(PlatformUtil.application) }

}