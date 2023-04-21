package com.example.aayushcamera

import android.content.ContentValues
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.aayushcamera.R
import com.example.aayushcamera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private fun setUpNavigation()
{
        setSupportActionBar(binding.toolbar)
    supportActionBar!!.setDisplayShowTitleEnabled(false)
    navController = findNavController(R.id.nav_host_fragment)
    appBarConfiguration = AppBarConfiguration(navController.graph)
}

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d(ContentValues.TAG, "Main Activity inflated successfully")
        setContentView(binding.root)

        setUpNavigation()
    }

    fun setUpToolBar(toolbarTitle: String, isRootPage: Boolean = false) {

        binding.toolbar.run {
            title = toolbarTitle
            if (!isRootPage) {
                setNavigationIcon(R.drawable.ic_arrow_white_24dp)
                setNavigationOnClickListener { view ->
                    view.findNavController().navigateUp()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

}