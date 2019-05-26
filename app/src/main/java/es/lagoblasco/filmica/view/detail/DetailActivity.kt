package es.lagoblasco.filmica.view.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.FilmsRepo

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (savedInstanceState == null) {
            val fragment = DetailFragment()
            val id = intent.getStringExtra("id")
            val fromFragment = intent.getStringExtra("fromFragment")

            val args = Bundle()
            args.putString("id", id)
            args.putString("fromFragment", fromFragment)

            fragment.arguments = args

            supportFragmentManager.beginTransaction()
                .add(R.id.containerDetail, fragment)
                .commit()

        }
    }

}