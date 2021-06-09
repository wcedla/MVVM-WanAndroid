package com.istrong.wcedla.base.architecture

import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.istrong.wcedla.base.data.LoadingStatus

open class BaseViewModel : ViewModel() {

    /**
     * 所有页面都有加载状态，所以在基类中提供状态LiveData
     */
    protected val loadingStatus = MutableLiveData<LoadingStatus>()

    @MainThread
    fun observerLoadingStatus(owner: LifecycleOwner, observer: Observer<LoadingStatus>) {
        loadingStatus.observe(owner, observer)
    }

    fun startLoadingData() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            loadingStatus.postValue(LoadingStatus.LOADING)
        } else {
            loadingStatus.value = LoadingStatus.LOADING
        }
    }

    fun loadDataSuccess() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            loadingStatus.postValue(LoadingStatus.LOAD_SUCCESS)
        } else {
            loadingStatus.value = LoadingStatus.LOAD_SUCCESS
        }
    }

    fun loadDataFailure() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            loadingStatus.postValue(LoadingStatus.LOAD_FAILURE)
        } else {
            loadingStatus.value = LoadingStatus.LOAD_FAILURE
        }
    }

    fun changeLoadingStatus(status: LoadingStatus) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            loadingStatus.postValue(status)
        } else {
            loadingStatus.value = status
        }
    }

}