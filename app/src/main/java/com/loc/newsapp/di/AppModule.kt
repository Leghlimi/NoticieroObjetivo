package com.loc.newsapp.di

import android.app.Application
import androidx.room.Room
import com.loc.newsapp.data.local.NewsDao
import com.loc.newsapp.data.local.NewsDatabase
import com.loc.newsapp.data.local.NewsTypeConvertor
import com.loc.newsapp.data.manager.LocalUserManagerImp
import com.loc.newsapp.data.remote.NewsApi
import com.loc.newsapp.data.repository.NewsRepositoryImpl
import com.loc.newsapp.domain.manager.LocalUserManager
import com.loc.newsapp.domain.repository.NewsRepository
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import com.loc.newsapp.domain.usecases.app_entry.AppEntryUseCases
import com.loc.newsapp.domain.usecases.app_entry.ReadAppEntry
import com.loc.newsapp.domain.usecases.app_entry.SaveAppEntry
import com.loc.newsapp.domain.usecases.news.DeleteArticle
import com.loc.newsapp.domain.usecases.news.GetNews
import com.loc.newsapp.domain.usecases.news.SearchNews
import com.loc.newsapp.domain.usecases.news.SelectArticle
import com.loc.newsapp.domain.usecases.news.SelectArticles
import com.loc.newsapp.domain.usecases.news.UpsertArticle
import com.loc.newsapp.util.Constants.BASE_URL
import com.loc.newsapp.util.Constants.NEWS_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUSerManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImp(application)




    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )


    //Añadir un gson converter para convertir la respuesta json que recibimos de la API
    //a un objeto Kotlin
    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() //llamamos a build
            .create(NewsApi::class.java) //llamamos a create
    }

    //Obtener una instancia de este repositorio y pasarlo a nuestro use case.
    //Usamos la interfaz abstracta.
    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi, //instanciar
        newsDao: NewsDao
    ): NewsRepository = NewsRepositoryImpl(newsApi, newsDao)

    @Provides
    @Singleton
    fun provideNewsUseCases(
        newsRepository: NewsRepository,
        newsDao: NewsDao
    ): NewsUseCases {
        return NewsUseCases(
            getNews = GetNews(newsRepository),
            searchNews = SearchNews(newsRepository),
            upsertArticle = UpsertArticle(newsRepository),
            deleteArticle = DeleteArticle(newsRepository),
            selectArticles = SelectArticles(newsRepository),
            selectArticle = SelectArticle(newsRepository)
        )
    }


    @Provides
    @Singleton
    fun provideNewsDatabase(
        application: Application //la  aplicación que usará Room database.
    ): NewsDatabase { //construir la base de datos Room.
        return Room.databaseBuilder(
            context = application,
            klass = NewsDatabase::class.java,
            name = NEWS_DATABASE_NAME
        ).addTypeConverter(NewsTypeConvertor())
            .fallbackToDestructiveMigration() //Esta función se usa para migración. Si actualizas algo en la BBDD room migrará la base de datos por nosotros.
            .build() //La base de datos está lista para usarse
    }

    //Ahora falta "proveer" el objeto Dao
    //Uso del importantísimo Dagger Hilt.
    @Provides
    @Singleton
    fun provideNewsDao(
        newsDatabase: NewsDatabase //obtener la instancia de nuestra BBDD.
        // Una vez hecho esto, Room lo inyectará en esta función por nosotros (Dagger Hilt).
    ): NewsDao = newsDatabase.newsDao //lo que devuelve.


}