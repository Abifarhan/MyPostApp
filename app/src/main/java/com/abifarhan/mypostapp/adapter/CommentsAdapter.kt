package com.abifarhan.mypostapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.mypostapp.R
import com.abifarhan.mypostapp.`interface`.CommentOptionsClickListener
import com.abifarhan.mypostapp.model.Comment
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentsAdapter(
    val comments: ArrayList<Comment>,
    val commentOptionsClickListener: CommentOptionsClickListener
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>(){

    inner class ViewHolder(
        itemView: View?,
    ) : RecyclerView.ViewHolder(itemView!!) {


        fun bindComments(comment: Comment) {

            itemView.findViewById<TextView>(R.id.textView_name_comment).text = comment.username

            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(comment.timestamp)
            itemView.findViewById<TextView>(R.id.textView_date_of_comment).text = dateString
            itemView.findViewById<TextView>(R.id.textView_comment_txt).text = comment.commentTxt
            val optionsImage =
                itemView.findViewById<ImageView>(
                    R.id.imageView_action_comment)

            optionsImage.visibility = View.INVISIBLE

            if (FirebaseAuth.getInstance().currentUser?.uid
                == comment.userId) {
                optionsImage?.visibility = View.VISIBLE
                optionsImage.setOnClickListener {
                    commentOptionsClickListener.optionsMenuClicked(comment)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.comment_list_view, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindComments(comments[position])
    }

    override fun getItemCount(): Int  = comments.size
}