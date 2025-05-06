package com.loc.newsapp.presentation.bookmark

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class BookmarkViewModel @Inject constructor( //inyectar los casos de uso (newsUseCases)
    private val newsUseCases: NewsUseCases
): ViewModel() {

    //crear el state
    private val _state = mutableStateOf(BookmarkState())
    val state: State<BookmarkState> = _state //versión pública (expuesta).

    init {
        getArticles()
    }

    //asReversed para que los guardados en Marcadores salgan de recientes a más antiguos.
    private fun getArticles() {
        newsUseCases.selectArticles().onEach {
            _state.value = _state.value.copy(articles = it.asReversed())
        }.launchIn(viewModelScope)
    }

}