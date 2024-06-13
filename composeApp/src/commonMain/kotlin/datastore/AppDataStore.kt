package datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import co.touchlab.stately.concurrency.Synchronizable
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import okio.Path.Companion.toPath

internal const val APP_PREFERENCE_DATASTORE = "app.preferences_pb"

object AppDataStore {
    private lateinit var dataStore: DataStore<Preferences>
    private val lock = SynchronizedObject()

    fun createDataSource(
        producePath: () -> String
    ) : DataStore<Preferences> {
        return synchronized(lock){
            if (::dataStore.isInitialized){
                dataStore
            } else {
                PreferenceDataStoreFactory.createWithPath(
                    produceFile = { producePath().toPath() }
                ).also { dataStore = it }
            }
        }
    }
}

expect fun dataStorePreferences(context: Any? = null): DataStore<Preferences>
