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

    private val films: MutableList<Film> = mutableListOf()
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

        if (fromFragment == "films") {
            currentFilmsList = films
        } else if (fromFragment == "trending") {
            currentFilmsList = trendingFilms
        } else if (fromFragment == "search") {
            currentFilmsList = searchResults
        } else {
            currentFilmsList = filmsOnDatabase
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

    fun discoverFilms(
        context: Context,
        onResponse: (List<Film>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = ApiRoutes.discoverMoviesUrl()
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val films = Film.parseFilms(response.getJSONArray("results"))
                FilmsRepo.films.clear()
                FilmsRepo.films.addAll(films)
                onResponse.invoke(FilmsRepo.films)
            },
            { error ->
                error.printStackTrace()
                onError.invoke(error)
            }
        )

        Volley.newRequestQueue(context)
            .add(request)
    }

    fun trendingFilms(
        context: Context,
        onResponse: (List<Film>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {

        val url = ApiRoutes.trendingFilmsUrl()
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val trendingFilms = Film.parseFilms(response.getJSONArray("results"))
                FilmsRepo.trendingFilms.clear()
                FilmsRepo.trendingFilms.addAll(trendingFilms)
                onResponse.invoke(FilmsRepo.trendingFilms)
            },
            { error ->
                error.printStackTrace()
                onError.invoke(error)
            }
        )

        Volley.newRequestQueue(context)
            .add(request)
    }

    fun searchFilms(
        context: Context,
        onResponse: (List<Film>) -> Unit,
        onError: (VolleyError) -> Unit,
        query: String
    ) {

        val url = ApiRoutes.searchFilmsUrl(query)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val searchResultsFilms = Film.parseFilms(response.getJSONArray("results"))
                FilmsRepo.searchResults.clear()
                FilmsRepo.searchResults.addAll(searchResultsFilms)
                onResponse.invoke(FilmsRepo.searchResults)
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