package com.loc.newsapp.domain.repository

import androidx.paging.PagingData
import com.loc.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface NewsRepository {
    /*
    función que obtiene las noticias, pasamos el origen de las noticias.
    Devuelve los artículos en PagingData (es una clase envoltura para los datos que vamos a implementar).
    Es una técnica que permite la búsqueda de los datos en pequeños trozos del servidor.
    Como habrá miles y miles de artículos y si queremos la búsqueda de todos ellos juntos
    tomará mucho tiempo y la solución aplicada es Paging, es decir,
    epecificar cuantos articulos se quieren cargar del API y entonces el API responderá.
    Esto ayudará a obtener repuestas mas rapidas.
    */

    fun getNews(sources: List<String>): Flow<PagingData<Article>>

    fun searchNews(searchQuery: String ,sources: List<String>): Flow<PagingData<Article>>

    suspend fun upsertArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun selectArticles(): Flow<List<Article>>

    suspend fun selectArticle(url: String): Article?

}