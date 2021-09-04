/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.extension

import android.app.Activity
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**activity参数获取*/
class ActivityExtras<T>(private val extraName: String, private val defaultValue: T) :
    ReadOnlyProperty<Activity, T> {
    private var extra: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return extra ?: thisRef.intent?.get<T>(extraName)?.also { extra = it }
        ?: defaultValue.also { extra = it }
    }
}

/**fragment参数获取*/
class FragmentExtras<T>(private val extraName: String, private val defaultValue: T) :
    ReadOnlyProperty<Fragment, T> {

    private var extra: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return extra ?: thisRef.arguments?.get<T>(extraName)?.also { extra = it }
        ?: defaultValue.also { extra = it }
    }
}

fun <T> extraFrag(extraName: String): FragmentExtras<T?> = FragmentExtras(extraName, null)

fun <T> extraFrag(extraName: String, defaultValue: T): FragmentExtras<T> =
    FragmentExtras(extraName, defaultValue)


fun <T> extraAct(extraName: String): ActivityExtras<T?> = ActivityExtras(extraName, null)

fun <T> extraAct(extraName: String, defaultValue: T): ActivityExtras<T> =
    ActivityExtras(extraName, defaultValue)