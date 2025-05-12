package com.example.devicecontrolinterface.ui.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devicecontrolinterface.ui.compose.ItemDetailView
import com.example.devicecontrolinterface.ui.compose.ItemListItem
import com.example.devicecontrolinterface.data.Item
import com.example.devicecontrolinterface.data.availableValueTypes
import com.example.devicecontrolinterface.ui.viewmodel.ItemViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import kotlin.text.first
import kotlin.text.forEach
import kotlin.text.format
import kotlin.text.get
import kotlin.text.set

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    itemViewModel: ItemViewModel = viewModel()
) {
    val items = itemViewModel.items
    val selectedItem = itemViewModel.selectedItem
    var showAddItemDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Item List") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddItemDialog = true }) {
                Icon(Icons.Filled.Add, "Add new item")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (selectedItem == null) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(items) { item ->
                        ItemListItem(
                            item = item,
                            onItemClick = { itemViewModel.selectItem(it) },
                            onAvailabilityChange = { itemToUpdate, isChecked ->
                                itemViewModel.updateItemAvailability(itemToUpdate, isChecked)
                            }
                        )
                    }
                }
            } else {
                ItemDetailView(
                    item = selectedItem,
                    onUpdateItem = { updatedItem ->
                        itemViewModel.updateItem(updatedItem)
                        itemViewModel.selectItem(null) // Close detail view after update
                    },
                    onDeleteItem = { itemToDelete ->
                        itemViewModel.deleteItem(itemToDelete)
                        itemViewModel.selectItem(null) // Close detail view after delete
                    },
                    onCloseDetail = { itemViewModel.selectItem(null) }
                )
            }
        }
    }

    // Add Item Dialog
    if (showAddItemDialog) {
        AddItemDialog(
            onAddItem = { newItem ->
                itemViewModel.addItem(newItem)
                showAddItemDialog = false
            },
            onDismiss = { showAddItemDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    onAddItem: (Item) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }

    // State for the selected value type and dropdown expansion
    var selectedValueType by remember { mutableStateOf(availableValueTypes.first()) }
    var expanded by remember { mutableStateOf(false) }

    // State for the different value types
    var stringValue by remember { mutableStateOf("") }
    var booleanValue by remember { mutableStateOf(false) }
    var numberValue by remember { mutableStateOf("") }
    var dateTimeValue by remember { mutableStateOf(Calendar.getInstance()) } // Use Calendar for date/time
    var showTimePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Item") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                TextField(
                    value = tag,
                    onValueChange = { tag = it },
                    label = { Text("Tag") }
                )

                // Dropdown for Value Type
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = selectedValueType,
                        onValueChange = {},
                        label = { Text("Value Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        availableValueTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    selectedValueType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Input field based on selected Value Type
                when (selectedValueType) {
                    "Number" -> {
                        TextField(
                            value = numberValue,
                            onValueChange = { numberValue = it },
                            label = { Text("Value (Number)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    "Date Time" -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTimePicker = true }, // Clickable to show picker
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = SimpleDateFormat("HH:mm").format(dateTimeValue.time),
                                onValueChange = {},
                                label = { Text("Value (Date Time)") },
                                readOnly = true, // Make it read-only
                                modifier = Modifier.weight(1f) // Take remaining space
                            )
                        }
                        if (showTimePicker) {
                            // Use TimePicker (requires adding dependency and possibly DatePickerDialog)
                            // For simplicity, we'll use a basic example here.
                            // A full implementation would involve remembering time/date states separately.
                            TimePickerDialog(
                                onDismissRequest = { showTimePicker = false },
                                initialHour = dateTimeValue.get(Calendar.HOUR_OF_DAY),
                                initialMinute = dateTimeValue.get(Calendar.MINUTE),
                                onTimeSelected = { hour, minute ->
                                    dateTimeValue.set(Calendar.HOUR_OF_DAY, hour)
                                    dateTimeValue.set(Calendar.MINUTE, minute)
                                    showTimePicker = false
                                }
                            )
                        }
                    }
                    "Boolean" -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Value (Boolean):")
                            Spacer(modifier = Modifier.weight(1f)) // Push the switch to the end
                            Switch(
                                checked = booleanValue,
                                onCheckedChange = { booleanValue = it }
                            )
                        }
                    }
                    "String" -> {
                        TextField(
                            value = stringValue,
                            onValueChange = { stringValue = it },
                            label = { Text("Value (String)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Determine the value based on the selected type
                    val itemValue = when (selectedValueType) {
                        "Number" -> numberValue
                        "Date Time" -> SimpleDateFormat("HH:mm").format(dateTimeValue.time)
                        "Boolean" -> booleanValue.toString()
                        "String" -> stringValue
                        else -> ""
                    }

                    val newItem = Item(
                        name = name,
                        tag = tag,
                        value = itemValue,
                        valueType = selectedValueType
                    )
                    onAddItem(newItem)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Basic TimePicker Dialog (Requires implementation details)
// You would typically use platform-specific TimePickerDialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit
) {
    // This is a placeholder. A real TimePickerDialog would be more complex.
    // You might consider using a library or implementing a custom dialog.
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Time") },
        text = {
            Text("TimePicker implementation needed here.")
            // In a real scenario, you would use TimePicker from compose-material3
            // and potentially a DatePickerDialog for the date part.
        },
        confirmButton = {
            Button(onClick = {
                // Simulate selecting a time
                onTimeSelected(initialHour, initialMinute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true) // Annotation to enable preview
@Composable
fun ItemListScreenPreview() {
    // Provide a sample ItemViewModel for the preview
    val sampleItems = listOf(
        Item(id = UUID.randomUUID(), name = "Item 1", tag = "Tag A", value = "Value 1", valueType = "Type X"),
        Item(id = UUID.randomUUID(), name = "Item 2", tag = "Tag B", value = "Value 2", valueType = "Type Y"),
        Item(id = UUID.randomUUID(), name = "Item 3", tag = "Tag A", value = "Value 3", valueType = "Type Z")
    )

    // You can create a simple ViewModel instance for the preview
    // In a real app, you might use a testing ViewModel
    val previewViewModel = ItemViewModel()
    sampleItems.forEach { previewViewModel.addItem(it) } // Add sample data

    ItemListScreen(itemViewModel = previewViewModel)
}
