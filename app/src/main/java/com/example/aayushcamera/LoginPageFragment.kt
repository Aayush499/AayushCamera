package com.example.aayushcamera

import android.content.ContentValues.TAG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.aayushcamera.databinding.FragmentLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import android.util.*
import androidx.navigation.findNavController
import android.widget.*


class LoginPageFragment: Fragment() {

    private val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }


    private lateinit var binding: FragmentLoginPageBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // mainActivity.setUpToolBar("Welcome", true)
        binding.loginButton.isEnabled = true
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (::binding.isInitialized) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            it.findNavController().navigate(R.id.landingPageFragment)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                context, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            else
            {
                Log.d(TAG, "signInWithEmail:failure")
            }
        }
    }





}