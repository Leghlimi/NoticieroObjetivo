package com.loc.newsapp.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/*
*Como sabemos, aquí tenemos objetos, y en bases de datos solo se pueden guardar
* datos primitivos, tipo Strings, Integers, etc.
* Por lo tanto, vamos a convertir estos objetos en datos primitivos.
* Se puede hacer con un type converter en Room. (clase NewsTypeConvertor)
*/

@Parcelize
@Entity
data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    @PrimaryKey val url: String,
    val urlToImage: String
): Parcelable //extender de Parcelable, implementar la interfaz Parcelable.