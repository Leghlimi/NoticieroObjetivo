package com.loc.newsapp.presentation.bookmark

import com.loc.newsapp.domain.model.Article

//Aquí solo habrá artículos, que será una lista de artículos (y lista vacía).
data class BookmarkState(
    val articles: List<Article> = emptyList()
)
