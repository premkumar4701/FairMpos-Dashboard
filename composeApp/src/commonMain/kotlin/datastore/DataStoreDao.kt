package datastore

import androidx.datastore.preferences.core.Preferences

interface DataStoreDao {
  suspend fun getBaseUrl(): String?

  suspend fun setBaseUrl(baseUrl: String): Preferences

  suspend fun getSetup(): Boolean

  suspend fun setSetup(isSetup: Boolean): Preferences

  suspend fun getLicenseExpired(): Boolean

  suspend fun setLicenseExpired(isLicenseExpired: Boolean): Preferences

  suspend fun getAuthenticated(): Boolean

  suspend fun setAuthenticated(isAuthenticated: Boolean): Preferences

  suspend fun getLicenseExpiredOn(): String?

  suspend fun setLicenseExpiredOn(licenseExpiredOn: String): Preferences
}
