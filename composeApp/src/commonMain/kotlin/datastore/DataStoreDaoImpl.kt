package datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map



class DataStoreDaoImpl(private val preferenceDataStore: DataStore<Preferences>) : DataStoreDao {

  private val keyBaseUrl = stringPreferencesKey("base_url_key")
  private val keyIsSetup = booleanPreferencesKey("is_setup_key")
  private val keyIsLicenseExpired = booleanPreferencesKey("validation_flag")
  private val keyLicenseExpiredOn = stringPreferencesKey("license_expired_on")
  private val keyAuthentication = booleanPreferencesKey("key_authentication")

  override suspend fun getBaseUrl(): String? =
      preferenceDataStore.data.map { preferences -> preferences[keyBaseUrl] }.first()

  override suspend fun setBaseUrl(baseUrl: String): Preferences =
      preferenceDataStore.edit { preferences -> preferences[keyBaseUrl] = baseUrl }

  override suspend fun getSetup(): Boolean =
      preferenceDataStore.data.map { preferences -> preferences[keyIsSetup] ?: false }.first()

  override suspend fun setSetup(isSetup: Boolean): Preferences =
      preferenceDataStore.edit { preferences -> preferences[keyIsSetup] = isSetup }

  override suspend fun getLicenseExpired(): Boolean =
      preferenceDataStore.data
          .map { preferences -> preferences[keyIsLicenseExpired] ?: false }
          .first()

  override suspend fun setLicenseExpired(isLicenseExpired: Boolean): Preferences =
      preferenceDataStore.edit { preferences ->
        preferences[keyIsLicenseExpired] = isLicenseExpired
      }

  override suspend fun getAuthenticated(): Boolean =
      preferenceDataStore.data
          .map { preferences -> preferences[keyAuthentication] ?: false }
          .first()

  override suspend fun setAuthenticated(isAuthenticated: Boolean): Preferences =
      preferenceDataStore.edit { preferences -> preferences[keyAuthentication] = isAuthenticated }

  override suspend fun getLicenseExpiredOn(): String? =
      preferenceDataStore.data.map { preferences -> preferences[keyLicenseExpiredOn] }.first()

  override suspend fun setLicenseExpiredOn(licenseExpiredOn: String): Preferences =
      preferenceDataStore.edit { preferences ->
        preferences[keyLicenseExpiredOn] = licenseExpiredOn
      }
}
