package es.lagoblasco.filmica.view.films

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.Film
import es.lagoblasco.filmica.view.detail.DetailActivity
import es.lagoblasco.filmica.view.detail.DetailFragment
import es.lagoblasco.filmica.view.watchlist.WatchlistFragment
import kotlinx.android.synthetic.main.activity_films.*

const val TAG_FILM = "films"
const val TAG_WATCHLIST = "watchlist"
const val TAG_TREND = "trend"

class FilmsActivity : AppCompatActivity(), FilmsFragment.OnFilmClickListener {

    private lateinit var filmsFragment: FilmsFragment
    private lateinit var watchlistFragment: WatchlistFragment
    private lateinit var activeFragment: Fragment
    private lateinit var trendFragment: TrendFragment

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
                R.id.action_trend -> showMainFragment(trendFragment)
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
        trendFragment = TrendFragment()
        activeFragment = filmsFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.container, filmsFragment, TAG_FILM)
            .add(R.id.container, watchlistFragment, TAG_WATCHLIST)
            .add(R.id.container, trendFragment, TAG_TREND)
            .hide(watchlistFragment)
            .hide(trendFragment)
            .commit()
    }

    private fun restoreFragments(tag: String) {
        filmsFragment = supportFragmentManager.findFragmentByTag(TAG_FILM) as FilmsFragment
        watchlistFragment = supportFragmentManager.findFragmentByTag(TAG_WATCHLIST) as WatchlistFragment
        trendFragment = supportFragmentManager.findFragmentByTag(TAG_TREND) as TrendFragment

        activeFragment =
            if (tag == TAG_WATCHLIST)
                watchlistFragment

            else if (tag == TAG_TREND)
                trendFragment

            else
                filmsFragment


    }

    private fun showMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()

        activeFragment = fragment
    }

    override fun onClick(film: Film) {
        if (!isDetailDetailViewAvailable()) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", film.id)
            startActivity(intent)
        } else {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container_detail,
                    DetailFragment.newInstance(film.id)
                )
                .commit()
        }
    }

    private fun isDetailDetailViewAvailable() =
        findViewById<FrameLayout>(R.id.container_detail) != null
}