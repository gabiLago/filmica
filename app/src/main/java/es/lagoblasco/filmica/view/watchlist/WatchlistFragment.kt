package es.lagoblasco.filmica.view.watchlist


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.Film
import es.lagoblasco.filmica.data.FilmsRepo
import es.lagoblasco.filmica.view.util.BaseFilmHolder
import es.lagoblasco.filmica.view.util.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_watchlist.*
import android.support.design.widget.Snackbar
import android.widget.Toast


class WatchlistFragment : Fragment() {

    lateinit var listener: OnWatchlistClickListener
    lateinit var onDeleteItemListener: OnDeletedWatchlistFilm

    val adapter: WatchlistAdapter = WatchlistAdapter {
            film -> listener.onClick(film)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnWatchlistClickListener && context is OnDeletedWatchlistFilm) {
            listener = context
            onDeleteItemListener = context
        } else {
            throw IllegalArgumentException("The attached activity isn't implementing ${OnWatchlistClickListener::class.java.canonicalName} or ${OnDeletedWatchlistFilm::class.java.canonicalName} ")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeHandler()
        watchlist.adapter = adapter
    }


    private fun setupSwipeHandler() {
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
                val film = (holder as BaseFilmHolder).film
                val position = holder.adapterPosition
                deleteFilm(film, position)

                onDeleteItemListener.onDeleted()

                // Snackbar for Undo Swipe Delete
                Snackbar.make(watchlist, "Deleted from Watchlist", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        FilmsRepo.saveFilm(context!!, film) {
                            Toast.makeText(context, "Deletion from Watchlist undone", Toast.LENGTH_LONG).show()

                            onResume() //It needs to refresh the load of the films.
                        }
                    }
                    .show()

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(watchlist)

    }


    private fun deleteFilm(film: Film, position: Int) {
        FilmsRepo.deleteFilm(context!!, film) {
            adapter.deleteFilm(position)
        }


    }

    override fun onResume() {
        super.onResume()

        FilmsRepo.getFilms(context!!) {
            adapter.setFilms(it)
        }
    }

    interface OnWatchlistClickListener {
        fun onClick(film: Film)
    }

    interface OnDeletedWatchlistFilm {
        fun onDeleted()
    }
}