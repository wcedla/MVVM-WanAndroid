package com.istrong.wcedla.base.utils

import androidx.appcompat.app.AppCompatActivity

object ActivityStack {

    private val activityList = mutableListOf<AppCompatActivity>()

    fun addActivity(appCompatActivity: AppCompatActivity) {
        activityList.add(appCompatActivity)
    }

    fun removeActivity(appCompatActivity: AppCompatActivity) {
        activityList.remove(appCompatActivity)
    }

    fun getActivityList() = activityList

    fun getTopActivity() = activityList.firstOrNull()

    fun finishAll() {
        activityList.forEach {
            it.finish()
        }
    }

}