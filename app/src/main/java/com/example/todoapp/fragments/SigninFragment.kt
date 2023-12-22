package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.databinding.FragmentSigninBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class SigninFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }
    private fun init(view: View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents(){
        binding.signupbtn.setOnClickListener {
            navControl.navigate(R.id.action_signinFragment_to_registerFragment)
        }
        binding.signinbtn.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val pass = binding.passwordInput.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty() ){
                binding.progressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(
                    OnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(context, "Successful sign in", Toast.LENGTH_LONG).show()
                            navControl.navigate(R.id.action_signinFragment_to_homeFragment)
                        } else{
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                )
            }else{
                Toast.makeText(context, "Fill empty fields", Toast.LENGTH_LONG).show()
            }
        }
    }
}