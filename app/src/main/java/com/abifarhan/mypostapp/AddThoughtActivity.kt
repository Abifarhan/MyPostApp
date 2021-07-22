package com.abifarhan.mypostapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.abifarhan.mypostapp.Constanst.CRAZY
import com.abifarhan.mypostapp.Constanst.FUNNY
import com.abifarhan.mypostapp.Constanst.SERIOUS
import com.abifarhan.mypostapp.Constanst.THOUGHTS
import com.abifarhan.mypostapp.databinding.ActivityAddThoughtBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AddThoughtActivity : AppCompatActivity() {

    private var _binding: ActivityAddThoughtBinding? = null
    private val binding get() = _binding!!
    private var selectedCategory = FUNNY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddThoughtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSavePost.setOnClickListener {

            val data = HashMap<String, Any>()
            data["category"] = selectedCategory
            data["numComments"] = 0
            data["numLikes"] = 0
            data["thoughtTxt"] = binding.addThoughtTxt.text.toString()
            data["timestamp"] = FieldValue.serverTimestamp()
            data["username"] = binding.addUserNameTxt.text.toString()

            FirebaseFirestore.getInstance().collection(THOUGHTS)
                .add(data)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener { exceptions ->
                    Log.e("Exceptions", "Could not add post: $exceptions")
                }
        }
    }

    fun addFunnyClicked(view: View) {
        binding.addCrazzyButton.isChecked = false
        binding.addSeriousButton.isChecked = false
        selectedCategory = FUNNY

    }
    fun onSeriousButtonClicked(view: View) {
        binding.addCrazzyButton.isChecked = false
        binding.addFunnyButton.isChecked = false
        selectedCategory = SERIOUS
    }
    fun onCrazyClicked(view: View) {
        binding.addSeriousButton.isChecked = false
        binding.addFunnyButton.isChecked = false
        selectedCategory = CRAZY
    }
}