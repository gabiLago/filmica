package es.lagoblasco.filmica.view.detail

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.Film
import es.lagoblasco.filmica.data.FilmsRepo
import es.lagoblasco.filmica.view.util.SimpleTarget
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {


    var film: Film? = null

    companion object {
        fun newInstance(filId: String): DetailFragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString("id", filId)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id", "")
        film = FilmsRepo.findFilmById(id!!)

        film?.let {
            labelTitle.text = it.title
            labelDescription.text = it.overview
            labelGenres.text = it.genre
            labelDate.text = it.date
            labelRating.text = it.rating.toString()

            loadImage(it)
        }

        buttonAdd.setOnClickListener {
            film?.let {
                FilmsRepo.saveFilm(context!!, it) {
                Toast.makeText(context, "Añadido al watchlist", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun loadImage(film: Film) {
        val target = SimpleTarget {
            imgPoster.setImageBitmap(it)
            setColor(it)
        }

        imgPoster.tag = target

        Picasso.with(context)
            .load(film.getPosterUrl())
            .error(R.drawable.placeholder)
            .into(target)
    }

    private fun setColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate {
            val defaultColor =
                ContextCompat.getColor(context!!, R.color.colorPrimary)
            val swatch = it?.vibrantSwatch ?: it?.dominantSwatch
            val color = swatch?.rgb ?: defaultColor

            val overlayColor = Color.argb(
                (Color.alpha(color) * 0.5).toInt(),
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )

            overlay.setBackgroundColor(overlayColor)
            buttonAdd.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}