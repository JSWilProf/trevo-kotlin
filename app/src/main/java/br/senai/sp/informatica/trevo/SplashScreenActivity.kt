package br.senai.sp.informatica.trevo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import br.senai.sp.informatica.trevo.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ocultar(binding.splashscreen)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(
                    this@SplashScreenActivity,
                    MainActivity::class.java
                )
            )
        }, 3000)
    }

    private fun ocultar(view: View) {
        if(Build.VERSION.SDK_INT >= 30) {
            view.windowInsetsController?.hide(WindowInsets.Type.navigationBars())
        } else {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }
}