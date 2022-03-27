package ru.gb.searchmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gb.searchmovies.databinding.ActivityMainBinding
import ru.gb.searchmovies.presentation.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}