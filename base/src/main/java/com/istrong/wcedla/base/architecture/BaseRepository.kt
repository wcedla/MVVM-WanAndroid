package com.istrong.wcedla.base.architecture

open class BaseRepository(
    protected val localDataSource: BaseLocalDataSource?,
    protected val remoteDataSource: BaseRemoteDataSource?
) {


}