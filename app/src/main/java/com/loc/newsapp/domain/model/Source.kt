package com.loc.newsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val id: String,
    val name: String
): Parcelable //extender de Parcelable, implementar la interfaz Parcelable.