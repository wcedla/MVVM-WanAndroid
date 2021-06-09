package com.istrong.wcedla.base.utils

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.istrong.wcedla.base.data.NetworkState
import com.orhanobut.logger.Logger

object NetWorkStateUtil {

    private val _netWorkState = MutableLiveData<NetworkState>()

    val networkState = _netWorkState

    private val connectivityManager by lazy {
        PlatformUtil.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private var isNetWorkStateBroadcastDestroy = false
    private val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
    private val networkStateBroadcast = NetworkStateBroadcast()
    private val networkStateActivityCallback = NetworkStateActivityCallback()


    internal fun initNetworkListener(application: Application) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            application.registerReceiver(networkStateBroadcast, intentFilter)
            application.registerActivityLifecycleCallbacks(networkStateActivityCallback)
        } else {
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    Logger.d("网络可用")
                    TrueTimeUtils.init(application)
                    _netWorkState.postValue(NetworkState.NETWORK_AVAILABLE)
                }

                override fun onLost(network: Network) {
                    Logger.d("网络丢失")
                    _netWorkState.postValue(NetworkState.NETWORK_UNAVAILABLE)
                }

                override fun onUnavailable() {
                    Logger.d("网络不可用")
                    _netWorkState.postValue(NetworkState.NETWORK_UNAVAILABLE)
                }

            })
        }
    }

    private class NetworkStateActivityCallback : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (isNetWorkStateBroadcastDestroy) {
                try {
                    activity.registerReceiver(networkStateBroadcast, intentFilter)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity.isTaskRoot) {
                try {
                    activity.unregisterReceiver(networkStateBroadcast)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                isNetWorkStateBroadcastDestroy = true
            }
        }
    }

    private class NetworkStateBroadcast : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if ((connectivityManager.activeNetworkInfo?.isConnected) != false) {
                _netWorkState.value = NetworkState.NETWORK_AVAILABLE
            } else {
                _netWorkState.value = NetworkState.NETWORK_UNAVAILABLE
            }
        }

    }

}