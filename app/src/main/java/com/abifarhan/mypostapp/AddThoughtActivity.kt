package com.abifarhan.mypostapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AddThoughtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_thought)
    }

    fun addFunnyClicked(view: View) {}
    fun onSeriousButtonClicked(view: View) {}
    fun onCrazyClicked(view: View) {}
}