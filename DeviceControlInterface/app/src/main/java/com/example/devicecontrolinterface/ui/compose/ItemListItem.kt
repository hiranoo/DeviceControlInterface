package com.example.devicecontrolinterface.ui.compose


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.devicecontrolinterface.data.Item

@Composable
fun ItemListItem(
    item: Item,
    onItemClick: (Item) -> Unit,
    onAvailabilityChange: (Item, Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item) }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .alpha(if (item.isAvailable) 1f else 0.6f)
        ) {
            Text(text = "Name: ${item.name}")
            Text(text = "Value: ${item.value}")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Checkbox(
            checked = item.isAvailable,
            onCheckedChange = { isChecked ->
                onAvailabilityChange(item, isChecked)
            }
        )
    }
}