package di

import Greeting
import org.koin.dsl.module

val AppModule = module {
    factory { Greeting() }
}
