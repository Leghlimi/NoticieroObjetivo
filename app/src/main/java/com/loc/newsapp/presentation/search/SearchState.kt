package com.loc.newsapp.presentation.search

import androidx.paging.PagingData
import com.loc.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query


data class SearchState(
    val searchQuery: String = "", //por defecto es un string vacío.
    val articles: Flow<PagingData<Article>>? = null //aquí los resultados que se obtienen de Paging 3.
) {
}