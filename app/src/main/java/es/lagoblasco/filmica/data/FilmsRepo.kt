package es.lagoblasco.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

object FilmsRepo {

    private val films: MutableList<Film> = mutableListOf()

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

    fun findFilmById(id: String): Film? {
        return films.find {
            return@find it.id == id
        }

    }

    fun saveFilm(
        context: Context,
        film: Film
    ) {
        val db = getDbInstance(context)
        db.filmDao().insertFilm(film)
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

    private fun dummyFilms(): MutableList<Film> {
        return (1..10).map { i: Int ->
            return@map Film(
                id = "${i}",
                title = "Film ${i}",
                overview = "Overview ${i}",
                genre = "Genre ${i}",
                rating = i.toFloat(),
                date = "2019-05-${i}"
            )
        }.toMutableList()
    }
}