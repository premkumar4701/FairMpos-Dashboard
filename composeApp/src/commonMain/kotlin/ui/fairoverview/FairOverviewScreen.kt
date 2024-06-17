package ui.fairoverview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FairOverviewScreen(modifier: Modifier = Modifier) {
  Column(modifier = modifier) {
    Text(
        "21st May, 2024 to 21st Jun, 2024",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
    FairOverviewCard()
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
