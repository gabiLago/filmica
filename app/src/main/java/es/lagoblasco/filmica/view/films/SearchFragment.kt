package es.lagoblasco.filmica.view.films

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.Film
import es.lagoblasco.filmica.data.FilmsRepo
import es.lagoblasco.filmica.view.util.GridOffsetDecoration
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_error.*
import java.lang.IllegalArgumentException


class SearchFragment : Fragment() {



    lateinit var listener: OnFilmClickListener


    val list: RecyclerView by lazy {
        listFilms.addItemDecoration(GridOffsetDecoration())
        return@lazy listFilms
    }

    val adapter = FilmsAdapter {
        listener.onClick(it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFilmClickListener) {
            listener = context

        } else {
            throw IllegalArgumentException("The attached activity isn't implementing ${OnFilmClickListener::class.java.canonicalName}")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view = inflater.inflate(R.layout.fragment_search, container, false)
         return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = adapter

        buttonRetry.setOnClickListener {
            reload()

        }



        moviesSearchBox.setHint("Probando desde onViewCreated")




    }

    override fun onResume() {
        super.onResume()

        reload()


    }

    private fun reload() {
        showProgress()

        FilmsRepo.trendingFilms(context!!,
            { films ->
                adapter.setFilms(films)
                showList()

            }, { errorRequest ->
                showError()
            })

    }


    private fun showList() {
        filmsProgress.visibility = View.INVISIBLE
        error.visibility = View.INVISIBLE
        list.visibility = View.VISIBLE
    }

    private fun showError() {
        filmsProgress.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
        error.visibility = View.VISIBLE
    }

    private fun showProgress() {
        filmsProgress.visibility = View.VISIBLE
        error.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
    }


    interface OnFilmClickListener {
        fun onClick(film: Film)
    }

    private fun searchLauncher(string: String) {

            Toast.makeText(context, string, Toast.LENGTH_LONG).show()



    }

}