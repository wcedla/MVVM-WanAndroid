package com.istrong.wcedla.wanandroid

import android.Manifest
import android.util.Log
import android.widget.Toast
import com.istrong.wcedla.base.architecture.BaseActivity
import com.istrong.wcedla.base.data.NetworkState
import com.istrong.wcedla.base.utils.PlatformUtil
import com.istrong.wcedla.wanandroid.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun beforeSuperCall() {
        PlatformUtil.setNavAndBarTranslate(
            this, darkBarText = true, translateNav = false
        )
    }

    override fun getRequestPermissionList(): List<String> {
        return mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
        )
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.articleData.observe(this) {
            Log.d("TAG", "获取完成，数量:${it.size}")
        }
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnTest.setOnClickListener {
            viewModel.loadData()
        }
    }

    override fun netWorkStateChange(state: NetworkState) {
        if (state == NetworkState.NETWORK_AVAILABLE) {
            Toast.makeText(this, "网络可用了!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "网络不可用！", Toast.LENGTH_SHORT).show()
        }
    }

}