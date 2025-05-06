package com.loc.newsapp.presentation.details.components



import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.loc.newsapp.R
import com.loc.newsapp.ui.theme.NewsAppTheme

/*
Vamos a crear una nueva función componible llamada DetailsTopBar,
la cual tendrá el botón de navegación y tres opciones:
opción Bookmark (marcador), opción Share (compartir) y opción Browsing (Búsqueda).
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
    onBrowsingClick:() -> Unit,
    onShareClick:() -> Unit,
    onBookmarkClick:() -> Unit,
    onBackClick:() -> Unit
) {

    TopAppBar(
        title = { },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = colorResource(id = R.color.body),
            navigationIconContentColor = colorResource(id = R.color.body),
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = null
                )

            }
        },
        //las tres opciones de click.
        actions = {
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = null
                )

            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )

            }
            IconButton(onClick = onBrowsingClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_network),
                    contentDescription = null
                )

            }
        }
    )

}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO,name = "Light mode")
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Dark mode")
@Composable
fun DetailsTopBarPreview() {
    NewsAppTheme (dynamicColor = false) {
        DetailsTopBar(
            onShareClick = { /*TODO*/ },
            onBookmarkClick = { /*TODO*/ },
            onBrowsingClick = {}) {

        }
    }
}



/*
//Preview no detecta correctamente la preview en oscuro;
//De esta manera forzamos explícitamente el darkTheme y la preview aplicará correctamente los colores.
@Preview //(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "dark mode")
@Composable
fun DetailsTopBarPreviewDark() {
    NewsAppTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            DetailsTopBar(
                onShareClick = { /*TODO*/ },
                onBookmarkClick = { /*TODO*/ },
                onBrowsingClick = {}
            ) { }
        }
    }
}
 */

