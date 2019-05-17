package es.lagoblasco.filmica

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class FilmsActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        val fragment = FilmsFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment)
            .commit()
    }
}