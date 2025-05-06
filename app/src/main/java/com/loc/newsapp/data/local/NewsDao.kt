package com.loc.newsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.loc.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

/*
*Implementar la clase DAO (Data Access Object), que ya trabajamos mucho el curso anterior.
*Es la clase o interface (en nuestro caso) responsable de acceder a nuestra base de datos y
* manipular los datos como insertar, actualizar o eliminar y más operaciones.
*/

/* RECORDATORIO
* Las funciones marcadas con suspend en Kotlin se utilizan para realizar operaciones asincrónicas sin bloquear el hilo principal,
* especialmente útil en programación con corutinas.
* Es una función que puede suspender su ejecución y reanudarla más tarde sin bloquear el hilo actual.
* Solo puede ser llamada desde otra función suspendida o desde una corutina.
* Se usan para llamadas a red (API, retrofit), acceso a datos ROOM como es nuestro caso, lectura de archivos, etc.
 */

@Dao
interface NewsDao {

    //onConflict: básicamente si estamos intentando insertar el mismo artículo, actualizamos el artículo existente (REPLACE).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * FROM Article")
    fun getArticles(): Flow<List<Article>>

    @Query("SELECT * FROM Article WHERE url =:url") //seleccionar all donde el url sea igual al url pasado.
    suspend fun getArticle(url: String): Article? //hay que crearle un caso de uso

}