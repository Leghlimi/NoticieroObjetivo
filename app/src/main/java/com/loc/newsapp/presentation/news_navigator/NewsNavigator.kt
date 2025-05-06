package com.loc.newsapp.presentation.news_navigator

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.newsapp.R
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.presentation.bookmark.BookmarkScreen
import com.loc.newsapp.presentation.bookmark.BookmarkViewModel
import com.loc.newsapp.presentation.details.DetailsEvent
import com.loc.newsapp.presentation.details.DetailsScreen
import com.loc.newsapp.presentation.details.DetailsViewModel
import com.loc.newsapp.presentation.home.HomeScreen
import com.loc.newsapp.presentation.home.HomeViewModel
import com.loc.newsapp.presentation.news_navigator.components.BottomNavigationItem
import com.loc.newsapp.presentation.news_navigator.components.NewsBottomNavigation
import com.loc.newsapp.presentation.nvgraph.Route
import com.loc.newsapp.presentation.search.SearchScreen
import com.loc.newsapp.presentation.search.SearchViewModel

/*
* En esta app tendremos dos NavGraph, uno para el onBoarding y SplashScreen,
* y otro para el NewsNavigator y el Botton Navigation Screen.
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsNavigator() {

    val bottomNavigationItem = remember {
        listOf(
            BottomNavigationItem(icon = R.drawable.ic_home, text = "Menú"),
            BottomNavigationItem(icon = R.drawable.ic_search, text = "Buscar"),
            BottomNavigationItem(icon = R.drawable.ic_bookmark, text = "Marcadores")
        )
    }

    //Ahora podemos tener nuestro Scaffold,  es uno de los componentes de Material Theme,
    //divide la pantalla en partes, tipo el Bottom navigation bar, y el navigation rail, por lo que es muy útil.
    val navController = rememberNavController()
    val backstackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }

    selectedItem = remember(key1 = backstackState) {
        when(backstackState?.destination?.route) {
            Route.HomeScreen.route -> 0
            Route.SearchScreen.route -> 1
            Route.BookmarkScreen.route -> 2
            else -> 0 //esta opción no es posible pero la incluimos por si las moscas.
        }
    }

    //Para que la barra sea o no visible.
    //Al clicar sobre una noticia que desaparecezca la barra.
    val isBottomBarVisible = remember(key1 = backstackState) {
        backstackState?.destination?.route == Route.HomeScreen.route ||
                backstackState?.destination?.route == Route.SearchScreen.route ||
                backstackState?.destination?.route == Route.BookmarkScreen.route
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                NewsBottomNavigation(
                    items = bottomNavigationItem,
                    selected = selectedItem,
                    onItemClick = { index ->
                        when (index) {
                            0 -> navigateToTab(
                                navController = navController,
                                route = Route.HomeScreen.route
                            )

                            1 -> navigateToTab(
                                navController = navController,
                                route = Route.SearchScreen.route
                            )

                            2 -> navigateToTab(
                                navController = navController,
                                route = Route.BookmarkScreen.route
                            )

                        }
                    }
                )
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding) //el bottom padding será el padding de NewsBottomNavigation de arriba.
        ) {
            //Función composable HomeScreen
            composable(route = Route.HomeScreen.route){
                val viewModel: HomeViewModel = hiltViewModel()
                val articles = viewModel.news.collectAsLazyPagingItems()
                //hay dos opciones en HomeScreen: la primera es navegar hasta la Search Screen, clicando en el Search Bar
                //y la segunda opción es navegar hasta la Details Screen.
                //Para discriminar entre estas opciones, en HomeScreen.kt en la función HomeScreen,
                //añadiremos dos opciones; navigateToSearch: () -> Unit, navigatetoDetails: (Article) -> Unit.
                HomeScreen(
                    articles = articles,
                    navigateToSearch = {
                        navigateToTab(navController = navController, route = Route.SearchScreen.route)
                    },
                    navigatetoDetails = { article ->
                        navigateToDetails(
                            navController = navController,
                            article = article
                        )
                    }
                )
            }
            //Función composable SearcScreen
            composable(route = Route.SearchScreen.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                val state = viewModel.state.value
                SearchScreen(
                    state = state,
                    event = viewModel::onEvent,
                    navigateToDetails = { article ->
                        navigateToDetails(
                            navController = navController,
                            article = article
                        )
                    }
                )
            }
            //Función composable DetailsScreen
            composable(route = Route.DetailsScreen.route) {
                val viewModel: DetailsViewModel = hiltViewModel()
                if (viewModel.sideEffect != null) {
                    Toast.makeText(LocalContext.current, viewModel.sideEffect, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.onEvent(DetailsEvent.RemoveSideEffect)
                }

                //Como podemos obtener el objeto article, lo pasamos en currentBackStack, pero ahora estamos en otro BackStackEntry,
                //la manera de obtener dicho objeto article es la siguiente:
                navController.previousBackStackEntry?.savedStateHandle?.get<Article?>("article")
                    ?.let { article ->
                        DetailsScreen(
                            article = article,
                            event = viewModel::onEvent,
                            navigateUp = { navController.navigateUp() }
                        )
                }
            }
            //Función composable BookmarkScreen
            composable(route = Route.BookmarkScreen.route) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                val state = viewModel.state.value
                BookmarkScreen(state = state, navigateToDetails = { article ->
                    navigateToDetails(navController = navController, article = article)
                } )
            }

        }

    }

}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen){
                saveState = true //Esto es muy IMPORTANTE
            }
            restoreState = true
            launchSingleTop = true //si estas en el home screen y clica múltiples veces esta no creará nuevas instancias de la screen,
                                   //lanzará la misma screen, esto es muy importante en nuestro caso.

        }
    }
}

//Crear una función que nos ayudará a navegar Details.
//Es un poco diferente porque cuando vamos hasta Details Screen queremos obtener el objeto article con nosotros.
//Lo haremos con BackStackEntry.
private fun navigateToDetails(navController: NavController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
    navController.navigate(
        route = Route.DetailsScreen.route
    )
}