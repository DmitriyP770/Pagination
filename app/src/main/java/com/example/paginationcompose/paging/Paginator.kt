package com.example.paginationcompose.paging

interface Paginator<Key, Item> {

   suspend fun loadNextItems()
    fun reset()
}