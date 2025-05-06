package com.loc.newsapp.presentation.search

sealed class SearchEvent {

    data class UpdateSearchQuery(val searchQuery: String): SearchEvent() //extends de SearchEvent

    object SearchNews : SearchEvent()
}