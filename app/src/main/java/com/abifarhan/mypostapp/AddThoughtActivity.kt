package com.abifarhan.mypostapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.abifarhan.mypostapp.databinding.ActivityAddThoughtBinding

class AddThoughtActivity : AppCompatActivity() {

    private var _binding: ActivityAddThoughtBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddThoughtBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun addFunnyClicked(view: View) {
        binding.addCrazzyButton.isChecked = false
        binding.addSeriousButton.isChecked = false
    }
    fun onSeriousButtonClicked(view: View) {
        binding.addCrazzyButton.isChecked = false
        binding.addFunnyButton.isChecked = false
    }
    fun onCrazyClicked(view: View) {
        binding.addSeriousButton.isChecked = false
        binding.addFunnyButton.isChecked = false
    }
}