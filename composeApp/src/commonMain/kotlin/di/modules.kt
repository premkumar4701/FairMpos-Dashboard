package di

import datastore.DataStoreDaoImpl
import datastore.dataStorePreferences
import org.koin.dsl.module
import ui.login.LoginViewModel
import ui.setup.SetupViewModel
import ui.utils.getContext

val AppModule = module {
  single { dataStorePreferences(context = getContext()) }
  single { DataStoreDaoImpl(preferenceDataStore = get()) }
  factory { LoginViewModel() }
  factory { SetupViewModel(dataStoreDaoImpl = get()) }
}
