package com.loc.newsapp.presentation.onboarding

import android.media.Image
import androidx.annotation.DrawableRes
import com.loc.newsapp.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = "La revolución de las criptomonedas",
        description = "Las criptomonedas se han convertido en un elemento clave que ha revolucionado el sistema financiero global con su énfasis en la descentralización, activos digitales y tecnología blockchain.",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Viajar",
        description = "El secreto de la felicidad está en viajar, además alarga la vida. Te lo contamos todo",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "La comida ya no es lo que era",
        description = "¿Te parece que la textura y sabor de los tomates no son como cuando eras pequeño? ¿Te sabe igual el pan que cuando eras un niño? El sabor ha cambiado y hay varias razones.",
        image = R.drawable.onboarding3
    )


)
