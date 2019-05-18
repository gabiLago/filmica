package es.lagoblasco.filmica.view.films

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.Film
import kotlinx.android.synthetic.main.item_film.view.*

class FilmsAdapter(val listener: (Film) -> Unit) :
    RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    private val films = mutableListOf<Film>()

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onCreateViewHolder(recyclerView: ViewGroup, type: Int): FilmViewHolder {
        val view = LayoutInflater.from(recyclerView.context).inflate(
            R.layout.item_film,
            recyclerView,
            false)

        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films.get(position)
        holder.film = film
    }

    fun setFilms(list: List<Film>) {
        this.films.clear()
        this.films.addAll(list)
        notifyDataSetChanged()
    }

    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var film: Film? = null
            set(value) {
                field = value

                with(itemView) {
                    field?.let {
                        labelTitle.text = it.title
                        labelGenre.text = it.genre
                        labelRating.text = it.rating.toString()

                        Picasso.with(itemView.context)
                            .load(it.getPosterUrl())
                            .into(imgPoster)
                    }
                }
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