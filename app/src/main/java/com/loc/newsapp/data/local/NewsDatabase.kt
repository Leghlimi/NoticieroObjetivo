package com.loc.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loc.newsapp.domain.model.Article



@Database(entities = [Article::class], version = 1) //no pasar version 0 ya que da error.
@TypeConverters(NewsTypeConvertor::class)
abstract class NewsDatabase: RoomDatabase() { //Extend de RoomDatabase.

    abstract val newsDao: NewsDao //Añadir el Dao a esta clase. Room implementará estas clases abstractas por nosotros.

}