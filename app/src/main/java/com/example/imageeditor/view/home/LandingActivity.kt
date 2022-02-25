package com.example.imageeditor.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.imageeditor.R
import com.example.imageeditor.view.MainActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var splashImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        splashImage = findViewById(R.id.splashScreen)
        splashImage.alpha = 0.0001f;
        splashImage.animate().setDuration(3000).alpha(1f).withEndAction {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}