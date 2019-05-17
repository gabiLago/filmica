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
        val fragment: DetailFragment = DetailFragment()

        var args = Bundle()
        args.putString("id", id)

        fragment.arguments = args

        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment)
            .commit()



    }
}
