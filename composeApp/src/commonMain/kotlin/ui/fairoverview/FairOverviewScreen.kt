package ui.fairoverview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.baseline_arrow_forward_ios_24
import fairmpos_dashboard.composeapp.generated.resources.ic_calendar_days
import fairmpos_dashboard.composeapp.generated.resources.ic_mobile
import fairmpos_dashboard.composeapp.generated.resources.ic_trophy
import fairmpos_dashboard.composeapp.generated.resources.search_titles
import org.jetbrains.compose.resources.painterResource

@Composable
fun FairOverviewScreen(modifier: Modifier = Modifier, fairID: Long?, hasBills: Boolean?) {
  Column(modifier = modifier) {
    Text(
        "21st May, 2024 to 21st Jun, 2024",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
    FairOverviewCard()
    Spacer(Modifier.padding(5.dp))
    BestSellerCardView()
    Spacer(Modifier.padding(5.dp))
    DateWiseSaleCardView()
    Spacer(Modifier.padding(5.dp))
    BillingDevicesInformationCardView()
    Spacer(Modifier.padding(5.dp))
    SearchTitlesCardView()
    Spacer(Modifier.padding(5.dp))
    BillingDetailsCardView()
  }
}

@Composable
fun FairOverviewCard() {
  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth(), elevation = 4.dp) {
    Column(modifier = Modifier.padding(8.dp)) {
      Text("Net Sales", modifier = Modifier.padding(top = 8.dp, end = 8.dp))
      Spacer(modifier = Modifier.padding(4.dp))
      Text("5,707", fontSize = 32.sp)
      Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Nett Qty Sold", modifier = Modifier.weight(1f))
        Text(text = "12", fontWeight = FontWeight.SemiBold)
      }
      Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Sales Amount", modifier = Modifier.weight(1f))
        Text(text = "355.00", fontWeight = FontWeight.SemiBold)
      }
      Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Return Amount", modifier = Modifier.weight(1f))
        Text(text = "2352.00", fontWeight = FontWeight.SemiBold)
      }
      Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Cancellations Amount", modifier = Modifier.weight(1f))
        Text(text = "23352.00", fontWeight = FontWeight.SemiBold)
      }
      Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("No. of Bills", modifier = Modifier.weight(1f))
        Text(text = "23", fontWeight = FontWeight.SemiBold)
      }
      Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "No. of cancelled Bills", modifier = Modifier.weight(1f))
        Text(text = "43", fontWeight = FontWeight.SemiBold)
      }
    }
  }
}

@Composable
fun BestSellerCardView() {
  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth(), elevation = 4.dp) {
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
  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth(), elevation = 4.dp) {
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
  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth(), elevation = 4.dp) {
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
  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth(), elevation = 4.dp) {
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
fun BillingDetailsCardView() {
  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth(), elevation = 4.dp) {
    Column {
      Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "No.of Devices", modifier = Modifier.weight(1f).padding(start = 4.dp))
        Text(text = "Billing Days", modifier = Modifier.padding(end = 8.dp))
      }
      Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "43",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f).padding(start = 4.dp))
        Text(text = "12", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(end = 8.dp))
      }
    }
  }
}
