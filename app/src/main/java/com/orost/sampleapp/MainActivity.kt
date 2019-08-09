package com.orost.sampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orost.sampleapp.ui.NewsFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val newsFragment: NewsFragment by inject()
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.fragment_container, newsFragment)
                .commit()
        }
    }
}
