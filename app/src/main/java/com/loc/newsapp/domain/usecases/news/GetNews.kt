package com.loc.newsapp.domain.usecases.news

import androidx.paging.PagingData
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

//Caso de uso para GetNews

class GetNews(
    private val newsRepository: NewsRepository
) {

    //Sources con una lista con string, que devuelve Flow Paging Data que contiene artículos.
    operator fun invoke(sources: List<String>): Flow<PagingData<Article>> {
        return newsRepository.getNews(sources = sources)
    }

}