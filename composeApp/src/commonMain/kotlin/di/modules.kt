package di

import datastore.DataStoreDaoImpl
import datastore.dataStorePreferences
import api.bestsellers.BestSellersService
import api.billingdeviceinfo.BillingDeviceInfoService
import api.billitemsoverview.BillItemsOverviewService
import api.billwiseoverview.BillWiseOverviewService
import api.datewiseoverview.DateWiseOverviewService
import api.fairdashboard.FairDashboardService
import api.fairoverview.FairOverviewService
import api.login.LoginService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ui.login.LoginViewModel
import ui.setup.SetupViewModel
import ui.utils.getContext

fun appModule(enableNetworkLogs: Boolean) = module {
  single { dataStorePreferences(context = getContext()) }
  single { DataStoreDaoImpl(preferenceDataStore = get()) }factory { LoginViewModel() }
    factory { LoginViewModel(get()) }
  factory { SetupViewModel(dataStoreDaoImpl = get()) }
  /** Creates a http client for Ktor that is provided to the API client via constructor injection */
  single {
    HttpClient {
      expectSuccess = false
      addDefaultResponseValidation()

      defaultRequest {
        url {
          protocol = URLProtocol.HTTPS
          host = "mpos-stage.lspl.dev"
        }
      }

      if (enableNetworkLogs) {
        install(Logging) {
          level = LogLevel.HEADERS
          logger =
              object : Logger {
                override fun log(message: String) {}
              }
        }
      }

      install(ContentNegotiation) {
        json(
            Json {
              ignoreUnknownKeys = true
              isLenient = true
            })
      }
    }
  }
  single { LoginService(httpClient = get()) }
  single { FairDashboardService(httpClient = get()) }
  single { BestSellersService(httpClient = get()) }
  single { BillingDeviceInfoService(httpClient = get()) }
  single { BillItemsOverviewService(httpClient = get()) }
  single { BillWiseOverviewService(httpClient = get()) }
  single { DateWiseOverviewService(httpClient = get()) }
  single { FairOverviewService(httpClient = get()) }
}
