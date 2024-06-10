package di

import org.koin.dsl.module
import ui.login.LoginViewModel
import ui.setup.SetupViewModel

val AppModule = module {
  factory { LoginViewModel() }
  factory { SetupViewModel() }
}
