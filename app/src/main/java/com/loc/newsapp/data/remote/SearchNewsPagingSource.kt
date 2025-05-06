package com.loc.newsapp.data.remote

//En esta función se realiza la funcionalidad de búsqueda de noticias de la app.
//Se construye la SearchNewsPagingSource, al igual que se hizo en NewsPagingSource.

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.loc.newsapp.domain.model.Article

class SearchNewsPagingSource(
    private val newsApi: NewsApi,
    private val searchQuery: String,
    private val sources: String
): PagingSource<Int, Article>() { //extends de PagingSource

    //Copiar la lógica de ambas funciones de NewsPagingSource, ya que es casi la misma:

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1 //empezar desde la pag. 1.
        return try {
            val newsResponse = newsApi.searchNews(searchQuery = searchQuery ,sources = sources, page = page)

            totalNewsCount += newsResponse.articles.size

            val articles = newsResponse.articles.distinctBy { it.title }

            LoadResult.Page(
                data = articles,
                nextKey = if (totalNewsCount == newsResponse.totalResults) null else page + 1,
                prevKey = null
            )

        } catch (e: Exception) {
            e.stackTrace
            LoadResult.Error(
                throwable = e
            )
        }
    }
}