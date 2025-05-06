package com.loc.newsapp.presentation.news_navigator.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.loc.newsapp.R
import com.loc.newsapp.presentation.Dimens.ExtraSmallPadding2
import com.loc.newsapp.presentation.Dimens.IconSize
import com.loc.newsapp.ui.theme.NewsAppTheme


@Composable
fun NewsBottomNavigation(
    items: List<BottomNavigationItem>, //pasar la lista de los items.
    selected: Int, //si ha sido seleccionado
    onItemClick:(Int) -> Unit //enviar el index que ha sido clickado.
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 10.dp
    ) {
        items.forEachIndexed { index, item -> //loop de todos los items. Indexed porque vamos a usar el item.
            NavigationBarItem(
                selected = index == selected,
                onClick = { onItemClick(index) },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(IconSize)
                        )
                        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
                        Text(text = item.text, style = MaterialTheme.typography.labelSmall)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = colorResource(id = R.color.body),
                    unselectedTextColor = colorResource(id = R.color.body),
                    indicatorColor = MaterialTheme.colorScheme.background

                )
            )
        }
    }
}

data class BottomNavigationItem(
    @DrawableRes val icon: Int,
    val text: String
)

/*
Debería aparecer el dark mode con la configuración de abajo, pero no se
muestra correctamente el modo oscuro en la preview.
De algún modo NewsAppTheme no respeta el uiMode de la preview, por lo que se ha recurrido a forzarlo.
*/
@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NewsBottomNavigationPreview() {
    val configuration = LocalConfiguration.current
    val isDarkTheme = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    NewsAppTheme(darkTheme = isDarkTheme, dynamicColor = false) {
        NewsBottomNavigation(
            items = listOf(
                BottomNavigationItem(icon = R.drawable.ic_home, text = "Menú"),
                BottomNavigationItem(icon = R.drawable.ic_search, text = "Buscar"),
                BottomNavigationItem(icon = R.drawable.ic_bookmark, text = "Marcadores")
            ),
            selected = 0,
            onItemClick = {}
        )
    }
}

/*
//No muestra correctamente la preview del modo oscuro.
@Preview(name = "light mode")
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "dark mode")
@Composable
fun NewsBottomNavigationPreview() {
    NewsAppTheme {
        NewsBottomNavigation(
            items = listOf(
                BottomNavigationItem(icon = R.drawable.ic_home, text = "Home"),
                BottomNavigationItem(icon = R.drawable.ic_search, text = "Search"),
                BottomNavigationItem(icon = R.drawable.ic_bookmark, text = "Bookmark")),
                selected = 0,
                onItemClick = {}
        )
    }
}
*/
