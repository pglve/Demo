/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.base

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.storeData: DataStore<Settings>
    get() = createDataStore(
        fileName = "settings.pb",
        serializer = getSerializer()
    )

suspend fun DataStore<Settings>.writeStoreData(apply: (Settings) -> Unit) {
    updateData {
        it.apply { apply(it) }
    }
}

val Context.preferencesData: DataStore<Preferences>
    get() = createDataStore(name = "settings")

suspend inline fun <reified T : Any> DataStore<Preferences>.read(keyName: String): T {
    val key = preferencesKey<T>(keyName)
    return this.data
        .map { preferences ->
            preferences[key] ?: new()
        }.first()
}

suspend inline fun <reified T : Any> DataStore<Preferences>.write(keyName: String, value: T) {
    val key = preferencesKey<T>(keyName)
    this.edit { settings ->
        settings[key] = value
    }
}



