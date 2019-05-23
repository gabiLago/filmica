package es.lagoblasco.filmica.view.films

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
    var query: String = ""


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
         return inflater.inflate(R.layout.fragment_search, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = adapter

        buttonRetry.setOnClickListener {
            reload(query)
        }

        moviesSearchListener()
    }



    override fun onResume() {
        super.onResume()
        reload(query)
    }

    private fun reload(query: String) {

       if(query != "") {
           showProgress()

           FilmsRepo.searchFilms(context!!,
               { films ->

                   adapter.setFilms(films)
                   showList()
                   if(films.size > 0) {
                       showList()
                   } else {
                       noResults()
                   }

               }, { errorRequest ->
                   showError()
               },
               query
           )

       } else {
           initSearch()
       }

    }

    private fun initSearch() {
        filmsProgress.visibility = View.INVISIBLE
        error.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
        noResults.visibility = View.INVISIBLE
    }

    private fun showList() {
        filmsProgress.visibility = View.INVISIBLE
        error.visibility = View.INVISIBLE
        list.visibility = View.VISIBLE
        noResults.visibility = View.INVISIBLE
    }

    private fun noResults() {
        noResults.visibility = View.VISIBLE
        filmsProgress.visibility = View.INVISIBLE
        error.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
    }

    private fun showError() {
        filmsProgress.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
        error.visibility = View.VISIBLE
        noResults.visibility = View.INVISIBLE
    }

    private fun showProgress() {
        filmsProgress.visibility = View.VISIBLE
        error.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
        noResults.visibility = View.INVISIBLE
    }


    interface OnFilmClickListener {
        fun onClick(film: Film)
    }




    private fun moviesSearchListener() {
        /* moviesSearchBox.setOnClickListener {


            query = moviesSearchBox.text.toString()

            Toast.makeText(context, query, Toast.LENGTH_LONG).show()

            reload(query)

        }*/

        moviesSearchBox.setOnEditorActionListener() { v, actionId, event ->
            query = moviesSearchBox.text.toString()
            reload(query)
            moviesSearchBox.hideKeyboard()
            true
        }
    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}