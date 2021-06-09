package com.istrong.wcedla.base.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.istrong.wcedla.base.R
import com.permissionx.guolindev.dialog.RationaleDialog

class PermissionDialog(
    context: Context,
    titleText: String?,
    message: String?,
    private val permissionList: MutableList<String>
) : RationaleDialog(context, R.style.PermissionDialog) {

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_rational_view, null, false)
        view.findViewById<TextView>(R.id.tvPermissionReason).text = message
        view.findViewById<TextView>(R.id.tvPermissionTitle).text = titleText
        val width = context.resources.displayMetrics.widthPixels.toFloat()
        val layoutParams =
            ViewGroup.LayoutParams((width * 4 / 5).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        setContentView(view, layoutParams)
    }

    fun showPermissionDialog() {
        if (this.isShowing) {
            cancel()
        }
        show()
    }

    fun cancelPermissionDialog() {
        if (this.isShowing) {
            cancel()
        }
    }

    override fun getPositiveButton(): View {
        return findViewById(R.id.btnPermissionGo) as View
    }

    override fun getNegativeButton(): View? {
        return findViewById(R.id.btnPermissionCancel) as? View
    }

    override fun getPermissionsToRequest(): MutableList<String> {
        return permissionList
    }

}