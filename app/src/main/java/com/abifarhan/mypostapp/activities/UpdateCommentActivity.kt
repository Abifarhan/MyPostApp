package com.abifarhan.mypostapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.abifarhan.mypostapp.R
import com.abifarhan.mypostapp.databinding.ActivityUpdateCommentBinding
import com.abifarhan.mypostapp.utils.Constanst.COMMENTS_REF
import com.abifarhan.mypostapp.utils.Constanst.COMMENT_DOC_ID_EXTRA
import com.abifarhan.mypostapp.utils.Constanst.COMMENT_TXT
import com.abifarhan.mypostapp.utils.Constanst.COMMENT_TXT_EXTRA
import com.abifarhan.mypostapp.utils.Constanst.THOUGHTS
import com.abifarhan.mypostapp.utils.Constanst.THOUGHT_DOC_ID_EXTRA
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCommentActivity : AppCompatActivity() {
    private var _binding: ActivityUpdateCommentBinding? = null
    private val binding get() = _binding!!

    lateinit var thoughtDocId: String
    lateinit var commentDocId: String
    lateinit var commentTxt: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUpdateCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thoughtDocId = intent.getStringExtra(THOUGHT_DOC_ID_EXTRA)!!
        commentDocId = intent.getStringExtra(COMMENT_DOC_ID_EXTRA)!!
        commentTxt = intent.getStringExtra(COMMENT_TXT_EXTRA)!!

        binding.edtEditComment.setText(commentTxt)

        binding.btnSubmitEditComment.setOnClickListener {
            FirebaseFirestore.getInstance().collection(THOUGHTS)
                .document(thoughtDocId)
                .collection(COMMENTS_REF)
                .document(commentDocId)
                .update(COMMENT_TXT, binding.edtEditComment.text.toString())
                .addOnSuccessListener {
                    hideKeyboard()
                    finish()
                }
                .addOnFailureListener {
                    Log.d("error","ini penyebab errornya ${it.localizedMessage}")

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