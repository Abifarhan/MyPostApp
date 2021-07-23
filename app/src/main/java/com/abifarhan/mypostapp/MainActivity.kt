package com.abifarhan.mypostapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abifarhan.mypostapp.Constanst.CRAZY
import com.abifarhan.mypostapp.Constanst.FUNNY
import com.abifarhan.mypostapp.Constanst.NUM_COMMENTS
import com.abifarhan.mypostapp.Constanst.NUM_LIKES
import com.abifarhan.mypostapp.Constanst.POPULAR
import com.abifarhan.mypostapp.Constanst.SERIOUS
import com.abifarhan.mypostapp.Constanst.THOUGHTS
import com.abifarhan.mypostapp.Constanst.THOUGHT_TXT
import com.abifarhan.mypostapp.Constanst.TIMESTAMP
import com.abifarhan.mypostapp.Constanst.USERNAME
import com.abifarhan.mypostapp.databinding.ActivityMainBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get () = _binding!!
    private var selectedCategory = FUNNY
    lateinit var adapter: ThoughtAdapter
    private val thought = arrayListOf<Thought>()
    private val dbCollection = FirebaseFirestore.getInstance().collection(THOUGHTS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddThoughtActivity::class.java))
        }

        adapter = ThoughtAdapter(thought)
        binding.rvMain.adapter = adapter
        binding.rvMain.layoutManager = LinearLayoutManager(this)

        dbCollection.get()
            .addOnSuccessListener {
                for (document in it) {
                    val data = document.data
                    val name = data[USERNAME] as String
                    val timestamp: Date = document.getDate("timestamp")!!
                    val thoughtTxt = data[THOUGHT_TXT] as String
                    val numLikes = data[NUM_LIKES] as Long
                    val numComments = data[NUM_COMMENTS] as Long
                    val documentId = document.id

                    val newThought = Thought(name,timestamp, thoughtTxt,numLikes.toInt(), numComments.toInt()
                    , documentId)

                    thought.add(newThought)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("Exception","could not fect data")
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