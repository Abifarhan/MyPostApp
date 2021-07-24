package com.abifarhan.mypostapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.abifarhan.mypostapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPwLogin.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password
            )
                .addOnSuccessListener {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Anda gagal login, silahkan periksa kembali data Anda",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        binding.btnMakeAccount.setOnClickListener {
            startActivity(Intent(this, CreateUserActivity::class.java))
        }
    }
}