package edu.farmingdale.threadsexample.countdowntimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null

    // Time picker selections
    var selectedHour by mutableIntStateOf(0); private set
    var selectedMinute by mutableIntStateOf(0); private set
    var selectedSecond by mutableIntStateOf(0); private set

    // Duration at start and remaining time
    var totalMillis by mutableLongStateOf(0L); private set
    var remainingMillis by mutableLongStateOf(0L); private set

    var isRunning by mutableStateOf(false); private set

    // ToDo 5: progress (1f -> 0f)
    val progress: Float
        get() = if (totalMillis <= 0L) 0f
        else (remainingMillis.coerceAtLeast(0L).toFloat() / totalMillis.toFloat())

    // To Do 7: one-shot finish cue
    var playFinishCue by mutableStateOf(false); private set
    fun consumeFinishCue() { playFinishCue = false }

    fun selectTime(hour: Int, min: Int, sec: Int) {
        selectedHour = hour
        selectedMinute = min
        selectedSecond = sec
    }

    fun startTimer() {
        // compute duration
        totalMillis = (selectedHour * 3600 + selectedMinute * 60 + selectedSecond) * 1000L
        if (totalMillis <= 0L) {
            isRunning = false
            remainingMillis = 0L
            return
        }

        // initialize
        playFinishCue = false
        remainingMillis = totalMillis
        isRunning = true

        // (re)start job
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (remainingMillis > 0L) {
                delay(1000L)
                remainingMillis = (remainingMillis - 1000L).coerceAtLeast(0L)
            }
            isRunning = false
            playFinishCue = true
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        isRunning = false
        remainingMillis = 0L
    }

    // To Do 6: “Rest” the timer back to the full duration
    fun restTimer() {
        timerJob?.cancel()
        isRunning = false
        if (totalMillis > 0L) {
            remainingMillis = totalMillis
        } else {
            remainingMillis = 0L
        }
    }

    //This method is used to reset the timer
    fun resetTimer() = restTimer()

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
