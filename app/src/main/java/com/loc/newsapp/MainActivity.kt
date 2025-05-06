package com.loc.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.loc.newsapp.data.local.NewsDao
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.model.Source
import com.loc.newsapp.presentation.nvgraph.NavGraph
import com.loc.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint //para cada componente
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepOnScreenCondition{
                viewModel.splashCondition
                //si se obtiene false ocurre installSplashScreen
            }
        }
        setContent {
            NewsAppTheme {
                //un booleano para saber si el sistema está en modo oscuro
                val isSysstemInDarkMode = isSystemInDarkTheme()
                val systemController = rememberSystemUiController()

                //usar el bloque SideEffect. Solo se ejecuta cuando ocurre una recomposición.
                SideEffect {
                    systemController.setSystemBarsColor(
                        //sin color, transparente para las barras de navegación.
                        //También podemos pasarle cualquier color que queramos a las barras de navegación.
                        color = Color.Transparent,
                        darkIcons = !isSysstemInDarkMode //al contrario que en dark
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    val startDestination = viewModel.startDestination
                    NavGraph(startDestination = startDestination)
                }
            }
        }
    }
}