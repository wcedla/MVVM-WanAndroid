package com.istrong.wcedla.base.architecture

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.istrong.wcedla.base.data.LoadingStatus
import com.istrong.wcedla.base.data.NetworkState
import com.istrong.wcedla.base.extension.getGenericClassList
import com.istrong.wcedla.base.utils.CommonUtil
import com.istrong.wcedla.base.utils.NetWorkStateUtil
import com.istrong.wcedla.base.utils.PlatformUtil
import com.istrong.wcedla.base.widget.LoadingDialog
import com.istrong.wcedla.base.widget.PermissionDialog
import com.orhanobut.logger.Logger
import com.permissionx.guolindev.PermissionX

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel>() : AppCompatActivity() {

    private val genericList by lazy { getGenericClassList() }
    protected lateinit var viewBinding: VB

    protected lateinit var viewModel: VM

    protected lateinit var loadingDialog: LoadingDialog

    protected val loadingStatusObserver = Observer<LoadingStatus> { status ->
        when {
            status.equals(LoadingStatus.LOADING) -> {
                showLoadingDialog()
            }
            status.equals(LoadingStatus.LOAD_SUCCESS) -> {
                cancelLoadingDialog()
            }
            status.equals(LoadingStatus.LOAD_FAILURE) -> {
                cancelLoadingDialog()
            }
        }
    }

    protected val networkStateObserver = Observer<NetworkState> { state ->
        netWorkStateChange(state)
    }

    abstract fun netWorkStateChange(state: NetworkState)

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSuperCall()
        super.onCreate(savedInstanceState)
        viewBinding = initViewBinding()
        viewModel = initViewModel()
        setContentView(viewBinding.root)
        initView()
        requestPermission()
    }


    protected open fun beforeSuperCall() {
        PlatformUtil.setNavAndBarTranslate(
            this, darkBarText = false, translateNav = false
        )
    }

    /**
     * 初始化view相关逻辑，在权限请求操作前，不依赖于权限请求的结果
     */
    @CallSuper
    protected open fun initView() {
        viewModel.observerLoadingStatus(this, loadingStatusObserver)
        NetWorkStateUtil.networkState.observe(this, networkStateObserver)
        loadingDialog = LoadingDialog(this)
    }


    /**
     * 初始化数据，在权限请求通过之后执行，依赖于权限请求的结果
     */
    @CallSuper
    protected open fun initObserver() {

    }

    /**
     * 初始化监听器，在权限请求通过之后执行，依赖于权限请求的结果
     */
    @CallSuper
    protected open fun initListener() {
    }

    /**
     * 所有权限被允许了，执行时间在initData和initListener后
     */
    @CallSuper
    protected open fun afterPermissionAllGranted() {

    }


    /**
     * 获取需要请求的权限列表
     */
    protected abstract fun getRequestPermissionList(): List<String>

    /**
     * 请求权限
     */
    protected open fun requestPermission() {
        val requestPermissionList = getRequestPermissionList()
        if (requestPermissionList.isNotEmpty()) {
            PermissionX.init(this)
                .permissions(requestPermissionList)
                .onExplainRequestReason { scope, deniedList ->
                    val permissionRationaleDialog =
                        PermissionDialog(this, "权限申请", "为了更好的为您提供服务，请允许我们所申请的权限！", deniedList)
                    scope.showRequestReasonDialog(permissionRationaleDialog)
                }
                .onForwardToSettings { scope, deniedList ->
                    val permissionRationaleDialog =
                        PermissionDialog(
                            this,
                            "权限申请",
                            "为了更好的为您提供服务，请前往设置->应用->${
                                packageManager.getApplicationInfo(
                                    packageName,
                                    0
                                ).loadLabel(packageManager)
                            }开启${CommonUtil.formatDenyPermission(deniedList)}权限！",
                            deniedList
                        )
                    scope.showForwardToSettingsDialog(permissionRationaleDialog)
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Logger.d("请求的权限都已经被允许！")
                        permissionAllGranted()
                    } else {
                        Toast.makeText(this, "很抱歉！因为您拒绝了我们申请的权限，我们暂时无法为您提供服务！", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
        } else {
            permissionAllGranted()
            Logger.d("不需要申请权限或者权限列表获取为空！")
        }
    }

    /**
     * 所有权限允许后的操作
     */
    private fun permissionAllGranted() {
        initObserver()
        initListener()
        afterPermissionAllGranted()
    }

    protected fun showLoadingDialog() {
        if (::loadingDialog.isInitialized) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                runOnUiThread { loadingDialog.show() }
            } else {
                loadingDialog.show()
            }
        }
    }

    protected fun cancelLoadingDialog() {
        if (::loadingDialog.isInitialized) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                runOnUiThread { loadingDialog.cancel() }
            } else {
                loadingDialog.cancel()
            }
        }
    }

    private fun initViewModel(): VM {
        try {
            genericList.forEach {
                if (it.superclass != Any::class.java && it.superclass.isAssignableFrom(BaseViewModel::class.java)) {
                    val clazz = it as Class<VM>
                    return ViewModelProvider(
                        viewModelStore,
                        defaultViewModelProviderFactory
                    ).get(clazz)
                }
            }
        } catch (e: Exception) {
        }
        throw Throwable("获取ViewModel失败")
    }

    private fun initViewBinding(): VB {
        genericList.forEach {
            if (it.interfaces.contains(ViewBinding::class.java)) {
                val clazz = it as Class<VB>
                return clazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
                    .invoke(null, layoutInflater) as VB
            }
        }
        throw Throwable("获取ViewBinding失败")
    }

}