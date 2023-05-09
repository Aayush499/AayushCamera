package com.example.aayushcamera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
    private val cameraPermission = Manifest.permission.CAMERA
    private val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

    private val requiredPermissions =
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    private lateinit var binding: FragmentLandingPageBinding

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Landing", "Camera permission granted")
            checkWritePermission()
        } else {
            Log.e("Landing", "Camera permission denied")
        }
    }

    private val writePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Landing", "Write permission granted")
            onPermissionsGranted()
        } else {
            Log.e("Landing", "Write permission denied")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mainActivity.setUpToolBar("Welcome", true)
        if (true) {
            onPermissionsGranted()
//            //toast all permissions are granted
//            Toast.makeText(requireContext(), "All permissions are granted", Toast.LENGTH_SHORT).show()
//        } else {
//            //toast that permissions are not granted
//            Toast.makeText(requireContext(), "Permissions are not granted", Toast.LENGTH_SHORT).show()
//           checkWritePermission()

        }

        //log a message when permission has been asked
        Log.d("Landing", "permission asked")
            binding.recurringButton.setOnClickListener {
                it.findNavController().navigate(R.id.recurringFragment)
            }

            binding.oneShotButton.setOnClickListener {
                it.findNavController().navigate(R.id.oneShotFragment)
            }

    }

    private fun onPermissionsGranted() {
        Log.d("Landing", "permission granted")
        binding.recurringButton.isEnabled = true
        binding.oneShotButton.isEnabled = true
    }

//    private fun requestPermissions() {
//        writePermissionLauncher.launch()
//    }
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                cameraPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            checkWritePermission()
        } else {
            cameraPermissionLauncher.launch(cameraPermission)
        }
    }

    private fun checkWritePermission() {
        //
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                writePermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionsGranted()
        } else {
            writePermissionLauncher.launch(writePermission)
        }
    }
    private fun hasRequiredPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

//make a function checkPermissions that returns a boolean
//    private fun checkPermissions(): Boolean {
//        return if (mainActivity.hasPermissions(requiredPermissions)) true
//        else {
//
//            //request permissions
//            requestPermissions(requiredPermissions, CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE)
//            false
//        }
//    }

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
