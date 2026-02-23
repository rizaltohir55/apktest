package com.yourapp.pricetracker.presentation.screen.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.pricetracker.data.local.database.dao.WatchlistDao
import com.yourapp.pricetracker.data.local.database.entity.WatchlistEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistDao: WatchlistDao
) : ViewModel() {

    private val _watchlist = MutableStateFlow<List<WatchlistEntity>>(emptyList())
    val watchlist = _watchlist.asStateFlow()

    init {
        viewModelScope.launch {
            watchlistDao.getAllWatchlist().collectLatest {
                _watchlist.value = it.sortedBy { entity -> entity.orderIndex }
            }
        }
    }

    fun addAsset(symbol: String, name: String) {
        viewModelScope.launch {
            val currentMaxOrder = _watchlist.value.maxOfOrNull { it.orderIndex } ?: -1
            watchlistDao.insertWatchlistItem(
                WatchlistEntity(
                    symbol = symbol.uppercase(),
                    name = name,
                    orderIndex = currentMaxOrder + 1
                )
            )
        }
    }

    fun removeAsset(symbol: String) {
        viewModelScope.launch {
            watchlistDao.deleteWatchlistItem(symbol)
        }
    }
}
