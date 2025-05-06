package com.loc.newsapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.loc.newsapp.domain.model.Article


//Para implementar Paging Library, se crea un remote paging source.

class NewsPagingSource(
    private val newsApi: NewsApi,
    private val sources: String

    /*
    Extender de PagingSource (clase abstracta).
    Se pasan dos valores, la key que será un integer (la key representa la page),
    y un value, que recibe un artículo de la API.
    Se pasa el constructor y se implementan dos funciones.
    */
) : PagingSource<Int, Article>() {

    //Aquí realizaremos la petición a la API, y devolverá el artículo.
    //Con lo parámetros obtener Page. Devolver un try catch.
    //Los datos retornados en este caso son LoadResult.
    //El responde se crea dentro del try.

    //También hay que determinar cuando parar el paging. Se usar el totalNewsCount.
    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        //request al servidor.
        val page = params.key ?: 1 //empezar desde la pag. 1.
        return try {
            // Añadir API_KEY en la función getNews así no es necesario pasarla aquí.
            val newsResponse = newsApi.getNews(sources = sources, page = page)
            //obtener el número de artículos e incremenetar.
            totalNewsCount += newsResponse.articles.size

            //distinct by: para evitar que devuelva los mismos articulos.
            //Eliminar cualquier articulo con el mismo titulo (duplicados).
            val articles = newsResponse.articles.distinctBy { it.title } //obtener los artículos del response
            LoadResult.Page(
                data = articles,
                nextKey = if (totalNewsCount == newsResponse.totalResults) null else page +1,
                prevKey = null
            )
        }catch (e:Exception){
            e.stackTrace
            LoadResult.Error(
                throwable = e
            )
        }
    }

    //En esta función obtenemos este state, que contiene información sobre el estado de nuestro paging.
    //EL anchorPosition es el ultimo page en la lista, chequear que no es null.
    //Obtener la posición más cercana a este último con closest.
    //Una vez obtenemos el page actual usar previousKey y nextKey.
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}