package com.istrong.wcedla.wanandroid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.istrong.wcedla.base.architecture.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : BaseViewModel() {

    val articleData = MutableLiveData<List<String>>()

    fun loadData() {
        viewModelScope.launch {
            startLoadingData()
            withContext(Dispatchers.IO) {
                delay(3000)
                loadDataSuccess()
                val articleDataList = mutableListOf<String>()
                for (i in 0..20) {
                    articleDataList.add("我是第${i}本书")
                }
                articleData.postValue(articleDataList)
            }
        }

    }


}