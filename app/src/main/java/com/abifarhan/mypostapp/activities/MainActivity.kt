package com.abifarhan.mypostapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.abifarhan.mypostapp.utils.Constanst.CATEGORY
import com.abifarhan.mypostapp.utils.Constanst.CRAZY
import com.abifarhan.mypostapp.utils.Constanst.FUNNY
import com.abifarhan.mypostapp.utils.Constanst.NUM_COMMENTS
import com.abifarhan.mypostapp.utils.Constanst.NUM_LIKES
import com.abifarhan.mypostapp.utils.Constanst.POPULAR
import com.abifarhan.mypostapp.utils.Constanst.SERIOUS
import com.abifarhan.mypostapp.utils.Constanst.THOUGHTS
import com.abifarhan.mypostapp.utils.Constanst.THOUGHT_TXT
import com.abifarhan.mypostapp.utils.Constanst.USERNAME
import com.abifarhan.mypostapp.R
import com.abifarhan.mypostapp.model.Thought
import com.abifarhan.mypostapp.adapter.ThoughtAdapter
import com.abifarhan.mypostapp.databinding.ActivityMainBinding
import com.abifarhan.mypostapp.utils.Constanst.DOCUMENT_KEY
import com.abifarhan.mypostapp.utils.Constanst.TIMESTAMP
import com.google.firebase.auth.FirebaseAuth
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
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddThoughtActivity::class.java))
        }

        adapter = ThoughtAdapter(thought){ thoughts ->
            val commentActivity = Intent(this, CommentActivity::class.java)
            commentActivity.putExtra(DOCUMENT_KEY, thoughts.documentId)
            Log.d("data","this is the data you want to send $thoughts")
            startActivity(commentActivity)

        }
        binding.rvMain.adapter = adapter
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        auth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
    private fun setListener() {

        if (selectedCategory == POPULAR) {
            thoughtsListener = dbCollection
                .orderBy(NUM_LIKES, Query.Direction.ASCENDING)
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

    private fun parseData(snapshot: QuerySnapshot) {
        thought.clear()
        for (document in snapshot.documents) {
            val data = document.data
            val name = data!![USERNAME] as String
            val timestamp: Date = document.getDate(TIMESTAMP)!!
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItem = menu?.getItem(0)
        if (auth.currentUser == null) {
            menuItem?.title = "Login"
        }else{
            menuItem?.title = "Logout"
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_login) {
            if (auth.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else{
                auth.signOut()
                updateUI()
            }
            return true
        }
        return false
    }

    fun updateUI() {
        if (auth.currentUser == null) {
            binding.mainCrazyBtn.isEnabled = false
            binding.mainPopularBtn.isEnabled = false
            binding.mainSeriousBtn.isEnabled = false
            binding.mainFunnyBtn.isEnabled = false
            binding.floatingActionButton.isEnabled = false
            thought.clear()
            adapter.notifyDataSetChanged()
        } else {
            binding.mainCrazyBtn.isEnabled = true
            binding.mainPopularBtn.isEnabled = true
            binding.mainSeriousBtn.isEnabled = true
            binding.mainFunnyBtn.isEnabled = true
            binding.floatingActionButton.isEnabled = true
            setListener()
        }
    }
}