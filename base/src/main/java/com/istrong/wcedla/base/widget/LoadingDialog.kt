package com.istrong.wcedla.base.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.istrong.wcedla.base.R
import com.istrong.wcedla.base.extension.dp

class LoadingDialog(context: Context) : Dialog(context, R.style.LoadingDialog) {

    private val layoutParams by lazy { ViewGroup.LayoutParams(100.dp, 100.dp) }
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = LayoutInflater.from(context).inflate(R.layout.loading_dialog_layout, null, false)
        view.layoutParams = layoutParams
        setContentView(view, layoutParams)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

}