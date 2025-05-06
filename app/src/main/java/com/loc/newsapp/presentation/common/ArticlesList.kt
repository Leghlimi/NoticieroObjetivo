package com.loc.newsapp.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.presentation.Dimens.ExtraSmallPadding2
import com.loc.newsapp.presentation.Dimens.MediumPadding1

/*
Esta función muestra la lista de artículos que se obtienen de Paging 3.
Esta función de lista de artículos composable será usada dentro de la pantalla de búsqueda,
también se usará en múltiples pantallas.
También se gestionan errores.

Un problema que se presenta es que en la función ArticlesList solo recibimos LazyPagingItems<Article>,
pero lo que recibimos en en la base de datos Room solo lista de artículos.
Una solución es copiar lam misma función modificando ciertas cosas, es decir, hacer OVERLOAD.
*/

//Copia a la función de más abajo (Overload)
@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onClick:(Article) -> Unit
){
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2),
        ) {
            items(count = articles.size) {
                val article = articles[it]
                    ArticleCard(article = article, onClick = { onClick(article) })
                }
        }
}

//Función original
@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>,
    onClick:(Article) -> Unit
){
    val handlePagingResult = handlePagingResult(articles = articles)
    if (handlePagingResult) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2),
        ) {
            items(count = articles.itemCount) {
                articles[it]?.let{
                    ArticleCard(article = it, onClick = { onClick(it) })
                }
            }
        }
    }
}

//Usar un boolean, es decir, si es true, es que tenemos los artículos.
@Composable
fun handlePagingResult(articles: LazyPagingItems<Article>): Boolean {
    val loadState = articles.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when {
        loadState.refresh is LoadState.Loading -> {
            ShimmerEffect()
            false
        }

        error != null -> {
            EmptyScreen(error = error)
            false
        }

        else -> {
            true
        }
    }
}


@Composable
private fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1)) {
        repeat(10){
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(horizontal = MediumPadding1)
            )
        }
    }
}