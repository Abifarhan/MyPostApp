package com.abifarhan.mypostapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.abifarhan.mypostapp.Constanst.CRAZY
import com.abifarhan.mypostapp.Constanst.FUNNY
import com.abifarhan.mypostapp.Constanst.POPULAR
import com.abifarhan.mypostapp.Constanst.SERIOUS
import com.abifarhan.mypostapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get () = _binding!!
    private var selectedCategory = FUNNY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddThoughtActivity::class.java))
        }
    }

    fun onSeriousClicked(view: View) {
        binding.mainCrazyBtn.isChecked = false
        binding.mainFunnyBtn.isChecked = false
        binding.mainPopularBtn.isChecked = false
        selectedCategory = SERIOUS
    }
    fun onFunnyClicked(view: View) {
        binding.mainCrazyBtn.isChecked = false
        binding.mainSeriousBtn.isChecked = false
        binding.mainPopularBtn.isChecked = false
        selectedCategory = FUNNY
    }

    fun onCrazyClicked(view: View) {
        binding.mainPopularBtn.isChecked = false
        binding.mainFunnyBtn.isChecked = false
        binding.mainSeriousBtn.isChecked = false
        selectedCategory = CRAZY
    }
    fun onPopularClicked(view: View) {
        binding.mainSeriousBtn.isChecked = false
        binding.mainFunnyBtn.isChecked = false
        binding.mainCrazyBtn.isChecked = false
        selectedCategory = POPULAR
    }
}