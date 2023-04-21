package com.example.aayushcamera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.aayushcamera.R
import com.example.aayushcamera.databinding.ActivityMainBinding
import com.example.aayushcamera.databinding.FragmentLandingPageBinding

class LandingPageFragment : Fragment() {

    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

    private val requiredPermissions =
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    private lateinit var binding: FragmentLandingPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar("Welcome", true)
        if (checkPermissions()) onPermissionsGranted()
        binding.recurringButton.setOnClickListener {
            it.findNavController().navigate(R.id.recurringFragment)
        }

        binding.oneShotButton.setOnClickListener {
            it.findNavController().navigate(R.id.oneShotFragment)
        }
    }

    fun onPermissionsGranted() {
        Log.d("Landing", "permission granted")
        binding.recurringButton.isEnabled = true
        binding.oneShotButton.isEnabled = true
    }

    private fun checkPermissions(): Boolean {
        return if (mainActivity.hasPermissions(requiredPermissions)) true
        else {
            requestPermissions(requiredPermissions, CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE)
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("Landing", "Permsion result called")
        if (requestCode == CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE &&
            confirmPermissionResults(grantResults)
        ) onPermissionsGranted()
    }

    private fun confirmPermissionResults(results: IntArray): Boolean {
        results.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    companion object {
        const val CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 100
    }
}
