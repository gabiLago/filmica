package es.lagoblasco.filmica

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id = intent.getStringExtra("id")
        val film = FilmsRepo.findFilmById(id)

        film?.let {
            labelTitle.text = it.title
            labelDescription.text = it.overview
            labelGenres.text = it.genre
            labelDate.text = it.date
        }

        buttonAdd.setOnClickListener {
            Toast.makeText(this@DetailActivity, "Añadido al watchlist", Toast.LENGTH_LONG).show()
        }

    }
}
