package com.loc.newsapp.presentation.details

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import com.loc.newsapp.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.model.Source
import com.loc.newsapp.presentation.Dimens.ArticleImageHeight
import com.loc.newsapp.presentation.Dimens.MediumPadding1
import com.loc.newsapp.presentation.details.components.DetailsTopBar
import com.loc.newsapp.ui.theme.NewsAppTheme

@Composable
fun DetailsScreen(
    article : Article,
    event: (DetailsEvent) -> Unit,
    navigateUp: () -> Unit //en este caso tendremos un nuevo parámetro (navigateUp).
) {

    //necesitaremos el contexto.
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        //llama a DetailsTopBar
        DetailsTopBar(
            /*
            en Browsing se crea un Intent, y lo lanzarlo. Lo que se busca con Intent es ver contenido.
            Un Intent es un objeto que se usa para iniciar actividades, servicios o enviar datos entre componentes de una app.
            Añadir una comprobación con resolveActivity() para asegurarse de que hay alguna aplicación capaz de manejar el Intent,
            que es una práctica recomendada.
            */
            /* EXPLICACION queries en AndroidManifest
            Se ha tenido que añadir esa sección <queries> en AndroidManifest.xml porque a partir de Android 11 (API 30),
            las apps deben declarar explícitamente las intenciones (intents) que quieren usar para interactuar con otras apps
            (como navegadores o apps de compartir), si no están dirigidas a un paquete concreto.
            Esto se hace por razones de privacidad y seguridad: evita que una app pueda consultar qué otras apps tienes instaladas sin permiso.
            */
            onBrowsingClick = {
                Intent(Intent.ACTION_VIEW).also { //Crea un intent para abrir una URL.
                    it.data = article.url.toUri() //Le asigna la URL del artículo.
                    //it.data = Uri.parse(article.url) //otra manera de hacerlo, aunque la expresión anterior es la recomendada por Kotlin.
                    if (it.resolveActivity(context.packageManager) != null) { //Verifica que haya una app instalada que pueda manejar este intent.
                        context.startActivity(it) //Lanza el intent (abre el navegador u otra app que maneje la URL).
                        //resolveActivity "chirría", por lo que se añaden ciertas queries en AndroidManifest (EXPLICACION ARRIBA)
                    }
                }
            },
            onShareClick = { //lógica similar
                Intent(Intent.ACTION_SEND).also {
                    it.putExtra(Intent.EXTRA_TEXT, article.url)
                    it.type = "text/plain"
                    if (it.resolveActivity(context.packageManager) != null) {
                        context.startActivity(it)
                    }
                }
            },
            onBookmarkClick = { event(DetailsEvent.UpsertDeleteArticle(article)) },
            onBackClick = navigateUp
        )
        //poder hacer scroll
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = MediumPadding1,
                end = MediumPadding1,
                top = MediumPadding1
            )
        ) {
            item {

                AsyncImage(
                    model = ImageRequest.Builder(context = context).data(article.urlToImage)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ArticleImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = article.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = colorResource(
                        id = R.color.text_title
                    )
                )

                Text(
                    text = article.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(
                        id = R.color.body
                    )
                )

            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    NewsAppTheme {
        DetailsScreen(
            article = Article(
                author = "",
                title = "El Supremo busca 500.000 euros que Transportes pagó a Ábalos en rentas y dietas y no constan en cuentas del exministro",
                description = "Da 15 días al Ministerio para que aporte información sobre los pagos exentos que representan el 69% de sus ingresos",
                content = "Usamos cookies y datos para Entregar y mantener los servicios de Google. Realizar un seguimiento de las interrupciones y proteger contra el spam, el fraude y el abuso. Medir la participación de la audiencia y las estadísticas del sitio para comprender… [+1131 chars]",
                publishedAt = "Actualizado: miércoles, 30 abril 2025 21:00",
                source = Source(
                    id = "", name = "EuropaPress"
                ),
                url = "https://www.europapress.es/nacional/noticia-juez-requiere-transportes-informacion-pago-mas-500000-euros-abalos-rentas-dietas-exentas-20250430100548.html",
                urlToImage = "https://phantom-elmundo.uecdn.es/930db41202094b855bf9f2b411438e69/crop/12x136/820x674/resize/646/f/webp/assets/multimedia/imagenes/2025/04/15/17447112633176.jpg"
            ),
            event = {}
        ) {
        }
    }
}