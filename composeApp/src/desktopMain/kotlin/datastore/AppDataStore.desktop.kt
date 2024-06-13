package datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun dataStorePreferences(context: Any?): DataStore<Preferences> {
    return AppDataStore.createDataSource(
        producePath = {
            APP_PREFERENCE_DATASTORE
        }
    )
}