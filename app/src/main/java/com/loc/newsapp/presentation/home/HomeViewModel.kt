package com.loc.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
Usar anotación Hilt.
Inyectar Usecases de NewsUsesCases
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
): ViewModel() { //extends de ViewModel
    //Llamamos a news use cases y llamamos a get news.
    //Le pasamos los sources, una lista e identificamos los news sources.
    //Alguna info se obtiene de la documentación de la API.
    //Asegurarse de guardar configuración con catchedIn.
    val news = newsUseCases.getNews(
        sources = listOf("bbc-news", "abc-news", "al-jazeera-english")
    ).cachedIn(viewModelScope)
}