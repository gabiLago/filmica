package es.lagoblasco.filmica.view.watchlist

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.View
import com.squareup.picasso.Picasso
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.Film
import es.lagoblasco.filmica.view.util.BaseFilmAdapter
import es.lagoblasco.filmica.view.util.BaseFilmHolder
import es.lagoblasco.filmica.view.util.SimpleTarget
import kotlinx.android.synthetic.main.item_watchlist.view.*


class WatchlistAdapter(val listener: (Film) -> Unit) :
    BaseFilmAdapter<WatchlistAdapter.WatchlistHolder>(
        R.layout.item_watchlist,
        { view -> WatchlistHolder(view, listener) }
    ) {

    class WatchlistHolder(itemView: View, listener: (Film) -> Unit) : BaseFilmHolder(
        itemView,
        listener
    ) {

        override fun bindFilm(film: Film) {
            super.bindFilm(film)

            with(itemView) {
                labelTitle.text = film.title
                labelVotes.text = film.rating.toString()
                labelOverview.text = film.overview

                loadImage(film)
            }
        }

        private fun loadImage(it: Film) {
            val target = SimpleTarget { bitmap: Bitmap ->
                itemView.imgPoster.setImageBitmap(bitmap)
                setColor(bitmap)
            }

            itemView.imgPoster.tag = target

            Picasso.with(itemView.context)
                .load(it.getPosterUrl())
                .error(R.drawable.placeholder)
                .into(target)
        }

        private fun setColor(bitmap: Bitmap) {
            Palette.from(bitmap).generate {
                val defaultColor =
                    ContextCompat.getColor(itemView.context, R.color.colorPrimary)
                val swatch = it?.vibrantSwatch ?: it?.dominantSwatch
                val color = swatch?.rgb ?: defaultColor

                val overlayColor = Color.argb(
                    (Color.alpha(color) * 0.5).toInt(),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color)
                )

                itemView.imgOverlay.setBackgroundColor(overlayColor)

            }
        }
    }
}
