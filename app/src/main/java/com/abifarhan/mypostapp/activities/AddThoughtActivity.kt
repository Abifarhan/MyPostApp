package com.abifarhan.mypostapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.abifarhan.mypostapp.utils.Constanst.CATEGORY
import com.abifarhan.mypostapp.utils.Constanst.CRAZY
import com.abifarhan.mypostapp.utils.Constanst.FUNNY
import com.abifarhan.mypostapp.utils.Constanst.NUM_COMMENTS
import com.abifarhan.mypostapp.utils.Constanst.NUM_LIKES
import com.abifarhan.mypostapp.utils.Constanst.SERIOUS
import com.abifarhan.mypostapp.utils.Constanst.THOUGHTS
import com.abifarhan.mypostapp.utils.Constanst.THOUGHT_TXT
import com.abifarhan.mypostapp.utils.Constanst.TIMESTAMP
import com.abifarhan.mypostapp.utils.Constanst.USERNAME
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
            data[CATEGORY] = selectedCategory
            data[NUM_COMMENTS] = 0
            data[NUM_LIKES] = 0
            data[THOUGHT_TXT] = binding.addThoughtTxt.text.toString()
            data[TIMESTAMP] = FieldValue.serverTimestamp()
            data[USERNAME] = binding.addUserNameTxt.text.toString()

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