package ui.fairoverview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import api.fairoverview.FairOverView
import enum.FairMposScreens
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.baseline_arrow_forward_ios_24
import fairmpos_dashboard.composeapp.generated.resources.ic_baseline_info
import fairmpos_dashboard.composeapp.generated.resources.ic_calendar_days
import fairmpos_dashboard.composeapp.generated.resources.ic_mobile
import fairmpos_dashboard.composeapp.generated.resources.ic_trophy
import fairmpos_dashboard.composeapp.generated.resources.search_titles
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import theme.colorPrimaryVariant
import ui.home.LoadingProgressBar
import utils.getUIDate

@Composable
fun FairOverviewScreen(
    modifier: Modifier = Modifier,
    fairID: Long?,
    hasBills: Boolean?,
    navHostController: NavHostController
) {
  val viewModel: FairOverviewViewModel = koinInject { parametersOf(fairID, hasBills) }
  val isEmpty = remember { mutableStateOf(false) }
  val isDialogShow = remember { mutableStateOf(false) }
  val fairOverView: MutableState<FairOverView?> = remember { mutableStateOf(null) }
  LaunchedEffect(true) {
    viewModel.fairOverview.collectLatest { fairOverView.value = it }
    launch {
      viewModel.isUnAuthorized.collectLatest {
        if (it) {
          navHostController.navigate(FairMposScreens.Login.name)
        }
      }
    }
  }
  isEmpty.value = !hasBills!!
  viewModel.loadFairOverview()
  if (isDialogShow.value) {
    FairInformationDialog(fairOverView, isDialogShow)
  }
  Column(modifier = modifier) {
    val fairDate =
        "${
              fairOverView.value?.startDate?.toLocalDateTime(TimeZone.currentSystemDefault())?.date?.getUIDate()
          } to ${
              fairOverView.value?.endDate?.toLocalDateTime(TimeZone.currentSystemDefault())?.date?.getUIDate()
          }"
    if (!isEmpty.value || !viewModel.isLoading) {
      Box(
          modifier =
              Modifier.fillMaxWidth().wrapContentHeight().background(color = colorPrimaryVariant)) {
            Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
              Text(
                  fairOverView.value?.school ?: "",
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(top = 8.dp, bottom = 8.dp).weight(1f))
              Image(
                  painter = painterResource(Res.drawable.ic_baseline_info),
                  contentDescription = "",
                  modifier = Modifier.size(30.dp).clickable { isDialogShow.value = true },
                  contentScale = ContentScale.Crop)
            }
          }
      Text(fairDate, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
      FairOverviewCard(fairOverView = fairOverView)
      Spacer(Modifier.padding(5.dp))
      BestSellerCardView()
      Spacer(Modifier.padding(5.dp))
      DateWiseSaleCardView()
      Spacer(Modifier.padding(5.dp))
      BillingDevicesInformationCardView()
      Spacer(Modifier.padding(5.dp))
      SearchTitlesCardView()
      Spacer(Modifier.padding(5.dp))
      BillingDetailsCardView(fairOverView = fairOverView)
    } else {
      LoadingProgressBar()
    }
  }
}

@Composable
fun FairOverviewCard(fairOverView: MutableState<FairOverView?>) {
  Card(
      modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(start = 8.dp, end = 8.dp),
      elevation = 4.dp) {
        Column(modifier = Modifier.padding(8.dp)) {
          Text("Net Sales", modifier = Modifier.padding(top = 8.dp, end = 8.dp))
          Spacer(modifier = Modifier.padding(4.dp))
          Text(fairOverView.value?.totalNettValue.toString(), fontSize = 32.sp)
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Nett Qty Sold", modifier = Modifier.weight(1f))
            Text(
                text = fairOverView.value?.totalNettSoldQty.toString(),
                fontWeight = FontWeight.SemiBold)
          }
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Sales Amount", modifier = Modifier.weight(1f))
            Text(text = fairOverView.value?.salesValue.toString(), fontWeight = FontWeight.SemiBold)
          }
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Return Amount", modifier = Modifier.weight(1f))
            Text(
                text = fairOverView.value?.returnedValue.toString(),
                fontWeight = FontWeight.SemiBold)
          }
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Cancellations Amount", modifier = Modifier.weight(1f))
            Text(
                text = fairOverView.value?.cancelledValue.toString(),
                fontWeight = FontWeight.SemiBold)
          }
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("No. of Bills", modifier = Modifier.weight(1f))
            Text(text = fairOverView.value?.noOfBills.toString(), fontWeight = FontWeight.SemiBold)
          }
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "No. of cancelled Bills", modifier = Modifier.weight(1f))
            Text(
                text = fairOverView.value?.totalCancelledBills.toString(),
                fontWeight = FontWeight.SemiBold)
          }
        }
      }
}

