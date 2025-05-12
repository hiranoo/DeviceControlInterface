package com.example.devicecontrolinterface.ui.viewmodel

import kotlin.collections.indexOfFirst
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.devicecontrolinterface.data.Item

class ItemViewModel : ViewModel() {

    // Live data for the list of items
    private val _items = mutableStateListOf<Item>()
    val items: List<Item>
        get() = _items

    // State for the currently selected item (for detail view/editing)
    var selectedItem by mutableStateOf<Item?>(null)
        private set

    // CRUD operations
    fun addItem(item: Item) {
        _items.add(item)
    }

    fun updateItem(updatedItem: Item) {
        val index = _items.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            _items[index] = updatedItem
        }
    }

    fun deleteItem(item: Item) {
        _items.remove(item)
    }

    fun selectItem(item: Item?) {
        selectedItem = item
    }

    fun updateItemAvailability(item: Item, isAvailable: Boolean) {
        val index = _items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val updatedItem = _items[index].copy(isAvailable = isAvailable)
            _items[index] = updatedItem
        }
    }
}