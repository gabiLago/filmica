package es.lagoblasco.filmica.view.films

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.Film
import es.lagoblasco.filmica.view.detail.PlaceholderFragment
import es.lagoblasco.filmica.view.detail.DetailActivity
import es.lagoblasco.filmica.view.detail.DetailFragment
import es.lagoblasco.filmica.view.watchlist.WatchlistFragment
import kotlinx.android.synthetic.main.activity_films.*

const val TAG_FILM = "films"
const val TAG_WATCHLIST = "watchlist"
const val TAG_TRENDING = "trending"
const val TAG_SEARCH = "search"

class FilmsActivity : AppCompatActivity(),
    FilmsFragment.OnFilmClickListener,
    TrendingFragment.OnFilmClickListener,
    SearchFragment.OnFilmClickListener,
    WatchlistFragment.OnWatchlistClickListener,
    WatchlistFragment.OnDeletedWatchlistFilm {

    private lateinit var filmsFragment: FilmsFragment
    private lateinit var watchlistFragment: WatchlistFragment
    private lateinit var activeFragment: Fragment
    private lateinit var trendingFragment: TrendingFragment
    private lateinit var searchFragment: SearchFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState == null) {
            setupFragments()
        } else {
            val tag = savedInstanceState.getString("active", TAG_FILM)
            restoreFragments(tag)
        }

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) { // It's like a switch statement
                R.id.action_discover -> showMainFragment(filmsFragment)
                R.id.action_watchlist -> showMainFragment(watchlistFragment)
                R.id.action_trending -> showMainFragment(trendingFragment)
                R.id.action_search -> showMainFragment(searchFragment)
            }

            true
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("active", activeFragment.tag)
    }

    private fun setupFragments() {
        filmsFragment = FilmsFragment()
        watchlistFragment = WatchlistFragment()
        trendingFragment = TrendingFragment()
        searchFragment = SearchFragment()
        activeFragment = filmsFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.container, filmsFragment, TAG_FILM)
            .add(R.id.container, watchlistFragment, TAG_WATCHLIST)
            .add(R.id.container, trendingFragment, TAG_TRENDING)
            .add(R.id.container, searchFragment, TAG_SEARCH)
            .hide(watchlistFragment)
            .hide(trendingFragment)
            .hide(searchFragment)
            .commit()

        showDetailFragmentPlaceholder()

    }

    private fun restoreFragments(tag: String) {
        filmsFragment = supportFragmentManager.findFragmentByTag(TAG_FILM) as FilmsFragment
        watchlistFragment = supportFragmentManager.findFragmentByTag(TAG_WATCHLIST) as WatchlistFragment
        trendingFragment = supportFragmentManager.findFragmentByTag(TAG_TRENDING) as TrendingFragment
        searchFragment = supportFragmentManager.findFragmentByTag(TAG_SEARCH) as SearchFragment


        activeFragment =
            if (tag == TAG_WATCHLIST)
                watchlistFragment
            else if (tag == TAG_TRENDING)
                trendingFragment
            else if (tag == TAG_SEARCH)
                searchFragment
            else
                filmsFragment

        if (isDetailDetailViewAvailable()) {
            showDetailFragmentPlaceholder()
        }

    }

    private fun showMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()

        activeFragment = fragment

        if (isDetailDetailViewAvailable()) {
            showDetailFragmentPlaceholder() // Avoid showing last Detail when changing menu item

            if (fragment == watchlistFragment) { // Watchlist did't refresh the view
                supportFragmentManager.beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit()
            }
        }
    }


    private fun showDetailFragmentPlaceholder() {
        if (isDetailDetailViewAvailable()) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container_detail,
                    PlaceholderFragment()
                )
                .commit()
        }
    }


    override fun onClick(film: Film) {
        val testFragment = activeFragment.getTag().toString()
        if (!isDetailDetailViewAvailable()) {


            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", film.id)
            intent.putExtra("fromFragment", testFragment)

            startActivity(intent)


        } else {

            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container_detail,
                    DetailFragment.newInstance(film.id, testFragment)
                )
                .commit()
        }
    }

    override fun onDeleted() {
        if (isDetailDetailViewAvailable()) {
            showDetailFragmentPlaceholder()
        }
    }


    private fun isDetailDetailViewAvailable() =
        findViewById<FrameLayout>(R.id.container_detail) != null
}