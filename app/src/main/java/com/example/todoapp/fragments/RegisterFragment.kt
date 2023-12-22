package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }

    private fun registerEvents() {
        binding.signinbtn.setOnClickListener {
            navControl.navigate(R.id.action_registerFragment_to_signinFragment)
        }

        binding.nextbtn.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val pass = binding.passwordInput.text.toString().trim()
            val passrep = binding.repeatpassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && passrep.isNotEmpty()) {
                if (passrep == pass) {
                    binding.progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener (
                        OnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Registered successfully", Toast.LENGTH_LONG).show()
                                navControl.navigate(R.id.action_registerFragment_to_homeFragment)
                            } else {
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                            }
                            binding.progressBar.visibility = View.GONE
                    })
                }else{
                    Toast.makeText(context, "Passwords does not match", Toast.LENGTH_LONG).show()
                }

            }
        }

    }
}