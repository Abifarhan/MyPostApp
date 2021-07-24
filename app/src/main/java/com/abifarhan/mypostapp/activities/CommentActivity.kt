package com.abifarhan.mypostapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abifarhan.mypostapp.R
import com.abifarhan.mypostapp.adapter.CommentsAdapter
import com.abifarhan.mypostapp.databinding.ActivityCommentBinding
import com.abifarhan.mypostapp.model.Comment
import com.abifarhan.mypostapp.utils.Constanst.COMMENTS_REF
import com.abifarhan.mypostapp.utils.Constanst.COMMENT_TXT
import com.abifarhan.mypostapp.utils.Constanst.DOCUMENT_KEY
import com.abifarhan.mypostapp.utils.Constanst.NUM_COMMENTS
import com.abifarhan.mypostapp.utils.Constanst.THOUGHTS
import com.abifarhan.mypostapp.utils.Constanst.TIMESTAMP
import com.abifarhan.mypostapp.utils.Constanst.USERNAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*
import kotlin.collections.HashMap

class CommentActivity : AppCompatActivity() {
    private var _binding: ActivityCommentBinding? = null
    private val binding get() = _binding!!

    lateinit var thoughtDocumentId: String
    lateinit var commentAdapter : CommentsAdapter
    private val comments = arrayListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY).toString()
        println(thoughtDocumentId)

        commentAdapter = CommentsAdapter(comments)
        binding.rvComment.adapter = commentAdapter
        binding.rvComment.layoutManager = LinearLayoutManager(this)

        FirebaseFirestore.getInstance().collection(THOUGHTS)
            .document(thoughtDocumentId)
            .collection(COMMENTS_REF)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
            .addSnapshotListener{snapshot, exception ->
                if (exception != null) {
                    Log.d("tes", "Could no add new comment because ${exception.localizedMessage}")
                }

                if (snapshot != null) {
                    comments.clear()
                    for (document in snapshot.documents) {
                        val data = document.data
                        val name = data!![USERNAME] as String
                        val timestamp = document.getDate(TIMESTAMP)!!
                        val commentTxt = data[COMMENT_TXT] as String

                        val newComment = Comment(name,timestamp, commentTxt)
                        comments.add(newComment)
                    }
                    commentAdapter.notifyDataSetChanged()
                }
            }
        binding.addCommentButton.setOnClickListener {
            val commentTxt = binding.enterCommentText.text.toString()
            val thoughtRef =
                FirebaseFirestore.getInstance().collection(THOUGHTS).document(thoughtDocumentId)

            FirebaseFirestore.getInstance().runTransaction { transaction ->
                val thought = transaction.get(thoughtRef)
                val numComments = thought.getLong(NUM_COMMENTS)?.plus(1)
                transaction.update(thoughtRef, NUM_COMMENTS, numComments)

                val newCommentRef = FirebaseFirestore.getInstance()
                    .collection(THOUGHTS)
                    .document(thoughtDocumentId)
                    .collection(COMMENTS_REF)
                    .document()

                Log.d("tes", "this is the document data $thoughtDocumentId")
                val data = HashMap<String, Any>()
                data[COMMENT_TXT] = commentTxt
                data[TIMESTAMP] = FieldValue.serverTimestamp()
                data[USERNAME] = FirebaseAuth.getInstance().currentUser?.displayName.toString()

                transaction.set(newCommentRef, data)
            }
                .addOnSuccessListener {
                    binding.enterCommentText.setText("")
                    hideKeyboard()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception", "Could not add comment ${exception.localizedMessage}")
                }
        }


    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        }
    }
}