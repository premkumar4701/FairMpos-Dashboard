package ui.main

import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.desktop_platform
import fairmpos_dashboard.composeapp.generated.resources.home
import fairmpos_dashboard.composeapp.generated.resources.login
import fairmpos_dashboard.composeapp.generated.resources.setup
import org.jetbrains.compose.resources.StringResource

enum class FairMposScreens(val tittle: StringResource) {
    PlaceHolder(Res.string.setup),
    Setup(Res.string.setup),
    Login(Res.string.login),
    Home(Res.string.home)
}

enum class PlatFormType(val names: StringResource){
    Desktop(Res.string.desktop_platform)
}
