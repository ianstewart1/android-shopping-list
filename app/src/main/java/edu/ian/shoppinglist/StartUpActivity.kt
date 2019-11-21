package edu.ian.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_startup.*


class StartUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        var startAnim = AnimationUtils.loadAnimation(
            this@StartUpActivity,
            R.anim.startup)

        var textAnim = AnimationUtils.loadAnimation(
            this@StartUpActivity,
            R.anim.text)

        startAnim.setAnimationListener(
            object: Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val mainIntent = Intent(this@StartUpActivity, MainActivity::class.java)
                    this@StartUpActivity.startActivity(mainIntent)
                    this@StartUpActivity.finish()
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            }
        )

        ivLogo.startAnimation(startAnim)
        tvStart.startAnimation(textAnim)
    }
}