@Composable
fun BestSellerCardView() {
  Card(
      modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(start = 8.dp, end = 8.dp),
      elevation = 4.dp) {
        Column {
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.ic_trophy),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Crop)
            Text(
                text = "Best sellers",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).padding(start = 4.dp))
            Image(
                painter = painterResource(Res.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "",
                modifier = Modifier.size(16.dp).padding(end = 8.dp),
                contentScale = ContentScale.Crop)
          }
        }
      }
}

@Composable
fun DateWiseSaleCardView() {
  Card(
      modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(start = 8.dp, end = 8.dp),
      elevation = 4.dp) {
        Column {
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.ic_calendar_days),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Crop)
            Text(
                text = "Date wise sale",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).padding(start = 4.dp))
            Image(
                painter = painterResource(Res.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "",
                modifier = Modifier.size(16.dp).padding(end = 8.dp),
                contentScale = ContentScale.Crop)
          }
        }
      }
}

@Composable
fun BillingDevicesInformationCardView() {
  Card(
      modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(start = 8.dp, end = 8.dp),
      elevation = 4.dp) {
        Column {
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.ic_mobile),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Crop)
            Text(
                text = "Billing Devices Information",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).padding(start = 4.dp))
            Image(
                painter = painterResource(Res.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "",
                modifier = Modifier.size(16.dp).padding(end = 8.dp),
                contentScale = ContentScale.Crop)
          }
        }
      }
}

@Composable
fun SearchTitlesCardView() {
  Card(
      modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(start = 8.dp, end = 8.dp),
      elevation = 4.dp) {
        Column {
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.search_titles),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Crop)
            Text(
                text = "Search Titles",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).padding(start = 4.dp))
            Image(
                painter = painterResource(Res.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "",
                modifier = Modifier.size(16.dp).padding(end = 8.dp),
                contentScale = ContentScale.Crop)
          }
        }
      }
}

@Composable
fun BillingDetailsCardView(fairOverView: MutableState<FairOverView?>) {
  Card(
      modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(start = 8.dp, end = 8.dp),
      elevation = 4.dp) {
        Column {
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "No.of Devices", modifier = Modifier.weight(1f).padding(start = 4.dp))
            Text(text = "Billing Days", modifier = Modifier.padding(end = 8.dp))
          }
          Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = fairOverView.value?.noOfDevices.toString(),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).padding(start = 4.dp))
            Text(
                text = fairOverView.value?.noOfBillingDays.toString(),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 8.dp))
          }
        }
      }
}

@Composable
fun FairInformationDialog(
    fairOverView: MutableState<FairOverView?>,
    isDialogShow: MutableState<Boolean>
) {

  Dialog(onDismissRequest = { isDialogShow.value = false }) {
    Surface(shape = RoundedCornerShape(8.dp), color = Color.White) {
      Box(contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
              text = "Fair Information",
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp,
              modifier = Modifier.padding(start = 8.dp, end = 8.dp))
          Text(
              text = "Fair Name",
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(start = 8.dp, end = 8.dp))
          Text(
              text = "${fairOverView.value?.code} - ${fairOverView.value?.name}",
              modifier = Modifier.padding(start = 8.dp, end = 8.dp))
          Spacer(modifier = Modifier.padding(8.dp))
          if (fairOverView.value?.school != null) {
            Text(
                text = "School Name",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Text(
                text = fairOverView.value?.school ?: "",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(8.dp))
          }

          Row(
              modifier = Modifier.padding(start = 4.dp, end = 4.dp),
              verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Fair from",
                    modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.SemiBold)
                Text(
                    text = "Fair to",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp))
              }
          Row(
              modifier = Modifier.padding(start = 4.dp, end = 4.dp),
              verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text =
                        "${fairOverView.value?.startDate?.toLocalDateTime(TimeZone.currentSystemDefault())?.date?.getUIDate()}",
                    modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp))
                Text(
                    text =
                        "${fairOverView.value?.endDate?.toLocalDateTime(TimeZone.currentSystemDefault())?.date?.getUIDate()}",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp))
              }
          Row(
              modifier = Modifier.padding(start = 4.dp, end = 4.dp),
              verticalAlignment = Alignment.CenterVertically) {
                Text(text = "", modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp))
                Button(
                    onClick = { isDialogShow.value = false },
                    colors =
                        ButtonDefaults.buttonColors(
                            backgroundColor = Color.White, contentColor = Color(0xFF127FBF))) {
                      Text(text = "OK")
                    }
              }
        }
      }
    }
  }
}
