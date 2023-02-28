package com.example.paginationcompose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paginationcompose.ListItem
import com.example.paginationcompose.Repository
import com.example.paginationcompose.paging.DefaultPaginator
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    //Put it in constructor when use this approach in normal project!
    private val repository = Repository()

    var state by mutableStateOf(ScreenState())
    private val paginator = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = {nextPage ->
            repository.getItems(nextPage, 20)
        },
        getNextKey = {
            state.page + 1
        },
        onError = {
            state = state.copy(error = it?.localizedMessage)
        },
        onSuccess = { items, newKey ->
            state = state.copy(
                items = state.items + items,
                page = newKey,
                isEndReach = items.isEmpty()
            )

        }
    )
init {
    loadNextItems()
}
    fun loadNextItems(){
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}

data class ScreenState(
    val isLoading: Boolean = false ,
    val items: List<ListItem> = emptyList() ,
    val error: String? = null ,
    val isEndReach: Boolean = false ,
    val page: Int = 0
)