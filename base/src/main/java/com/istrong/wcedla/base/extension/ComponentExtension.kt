package com.istrong.wcedla.base.extension

import java.lang.reflect.ParameterizedType

//inline fun <reified VB : ViewBinding> AppCompatActivity.getViewBinding(inflater: LayoutInflater) =
//    lazy {
//        val method = VB::class.java.getDeclaredMethod("inflate", LayoutInflater::class.java)
//        return@lazy method.invoke(null, inflater) as VB
//    }
//
//inline fun <reified VM : BaseViewModel> AppCompatActivity.getViewModel() =
//    viewModels<VM>()

/**
 * 获取当前类的所有泛型Class类型
 */
fun Any.getGenericClassList(): List<Class<*>> {
    val genericClassList = mutableListOf<Class<*>>()
    val type = this.javaClass.genericSuperclass
    if (type is ParameterizedType) {
        type.actualTypeArguments.forEach {
            if (it is Class<*>) {
                genericClassList.add(it)
            }
        }
    }
    return genericClassList
}