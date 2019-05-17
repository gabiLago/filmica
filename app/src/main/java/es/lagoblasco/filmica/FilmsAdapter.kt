package es.lagoblasco.filmica

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.concurrent.RecursiveAction

class FilmsAdapter(val listener: (Film) -> Unit) :
    RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {


    // Instancia de los datos que vienen del servidor
    //val films: MutableList<Film> = mutableListOf()
    // Por inferencia, hay syntactic sugar, se puedo escribir así:
    private val films = mutableListOf<Film>()


    override fun getItemCount(): Int {
        return films.size
    }

    override fun onCreateViewHolder(recyclerView: ViewGroup, type: Int): FilmViewHolder {
        val view = LayoutInflater.from(recyclerView.context).inflate(R.layout.item_film,
            recyclerView, false)

        return FilmViewHolder(view)
    }

    fun setFilms(list: MutableList<Film>) {
        this.films.clear()
        this.films.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int ) {
        val film = films.get(position)
        holder.film = film
    }

    // nested class => inner class
    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var film: Film? = null
        set(value) {
            field = value
            //Data casting:
            (itemView as TextView).text = value?.title
        }

        init {
            itemView.setOnClickListener {
                film?.let {
                    listener.invoke(it)
                }
            }
        }
    }
}