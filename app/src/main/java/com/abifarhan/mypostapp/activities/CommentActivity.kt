package com.abifarhan.mypostapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.abifarhan.mypostapp.R
import com.abifarhan.mypostapp.databinding.ActivityCommentBinding
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

class CommentActivity : AppCompatActivity() {
    private var _binding: ActivityCommentBinding? = null
    private val binding get() = _binding!!

    lateinit var thoughtDocumentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY).toString()
        println(thoughtDocumentId)

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
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception", "Could not add comment ${exception.localizedMessage}")
                }
        }


    }


}