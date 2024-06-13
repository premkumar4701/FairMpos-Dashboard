package ui.utils

import android.content.Context

lateinit var appContext: Context
actual fun getContext(): Any? = appContext