package com.example.windmoiveapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleEventObserver
import com.example.windmoiveapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SPLASH_TIME_OUT = 2000L
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val slideImage = AnimationUtils.loadAnimation(this, R.anim.anim_splash_screen)
        val animLogo = AnimationUtils.loadAnimation(this, R.anim.anim_faded)
        findViewById<TextView>(R.id.tvAppName).startAnimation(slideImage)
        findViewById<ImageView>(R.id.imvMovie).startAnimation(animLogo)

         Handler().postDelayed({ onNext() }, SPLASH_TIME_OUT);
    }

    private fun onNext() {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
    }
}