package enum

import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.billing_device_info
import fairmpos_dashboard.composeapp.generated.resources.desktop_platform
import fairmpos_dashboard.composeapp.generated.resources.fair_overview
import fairmpos_dashboard.composeapp.generated.resources.home
import fairmpos_dashboard.composeapp.generated.resources.login
import fairmpos_dashboard.composeapp.generated.resources.no_connection
import fairmpos_dashboard.composeapp.generated.resources.setup
import org.jetbrains.compose.resources.StringResource

enum class FairMposScreens(val title: StringResource) {
    PlaceHolder(Res.string.setup),
    Setup(Res.string.setup),
    Login(Res.string.login),
    Home(Res.string.home),
    NoConnection(Res.string.no_connection),
    BillingDeviceInfo(Res.string.billing_device_info),
    FairOverview(Res.string.fair_overview)
}

enum class PlatFormType(val names: StringResource){
    Desktop(Res.string.desktop_platform)
}
