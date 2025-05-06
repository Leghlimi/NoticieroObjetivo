package com.loc.newsapp.presentation.details

import com.loc.newsapp.domain.model.Article

sealed class DetailsEvent {

    //guardar nuestro update o eliminar el artículo.
    data class UpsertDeleteArticle(val article: Article) : DetailsEvent()

    object RemoveSideEffect: DetailsEvent()

}