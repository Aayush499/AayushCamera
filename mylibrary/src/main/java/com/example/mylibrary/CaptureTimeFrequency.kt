package com.example.mylibrary

sealed class CaptureTimeFrequency{
 object OneShot : CaptureTimeFrequency()

    class Recurring(val captureIntervalMillis: Long) : CaptureTimeFrequency()


}