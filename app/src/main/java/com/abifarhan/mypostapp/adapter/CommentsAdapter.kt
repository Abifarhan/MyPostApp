package com.abifarhan.mypostapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.mypostapp.R
import com.abifarhan.mypostapp.model.Comment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentsAdapter(
    val comments: ArrayList<Comment>,
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