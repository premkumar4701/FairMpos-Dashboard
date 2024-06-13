package com.fairmpos.dashboard

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.utils.appContext

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.statusBarColor = getColor(R.color.statusBar)
    appContext = this
    setContent { App() }
  }
}

@Preview(showSystemUi = true)
@Composable
fun AppAndroidPreview() {
  App()
}
