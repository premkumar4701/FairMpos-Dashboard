package ui.billingdeviceinfo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import api.billingdeviceinfo.FairDevicesLastSync
import enum.FairMposScreens
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.billingDays
import fairmpos_dashboard.composeapp.generated.resources.devices
import fairmpos_dashboard.composeapp.generated.resources.mobile_number
import fairmpos_dashboard.composeapp.generated.resources.name
import fairmpos_dashboard.composeapp.generated.resources.synced
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import theme.colorLabel
import uicomponents.LoadingProgressBar
import utils.timeAgo

@Composable
fun BillingDeviceInfoScreen(
    modifier: Modifier,
    navHostController: NavHostController,
    fairId: String,
    billingDeviceInfoViewModel: BillingDeviceInfoViewModel = koinInject()
) {
  val noOfDevices: MutableState<Int?> = remember { mutableStateOf(0) }
  val billingDays: MutableState<Int?> = remember { mutableStateOf(0) }
  val isEmpty: MutableState<Boolean?> = remember { mutableStateOf(null) }
  val fairDevicesLastSync: MutableState<List<FairDevicesLastSync>> = remember {
    mutableStateOf(listOf())
  }

  LaunchedEffect(true) {
    launch {
      billingDeviceInfoViewModel.getBillingDevicesInfo(fairId.toLong())
      billingDeviceInfoViewModel.billingDevicesInfo.collectLatest { billingDeviceInfo ->
        isEmpty.value = billingDeviceInfo?.fairDevicesLastSync?.isEmpty()
        noOfDevices.value = billingDeviceInfo?.noOfDevices
        billingDays.value = billingDeviceInfo?.billingDays
        fairDevicesLastSync.value = billingDeviceInfo?.fairDevicesLastSync ?: listOf()
      }
    }

    launch {
      billingDeviceInfoViewModel.isUnAuthorized.collectLatest { isUnAuthorized ->
        if (isUnAuthorized) {
          navHostController.navigate(FairMposScreens.Login.name)
        }
      }
    }
  }

  Column(modifier = modifier) {
      Row(
          modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
          verticalAlignment = Alignment.CenterVertically
      ) {
          Text(
              text = stringResource(Res.string.devices),
              modifier = Modifier.weight(1F),
              color = colorLabel
          )
          Text(text = stringResource(Res.string.billingDays), color = colorLabel)
      }
      Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
          Text(
              text = noOfDevices.value.toString(),
              modifier = Modifier.weight(1F),
              color = Color.Black,
              fontWeight = FontWeight.Bold
          )
          Text(
              text = billingDays.value.toString(),
              color = Color.Black,
              fontWeight = FontWeight.Bold
          )
      }
      if (billingDeviceInfoViewModel.isShowLoading) {
          LoadingProgressBar()
      } else {
          CardList(fairDevicesLastSync.value)
      }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardList(fairDevicesLastSyncList: List<FairDevicesLastSync>) {
  LazyColumn(modifier = Modifier.padding(8.dp)) {
    stickyHeader { HeaderCard() }
    items(fairDevicesLastSyncList) { item -> ItemCard(item) }
  }
}

@Composable
fun ItemCard(item: FairDevicesLastSync) {
  val hasSynced = item.lastSyncInstant != null
  var lastSyncedAt: LocalDateTime? = null
  var duration: Duration? = null
  if (hasSynced) {
    lastSyncedAt = item.lastSyncInstant?.toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
    duration = item.lastSyncInstant?.let { Clock.System.now().minus(it) }
  }
  Column(modifier = Modifier.background(Color.White)) {
    Row(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
      Text(text = item.name, modifier = Modifier.weight(1F), fontSize = 14.sp)
      Text(
          text =
              if (duration != null) {
                  lastSyncedAt.timeAgo(prefix = "")
              } else {
                "Not yet synced"
              },
          fontSize = 12.sp,
          color =
              if (duration != null) {
                if (duration.inWholeMinutes > 45) {
                  Color.Red
                } else {
                  Color.Black
                }
              } else {
                Color.Red
              })
    }
    Text(
        text = item.mobileNo ?: "",
        fontSize = 14.sp,
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
    Divider()
  }
}

@Composable
fun HeaderCard() {
  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().background(Color.White)) {
    Column {
      Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
        Text(
            text = stringResource(Res.string.name),
            modifier = Modifier.weight(1F),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp)
        Text(
            text = stringResource(Res.string.synced),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp)
      }
      Text(
          text = stringResource(Res.string.mobile_number),
          modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp)
    }
  }
}
