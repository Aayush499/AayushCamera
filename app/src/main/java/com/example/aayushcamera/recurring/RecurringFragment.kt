/**
 * Copyright (c) 2019 Cotta & Cush Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.aayushcamera.recurring

import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aayushcamera.R
//import com.example.aayushcamera.CaptureTimeFrequency
//import com.example.aayushcamera.HiddenCam
import com.example.aayushcamera.MainActivity
import com.example.mylibrary.CaptureTimeFrequency
import com.example.mylibrary.HiddenCam
import com.example.mylibrary.OnImageCapturedListener
//import com.example.aayushcamera.camera.ImageCapture
import com.example.aayushcamera.databinding.FragmentRecurringBinding
import java.io.File

class RecurringFragment : Fragment(), OnImageCapturedListener {

    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

private lateinit var hiddenCam: HiddenCam
    private lateinit var baseStorageFolder: File
    private lateinit var binding: FragmentRecurringBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecurringBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar(getString(R.string.recurring))
        baseStorageFolder = File(mainActivity.getExternalFilesDir(null), "HiddenCam").apply {
            if (exists()) deleteRecursively()
            mkdir()
        }
        hiddenCam = HiddenCam(
            mainActivity, baseStorageFolder, this,
            CaptureTimeFrequency.Recurring(RECURRING_INTERVAL),
            targetResolution = Size(1080, 1920)
        )
        binding.startCaptureButton.setOnClickListener {
            hiddenCam.start()
        }
        binding.stopCaptureButton.setOnClickListener {
            hiddenCam.stop()
        }
    }

    override fun onImageCaptured(image: File) {
        val message = "Image captured, saved to:${image.absolutePath}"
        log(message)
        showToast(message)
    }
//
    override fun onImageCaptureError(e: Throwable?) {
        e?.run {
            val message = "Image captured failed:${e.message}"
            showToast(message)
            log(message)
            printStackTrace()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        hiddenCam.destroy()
    }

    companion object {
        const val TAG = "RecurringFragment"
        const val RECURRING_INTERVAL = 10 * 1000L
    }
}
