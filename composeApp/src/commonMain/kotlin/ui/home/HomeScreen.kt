package ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import api.fairdashboard.FairDashboard
import api.fairdashboard.FairType
import enum.FairMposScreens
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.ic_undraw_empty
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import theme.colorLabel
import theme.colorPrimary
import theme.colorProgressBar
import theme.colorText
import utils.getUIDate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = koinInject()
) {
  Column(modifier = modifier) {
    val options = FairType.entries
    val expanded = remember { mutableStateOf(false) }
    val isEmpty = remember { mutableStateOf(false) }
    val selectedFairs = remember { mutableStateOf(options[0]) }
    val fairDashboardData = remember { mutableStateOf(listOf<FairDashboard>()) }

    LaunchedEffect(true) {
      homeViewModel.loadFairDashboard(selectedFairs.value)
      homeViewModel.fetchFairData.collectLatest { fairView ->
        isEmpty.value = fairView.fairList.isEmpty()
        fairDashboardData.value = fairView.fairList
      }
    }
    LaunchedEffect(true) {
      homeViewModel.isUnAuthorized.collectLatest {
        if (it) {
          navHostController.navigate(FairMposScreens.Login.name)
        }
      }
    }

    Card(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(horizontal = 8.dp),
        elevation = 4.dp) {
          Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(8.dp)
                      .clip(RoundedCornerShape(4.dp))
                      .border(BorderStroke(1.dp, Color.DarkGray), RoundedCornerShape(4.dp))
                      .clickable { expanded.value = !expanded.value }
                      .padding(10.dp)) {
                Text(
                    text = selectedFairs.value.text,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start)

                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
              }

          DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { selectionOption ->
              DropdownMenuItem(
                  onClick = {
                    selectedFairs.value = selectionOption
                    expanded.value = false
                    homeViewModel.loadFairDashboard(selectedFairs.value)
                  }) {
                    Text(
                        text = selectionOption.text,
                        color =
                            if (selectionOption == selectedFairs.value) colorPrimary else colorText)
                  }
            }
          }
        }
    Spacer(Modifier.padding(5.dp))
    when {
      homeViewModel.isShowLoading -> {
        LoadingProgressBar()
      }
      isEmpty.value -> {
        NoDataCard()
      }
      !isEmpty.value && !homeViewModel.isShowLoading -> {
        CardList(selectedFairs.value, fairDashboardData.value, navHostController)
      }
    }
  }
}

@Composable
fun CardList(
    fairType: FairType,
    fairDashboardData: List<FairDashboard>,
    navHostController: NavHostController
) {
  LazyColumn(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
    items(fairDashboardData) { fair ->
      val fairStartDate = fair.startDate
      val fairEndDate = fair.endDate
      val fairStartDateInSystemZone =
          fairStartDate.toLocalDateTime(TimeZone.currentSystemDefault()).date
      val fairEndDateInSystemZone =
          fairEndDate.toLocalDateTime(TimeZone.currentSystemDefault()).date
      val fairDate =
          "${fairStartDateInSystemZone.getUIDate()} to ${fairEndDateInSystemZone.getUIDate()}"
      val hasBills: Boolean = fair.totalBills > 0
      if (fairType == FairType.TODAY) {
        TodayFairCardItem(
            fairName = fair.fairName,
            schoolName = fair.schoolName,
            totalNetSale = fair.totalNettValue,
            totalNetQtySold = fair.totalNettSoldQty ?: 0,
            onClick = {
              navHostController.navigate(
                  "${FairMposScreens.FairOverview.name}/${fair.fairID}/$hasBills")
            },
            todayNetSale = fair.todayNettValue,
            todayNetQtySold = fair.todayNettSoldQty)
      } else {
        CardItem(
            fairName = fair.fairName,
            schoolName = fair.schoolName,
            totalNetSale = fair.totalNettValue,
            totalNetQtySold = fair.totalNettSoldQty ?: 0,
            fairDate = fairDate,
            fairStatus = fair.status,
            onClick = {
              navHostController.navigate(
                  "${FairMposScreens.FairOverview.name}/${fair.fairID}/$hasBills")
            })
      }
    }
  }
}

@Composable
fun CardItem(
    fairName: String,
    schoolName: String?,
    totalNetSale: Double,
    totalNetQtySold: Int,
    fairDate: String,
    fairStatus: String,
    onClick: () -> Unit
) {
  Card(
      modifier =
          Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp).clickable {
            onClick()
          },
      elevation = 4.dp,
      shape = RoundedCornerShape(4.dp)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
          Text(textAlign = TextAlign.Center, text = fairName, fontSize = 16.sp, color = colorText)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Total Net Sale",
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                color = colorLabel)
            Text(text = "Total Net Qty Sold", fontSize = 12.sp, color = colorLabel)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = totalNetSale.toString(),
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = colorText)
            Text(text = totalNetQtySold.toString(), fontSize = 16.sp, color = colorText)
          }
          if (schoolName != null) {
            Text(
                textAlign = TextAlign.Center,
                text = schoolName,
                fontSize = 12.sp,
                color = colorText)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = fairDate,
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                color = colorText)
            Text(
                text = fairStatus,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorText)
          }
        }
      }
}

@Composable
fun TodayFairCardItem(
    fairName: String,
    schoolName: String?,
    totalNetSale: Double,
    totalNetQtySold: Int,
    onClick: () -> Unit,
    todayNetSale: Double,
    todayNetQtySold: Int
) {
  Card(
      modifier =
          Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp).clickable {
            onClick()
          },
      elevation = 4.dp,
      shape = RoundedCornerShape(4.dp)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
          Text(textAlign = TextAlign.Center, text = fairName, fontSize = 16.sp, color = colorText)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Today's Net Sale",
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                color = colorLabel)
            Text(text = "Total Net Sale", fontSize = 12.sp, color = colorLabel)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = todayNetSale.toString(),
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = colorText)
            Text(text = totalNetSale.toString(), fontSize = 16.sp, color = colorText)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Today's Net Qty Sold",
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                color = colorLabel)
            Text(text = "Total Net Qty Sold", fontSize = 12.sp, color = colorLabel)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = todayNetQtySold.toString(),
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = colorText)
            Text(text = totalNetQtySold.toString(), fontSize = 16.sp, color = colorText)
          }
          if (schoolName != null) {
            Text(
                textAlign = TextAlign.Center,
                text = schoolName,
                fontSize = 12.sp,
                color = colorText)
          }
        }
      }
}

@Composable
fun NoDataCard() {
  Box(Modifier.fillMaxSize()) {
    Card(
        shape =
            RoundedCornerShape(
                topStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp),
        modifier = Modifier.wrapContentSize().padding(8.dp).align(Alignment.Center),
        elevation = 8.dp) {
          Column(
              Modifier.wrapContentSize().padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(Res.drawable.ic_undraw_empty),
                    contentDescription = "",
                    modifier = Modifier.size(128.dp),
                    contentScale = ContentScale.Crop)
                Spacer(Modifier.padding(5.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = "No fairs currently running",
                    style = MaterialTheme.typography.h6,
                )
              }
        }
  }
}

@Composable
fun LoadingProgressBar() {
  Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    CircularProgressIndicator(color = colorProgressBar)
  }
}

fun navigateToNextScreen(navHostController: NavHostController) {
    navHostController.navigate(FairMposScreens.BillingDeviceInfo.name)
}

