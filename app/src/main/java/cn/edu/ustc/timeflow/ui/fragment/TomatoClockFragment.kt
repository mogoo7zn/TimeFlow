package cn.edu.ustc.timeflow.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.timeflow.R

class TomatoClockFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var startButton: Button
    private lateinit var terminateButton: Button
    private lateinit var tomatoCountTextView: TextView
    private var tomatoCount = 0
    private var isRunning = false
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tomato_clock, container, false)

        progressBar = view.findViewById(R.id.progress_bar)
        startButton = view.findViewById(R.id.start_button)
        tomatoCountTextView = view.findViewById(R.id.tomato_title)
        terminateButton = view.findViewById(R.id.terminate_button)

        startButton.setOnClickListener {
            if (isRunning) {
                stopTimer()
            } else {
                startTimer()
            }
        }

        terminateButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    private fun startTimer() {
        isRunning = true
        startButton.text = getString(R.string.tomato_clock_resume)
        countDownTimer = object : CountDownTimer(1500000, 1000) { // 25 minutes
            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished / 1500000.0 * 100).toInt()
                progressBar.progress = progress
            }

            override fun onFinish() {
                isRunning = false
                startButton.text = "Start"
                tomatoCount++
                tomatoCountTextView.text = "Tomato Clocks Completed: $tomatoCount"
            }
        }.start()
    }

    private fun stopTimer() {
        isRunning = false
        startButton.text = "Start"
        countDownTimer.cancel()
    }
}