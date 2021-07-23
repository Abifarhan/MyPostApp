package com.abifarhan.mypostapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abifarhan.mypostapp.Constanst.CATEGORY
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
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get () = _binding!!
    private var selectedCategory = FUNNY
    lateinit var adapter: ThoughtAdapter
    private val thought = arrayListOf<Thought>()
    private val dbCollection = FirebaseFirestore.getInstance().collection(THOUGHTS)
    lateinit var thoughtsListener: ListenerRegistration
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
    }

    override fun onResume() {
        super.onResume()
        setListener()
    }
    private fun setListener() {

        if (selectedCategory == POPULAR) {
            thoughtsListener = dbCollection
                .orderBy(NUM_LIKES, Query.Direction.DESCENDING)
                .addSnapshotListener(this){snapshot, exception ->

                    if (exception != null) {
                        Log.d("Exception","could note retrieve document")
                    }
                    if (snapshot != null) {
                        parseData(snapshot)
                    }
                }
        } else {
            thoughtsListener = dbCollection
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .whereEqualTo(CATEGORY, selectedCategory)
                .addSnapshotListener(this){snapshot, exception ->

                    if (exception != null) {
                        Log.d("Exception","could note retrieve document")
                    }
                    if (snapshot != null) {
                        parseData(snapshot)
                    }
                }
        }

    }
    fun onSeriousClicked(view: View) {
        if (selectedCategory == SERIOUS) {
            binding.mainSeriousBtn.isChecked = true
            return
        }
        binding.mainCrazyBtn.isChecked = false
        binding.mainPopularBtn.isChecked = false
        binding.mainFunnyBtn.isChecked = false
        selectedCategory = SERIOUS
        thoughtsListener.remove()
        setListener()
    }
    fun onCrazyClicked(view: View) {
        if (selectedCategory == CRAZY) {
            binding.mainCrazyBtn.isChecked = true
            return
        }
        binding.mainFunnyBtn.isChecked = false
        binding.mainSeriousBtn.isChecked = false
        binding.mainPopularBtn.isChecked = false
        selectedCategory = CRAZY
        thoughtsListener.remove()
        setListener()
    }
    fun onFunnyClicked(view: View) {
        if (selectedCategory == FUNNY) {
            binding.mainFunnyBtn.isChecked = true
            return
        }
        binding.mainCrazyBtn.isChecked = false
        binding.mainSeriousBtn.isChecked = false
        binding.mainPopularBtn.isChecked = false
        selectedCategory = FUNNY

        thoughtsListener.remove()
        setListener()
    }


    fun onPopularClicked(view: View) {
        if (selectedCategory == POPULAR) {
            binding.mainPopularBtn.isChecked = true
            return
        }
        binding.mainSeriousBtn.isChecked = false
        binding.mainFunnyBtn.isChecked = false
        binding.mainCrazyBtn.isChecked = false
        selectedCategory = POPULAR

        thoughtsListener.remove()
        setListener()
    }

    fun parseData(snapshot: QuerySnapshot) {
        thought.clear()
        for (document in snapshot.documents) {
            val data = document.data
            val name = data!![USERNAME] as String
            val timestamp: Date = document.getDate("timestamp")!!
            val thoughtTxt = data[THOUGHT_TXT] as String
            val numLikes = data[NUM_LIKES] as Long
            val numComments = data[NUM_COMMENTS] as Long
            val documentId = document.id

            val newThought = Thought(name,timestamp, thoughtTxt,numLikes.toInt(), numComments.toInt()
                , documentId)

            Log.d("this","This is the data $newThought")
            thought.add(newThought)
        }
        adapter.notifyDataSetChanged()
    }
}