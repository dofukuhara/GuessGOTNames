package br.com.fdtechcorp.android.guessgotnames.lib.common.timer

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class TimerCase(backgroundDispatcher: CoroutineDispatcher) {
    val timerFlow: TimerFlow = TimerFlow(backgroundDispatcher)
    val timerStateFlow: StateFlow<TimerState> = timerFlow.timerStateFlow
}

class TimerFlow(private val backgroundDispatcher: CoroutineDispatcher) {
    private var _timerStateFlow = MutableStateFlow(TimerState())
    val timerStateFlow: StateFlow<TimerState> = _timerStateFlow

    private var job: Job? = null

    fun toggleTime(totalSeconds: Int, timerScope: CoroutineScope) {
        job = if (job == null) {
            timerScope.launch(backgroundDispatcher) {
                initTimer(totalSeconds)
                    .onCompletion { _timerStateFlow.emit(TimerState(0, totalSeconds)) }
                    .collect { _timerStateFlow.emit(it) }
            }
        } else {
            job?.cancel()
            null
        }
    }

    private fun initTimer(totalSeconds: Int): Flow<TimerState> =
        (totalSeconds - 1 downTo 0).asFlow()
            .onEach { delay(1000) }
            .conflate()
            .transform { remainingSeconds: Int ->
                emit(TimerState(remainingSeconds, totalSeconds))
            }
}

data class TimerState(val secondsRemaining: Int? = null, val totalSeconds: Int = 60) {
    val progressPercentage: Int = (((secondsRemaining ?: totalSeconds) / totalSeconds.toFloat()) * 100).toInt()
}
