package com.example.devicecontrolinterface.data

import java.util.UUID

// Define the possible value types
val availableValueTypes = listOf("Number", "Date Time", "Boolean", "String")

data class Item(
    val id: UUID = UUID.randomUUID(), // Unique identifier for each item
    var name: String,
    var tag: String,
    var value: String,
    var valueType: String,
    var isAvailable: Boolean = true
)