package com.example.mylibrary

import android.os.Handler
import android.os.Message

internal class CaptureTimerHandler (private val captureInterval: Long,
                                    private val captureTimeListener: CaptureTimeListener) : Handler() {
    companion object {
        /** Message code for Capture to uniquely identify our message */
        private const val UPDATE_TIMER_COMMAND = 100
        /** Two Second initial Delay for Automatic Captures */
        private const val INITIAL_CAPTURE_DELAY = 2 * 1000L
    }
    override fun handleMessage(msg: Message) {
        captureTimeListener.onCaptureTimeTick()
        queueNextCapture(captureInterval)
    }

    /** Removes current message and re-queue it with a delay.
     * @param delay the delay in millisecond.
     * */
    private fun queueNextCapture(delay: Long) {
        removeMessages(UPDATE_TIMER_COMMAND)
        sendMessageDelayed(obtainMessage(UPDATE_TIMER_COMMAND), delay)
    }

    /** Stop Automatic Captures */
    fun stopUpdates() = removeMessages(UPDATE_TIMER_COMMAND)

    /** Start Automatic Captures */
    fun startUpdates() = queueNextCapture(INITIAL_CAPTURE_DELAY)
                                    }

internal interface CaptureTimeListener {

    /** Called on every time delay is passed */
    fun onCaptureTimeTick()
}
