package com.istrong.wcedla.base.utils

object CommonUtil {


    /**
     * 格式化权限请求说明
     */
    fun formatDenyPermission(deniedList: List<String>): String {
        val denyTipsString = StringBuilder()
        var needLocationPermissionTips = false
        var needPhoneStatePermissionTips = false
        var needCameraPermissionTips = false
        var needRecordAudioPermissionTips = false
        var needAccessStoragePermissionTips = false
        deniedList.forEach {
            when (it) {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS -> {
                    needLocationPermissionTips = true
                }
                android.Manifest.permission.READ_PHONE_STATE -> {
                    needPhoneStatePermissionTips = true
                }
                android.Manifest.permission.CAMERA -> {
                    needCameraPermissionTips = true
                }
                android.Manifest.permission.RECORD_AUDIO -> {
                    needRecordAudioPermissionTips = true
                }
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    needAccessStoragePermissionTips = true
                }
                else -> {
                }
            }
        }
        if (needLocationPermissionTips) {
            denyTipsString.append("定位")
            denyTipsString.append(",")
        }
        if (needPhoneStatePermissionTips) {
            denyTipsString.append("电话")
            denyTipsString.append(",")
        }
        if (needCameraPermissionTips) {
            denyTipsString.append("相机")
            denyTipsString.append(",")
        }
        if (needRecordAudioPermissionTips) {
            denyTipsString.append("麦克风")
            denyTipsString.append(",")
        }
        if (needAccessStoragePermissionTips) {
            denyTipsString.append("存储")
        } else {
            denyTipsString.deleteCharAt(denyTipsString.lastIndex)
        }
        return denyTipsString.toString()
    }
}