package es.lagoblasco.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object FilmsRepo {

    private val discoverFilms: MutableList<Film> = mutableListOf()
    private val trendingFilms: MutableList<Film> = mutableListOf()
    private val searchResults: MutableList<Film> = mutableListOf()
    private val filmsOnDatabase: MutableList<Film> = mutableListOf()

    private var db: FilmDatabase? = null

    private fun getDbInstance(context: Context): FilmDatabase {
        if (db == null) {
            db = Room.databaseBuilder(
                context,
                FilmDatabase::class.java,
                "filmica-db"
            ).build()
        }

        return db as FilmDatabase
    }

    fun findFilmById(id: String, fromFragment: String): Film? {
        var currentFilmsList: MutableList<Film> = mutableListOf()

        when (fromFragment) {
            "films" -> currentFilmsList = discoverFilms
            "trending" -> currentFilmsList = trendingFilms
            "search" -> currentFilmsList = searchResults
            else -> currentFilmsList = filmsOnDatabase
        }


        return currentFilmsList.find {
            return@find it.id == id
        }

    }

    fun saveFilm(
        context: Context,
        film: Film,
        callback: (Film) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().insertFilm(film)
            }
            async.await()
            callback.invoke(film)
        }
    }

    fun getFilms(context: Context, callback: (List<Film>) -> Unit) {
        // Refactored to store and retrieve watchlist items in DB

        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().getFilms()
            }
            val films = async.await()

            //CLean an add to db
            filmsOnDatabase.clear()
            filmsOnDatabase.addAll(films)

            //Compose the mutable list, thus it can called from Detail by its id
            callback.invoke(FilmsRepo.filmsOnDatabase)
        }
    }


    fun deleteFilm(
        context: Context,
        film: Film,
        callback: (Film) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().deleteFilm(film)
            }

            async.await()
            callback.invoke(film)
        }
    }

    fun getParsedFilms(
        context: Context,
        forSection: String,
        onResponse: (List<Film>) -> Unit,
        onError: (VolleyError) -> Unit,
        query: String = ""
    ) {

        var listToFill: MutableList<Film> = mutableListOf()
        var url : String = ""

        when (forSection) {
            "discoverFilms" -> {
                listToFill = discoverFilms
                url = ApiRoutes.discoverMoviesUrl()
            }

            "trendingFilms" -> {
                listToFill = trendingFilms
                url = ApiRoutes.trendingFilmsUrl()
            }

            "Search" -> {
                listToFill = filmsOnDatabase
                url = ApiRoutes.searchFilmsUrl(query)
            }
        }


        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val films = Film.parseFilms(response.getJSONArray("results"))
                listToFill.clear()
                listToFill.addAll(films)
                onResponse.invoke(listToFill)
            },
            { error ->
                error.printStackTrace()
                onError.invoke(error)
            }
        )

        Volley.newRequestQueue(context)
            .add(request)
    }
}