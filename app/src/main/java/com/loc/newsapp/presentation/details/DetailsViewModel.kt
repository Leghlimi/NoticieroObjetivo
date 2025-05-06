package com.loc.newsapp.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


//Añadir HiltViewModel, extends de ViewModel, e inyectar los casos de uso.
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
): ViewModel() {

    var sideEffect by mutableStateOf<String?>(null)
        private set

    /*
    * Necesitamos determinar si debemos eliminar o insertar porque el mismo botón o icono hará el mimso trabajo.
    * Queremos obtener el artículo, si es null ya sabemos que debería insertar el artículo,
    * y si no es null debería eliminarlo.
    */
    fun onEvent(event: DetailsEvent) {
        when(event){
            is DetailsEvent.UpsertDeleteArticle -> {
                viewModelScope.launch { //dentro de un viewModelScope porque es una función "suspend".
                    val article = newsUseCases.selectArticle(event.article.url)
                    if (article == null){
                        upsertArticle(event.article)
                    }else{
                        deleteArticle(event.article)
                    }
                }
            }

            is DetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private suspend fun upsertArticle(article: Article) {
        newsUseCases.upsertArticle(article = article)
        sideEffect = "Artículo guardado" //Informar al UI de que hemos insertado el artículo.
    }

    private suspend fun deleteArticle(article: Article) {
        newsUseCases.deleteArticle(article = article)
        sideEffect = "Artículo eliminado"
    }

}