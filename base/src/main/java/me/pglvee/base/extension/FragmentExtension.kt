/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.extension

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.*

/**实例化 Fragment*/
inline fun <reified T : Fragment> Context.newInstanceFragment(args: Bundle? = null): T {
    val className = T::class.java.name;
    val clazz = FragmentFactory.loadFragmentClass(
        classLoader, className
    )
    val f = clazz.getConstructor().newInstance()
    if (args != null) {
        args.classLoader = f.javaClass.classLoader
        f.arguments = args
    }
    return f as T
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun FragmentActivity.addFragment(frameId: Int, fragment: Fragment) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun FragmentActivity.replaceFragment(frameId: Int, fragment: Fragment) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}
