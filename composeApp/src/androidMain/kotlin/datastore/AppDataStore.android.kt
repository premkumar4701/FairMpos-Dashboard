package datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun dataStorePreferences(context: Any?): DataStore<Preferences> {
  require(value = context is Context, lazyMessage = { "Context is required" })
  return AppDataStore.createDataSource(
      producePath = { context.filesDir.resolve(APP_PREFERENCE_DATASTORE).absolutePath })
}
