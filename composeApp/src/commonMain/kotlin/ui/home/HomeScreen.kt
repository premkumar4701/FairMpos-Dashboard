package ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.fairdashboard.FairDashboardResponse

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
  Column(modifier = modifier) {
    DropdownCard()
    Spacer(Modifier.padding(5.dp))
    //CardList()
  }
}

@Composable
fun DropdownCard() {

  val options = listOf("Today's Fairs", "Active Fairs", "Closed Fairs", "All Fairs")

  val expanded = remember { mutableStateOf(false) }

  val selectedFairs = remember { mutableStateOf(options[0]) }

  Card(modifier = Modifier.wrapContentHeight().fillMaxWidth(), elevation = 4.dp) {
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
              text = selectedFairs.value,
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
            }) {
              Text(
                  text = selectionOption,
                  color = if (selectionOption == selectedFairs.value) Color.Cyan else Color.Black)
            }
      }
    }
  }
}

@Composable
fun CardItem(
    fairName: String,
    schoolName: String,
    totalNetSale: Double,
    totalNetQtySold: Int,
    // TODO fairStartDate,
    // TODO fairEndDate,
    fairStatus: String,
    onClick: () -> Unit
) {
  Card(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() }, elevation = 4.dp) {
    Text(
        textAlign = TextAlign.Center,
        text = fairName,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(16.dp))
    Spacer(Modifier.padding(5.dp))
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Text(
          text = "Total Net Sale",
          style = MaterialTheme.typography.h6,
          modifier = Modifier.weight(1f))
      Text(text = "Total Net Qty Sold", style = MaterialTheme.typography.body2)
    }
    Spacer(Modifier.padding(5.dp))
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Text(
          text = totalNetSale.toString(),
          style = MaterialTheme.typography.h6,
          modifier = Modifier.weight(1f))
      Text(text = totalNetQtySold.toString(), style = MaterialTheme.typography.body2)
    }
    Spacer(Modifier.padding(5.dp))
    Text(
        textAlign = TextAlign.Center,
        text = schoolName,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(16.dp))
    Spacer(Modifier.padding(5.dp))
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Text(
          text = "1st May 2021 - 05 May 2021",
          style = MaterialTheme.typography.h6,
          modifier = Modifier.weight(1f))
      Text(text = fairStatus, style = MaterialTheme.typography.body2)
    }
  }
}

@Composable
fun CardList(fairDashboardResponse: FairDashboardResponse) {
  LazyColumn(modifier = Modifier.fillMaxSize()) {
    items(fairDashboardResponse.data) { fair ->
      CardItem(
          fairName = fair.fairName,
          schoolName = fair.schoolName,
          totalNetSale = fair.totalNettValue,
          totalNetQtySold = fair.totalNettSoldQty,
          fairStatus = fair.status,
          onClick = {})
    }
  }
}
