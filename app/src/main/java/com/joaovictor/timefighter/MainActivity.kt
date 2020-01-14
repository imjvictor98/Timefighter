package com.joaovictor.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    internal val TAG = MainActivity::class.java.simpleName

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var tapMeButton: Button
    internal lateinit var countDownTimer: CountDownTimer

    internal var gameStarted = false
    internal var initialCountDown:Long = 60000
    internal var intervalCountDown:Long = 1000
    internal var timeLeft = 60

    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called. Score is: $score")

        gameScoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        tapMeButton = findViewById<Button>(R.id.tap_me_button)

        tapMeButton.setOnClickListener { v ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            v.startAnimation(bounceAnimation)

            incrementScore()

        }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstance: Saving score: $score & Time Left: $timeLeft")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_settings){
            showInfo()
        }
        return true
    }

    private fun incrementScore() {
        //Lógica de incrementar a pontuação

        if(!gameStarted) {
            startGame()
        }

        score += 1

        val newScore = getString(R.string.pontuacao, Integer.toString(score))
        gameScoreTextView.text = newScore

    }

    private fun resetGame() {
        //Lógica de resetar o jogo


        score = 0

        val initialScore = getString(R.string.pontuacao, Integer.toString(score))
        gameScoreTextView.text = initialScore

        val initialTimeLeft = getString(R.string.tempo_restante, Integer.toString(60))
        timeLeftTextView.text = initialTimeLeft


        countDownTimer = object : CountDownTimer(initialCountDown, intervalCountDown) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.tempo_restante, Integer.toString(timeLeft))
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false

    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.sobre_nos, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.sobre_nos_mensagem)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }


    private fun restoreGame(){
        val restoredScore = getString(R.string.pontuacao, Integer.toString(score))
        gameScoreTextView.text = restoredScore

        val restoredTime = getString(R.string.tempo_restante, Integer.toString(timeLeft))
        timeLeftTextView.text = restoredTime

        countDownTimer = object : CountDownTimer((timeLeft * 1000).toLong(), intervalCountDown) {
            override fun onTick(millisUntilFinished: Long) {

                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.tempo_restante, Integer.toString(timeLeft))
                timeLeftTextView.text = timeLeftString

            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    private fun startGame() {
        //Lógica de iniciar o jogo

        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        //Lógica de finalizar o jogo

        Toast.makeText(this, getString(R.string.fim_jogo, Integer.toString(score)), Toast.LENGTH_LONG).show()
        resetGame()
    }

}
