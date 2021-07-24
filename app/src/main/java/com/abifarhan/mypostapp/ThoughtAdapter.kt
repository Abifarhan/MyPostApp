package com.abifarhan.mypostapp

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.mypostapp.Constanst.NUM_LIKES
import com.abifarhan.mypostapp.Constanst.THOUGHTS
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ThoughtAdapter(val thoughts: ArrayList<Thought>): RecyclerView.Adapter<ThoughtAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val username = itemView?.findViewById<TextView>(R.id.listViewNameTxt)
        val timestamp = itemView?.findViewById<TextView>(R.id.listViewDateTxt)
        val thoughtTxt = itemView?.findViewById<TextView>(R.id.listViewThoughtTxt)
        val numLikes = itemView?.findViewById<TextView>(R.id.listViewlNumLikesTxt)
        val likesImage = itemView?.findViewById<ImageView>(R.id.listViewLikeImage)

        fun bindThought(thought: Thought) {
            username?.text = thought.username
            thoughtTxt?.text = thought.thoughtTxt
            numLikes?.text = thought.numLikes.toString()

            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(thought.timestamp)
            timestamp?.text = dateString

            likesImage?.setOnClickListener {
                FirebaseFirestore.getInstance().collection(THOUGHTS)
                    .document(thought.documentId)
                    .update(NUM_LIKES, thought.numLikes + 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.thought_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindThought(thoughts[position])
    }

    override fun getItemCount(): Int {
        return thoughts.count()
    }




}