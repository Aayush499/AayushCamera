package com.example.mylibrary

import android.content.Context
import android.net.Uri
import android.util.Size
import androidx.camera.core.*
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import java.io.File
import com.example.mylibrary.CaptureTimeFrequency.OneShot
import com.example.mylibrary.CaptureTimeFrequency.Recurring

import androidx.camera.core.*
import androidx.camera.core.impl.PreviewConfig
import com.example.mylibrary.CaptureTimeFrequency
import androidx.camera.*


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class HiddenCam @JvmOverloads constructor(
    context: Context,
    private val baseFileDirectory: File,
    private val imageCapturedListener: OnImageCapturedListener,
    private val captureFrequency: CaptureTimeFrequency = CaptureTimeFrequency.OneShot,
    //private val targetAspectRatio: TargetAspectRatio? = null,
    private val targetResolution: Size? = null,
    private val targetRotation: Int? = null,
    //private val cameraType: CameraType = CameraType.BACK_CAMERA
)  {
    private lateinit var captureTimer: CaptureTimerHandler
    private val lifeCycleOwner = HiddenCamLifeCycleOwner()


 private var preview : Preview

    private var imageCapture: ImageCapture

    init {
        if (context.hasPermissions()) {
            imageCapture  = ImageCapture.Builder().apply {
                if (targetRotation != null) setTargetRotation(targetRotation)
                if (targetResolution != null) setTargetResolution(targetResolution)
                setTargetAspectRatio(AspectRatio.RATIO_4_3)
            }.build()
            preview =  Preview.Builder().apply {
                if (targetRotation != null) setTargetRotation(targetRotation)
                setTargetAspectRatio(AspectRatio.RATIO_4_3)
                if (targetResolution != null) setTargetResolution(targetResolution)
            }.build()
//      preview.setOnPreviewOutputUpdateListener { }

//            CameraX.bindToLifecycle(lifeCycleOwner, preview, imageCapture)
            when (val interval = captureFrequency) {
                OneShot -> {
                    // Nothing for now, we don't need to schedule anything
                }
                is Recurring -> {
                    captureTimer = CaptureTimerHandler(
                        interval.captureIntervalMillis,
                        object : CaptureTimeListener {
                            override fun onCaptureTimeTick() {
                                capture()
                            }
                        })
                }
            }
        } else throw SecurityException("You need to have access to both CAMERA and WRITE_EXTERNAL_STORAGE permissions")
    }

    /**
     * Mark [HiddenCam]'s lifecycle as started. If the [CaptureTimeFrequency] supplied is
     * [Recurring], automatic image captures are triggered.
     */
    fun start() {
        lifeCycleOwner.start()
        if (captureFrequency is Recurring) captureTimer.startUpdates()
    }

    /**
     * Mark [HiddenCam]'s lifecycle as stopped. If the [CaptureTimeFrequency] supplied is
     * [Recurring], automatic image captures are stopped.
     */
    fun stop() {
        lifeCycleOwner.stop()
        if (captureFrequency is Recurring) captureTimer.stopUpdates()
    }

    /**
     * Mark [HiddenCam]'s lifecycle as destroyed. If the kind of [CaptureTimeFrequency] supplied is
     * [Recurring], Automatic image captures are stopped.
     */
    fun destroy() {
        lifeCycleOwner.tearDown()
        //imageCapture.markState(Lifecycle.State.DESTROYED)

        if (captureFrequency is Recurring) captureTimer.stopUpdates()
    }

    /**
     * Method to manually trigger an image capture.
     * @see capture
     */
    fun captureImage() {
        if (captureFrequency is OneShot) {
            capture()
        }
    }

    private fun capture() {
        var img = createFile(baseFileDirectory)
        var opFile : ImageCapture.OutputFileOptions = ImageCapture.OutputFileOptions.Builder(img).build();

        imageCapture.takePicture(

            opFile,
            MainThreadExecutor,
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(exception: ImageCaptureException) {
                    imageCapturedListener.onImageCaptureError(exception)


                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    imageCapturedListener.onImageCaptured(img)

                }

            })
    }

}