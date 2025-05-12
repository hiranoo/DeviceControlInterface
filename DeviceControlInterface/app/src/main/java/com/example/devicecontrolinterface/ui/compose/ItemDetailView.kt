package com.example.devicecontrolinterface.ui.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.devicecontrolinterface.data.Item

@Composable
fun ItemDetailView(
    item: Item,
    onUpdateItem: (Item) -> Unit,
    onDeleteItem: (Item) -> Unit,
    onCloseDetail: () -> Unit
) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedTag by remember { mutableStateOf(item.tag) }
    var editedValue by remember { mutableStateOf(item.value) }
    var editedValueType by remember { mutableStateOf(item.valueType) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = editedName,
                onValueChange = { editedName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = editedTag,
                onValueChange = { editedTag = it },
                label = { Text("Tag") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = editedValue,
                onValueChange = { editedValue = it },
                label = { Text("Value") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = editedValueType,
                onValueChange = { editedValueType = it },
                label = { Text("Value Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onUpdateItem(item.copy(
                        name = editedName,
                        tag = editedTag,
                        value = editedValue,
                        valueType = editedValueType
                    ))
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Update Item")
            }

            Button(
                onClick = { onDeleteItem(item) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Delete Item")
            }

            Button(
                onClick = onCloseDetail,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Close Detail")
            }
        }
    }
}