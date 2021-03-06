package com.abifarhan.mypostapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.mypostapp.R
import com.abifarhan.mypostapp.`interface`.ThoughtsOptionsClickListener
import com.abifarhan.mypostapp.model.Thought
import com.abifarhan.mypostapp.utils.Constanst.NUM_COMMENTS
import com.abifarhan.mypostapp.utils.Constanst.NUM_LIKES
import com.abifarhan.mypostapp.utils.Constanst.THOUGHTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ThoughtAdapter(
    private val thoughts: ArrayList<Thought>,
    val thoughtsOptionsClickListener: ThoughtsOptionsClickListener,
    private val itemClick: (Thought) -> Unit
) : RecyclerView.Adapter<ThoughtAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View?,
                           val itemClick: (Thought) -> Unit)
        : RecyclerView.ViewHolder(itemView!!) {
        val username =
            itemView?.findViewById<TextView>(R.id.listViewNameTxt)
        private val timestamp =
            itemView?.findViewById<TextView>(R.id.listViewDateTxt)
        private val thoughtTxt =
            itemView?.findViewById<TextView>(R.id.listViewThoughtTxt)
        private val numLikes =
            itemView?.findViewById<TextView>(R.id.listViewlNumLikesTxt)
        private val likesImage =
            itemView?.findViewById<ImageView>(R.id.listViewLikeImage)
        private val numComment =
            itemView?.findViewById<TextView>(R.id.textView_comment_count)
        private val commentImage =
            itemView?.findViewById<ImageView>(R.id.imageView_comment)

        val optionsImage = itemView?.findViewById<ImageView>(
            R.id.imageView_action_for_comment
        )


        fun bindThought(thought: Thought) {
            username?.text = thought.username
            thoughtTxt?.text = thought.thoughtTxt
            numLikes?.text = thought.numLikes.toString()
            numComment?.text = thought.numComments.toString()

            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(thought.timestamp)
            timestamp?.text = dateString

            likesImage?.setOnClickListener {
                FirebaseFirestore.getInstance().collection(THOUGHTS)
                    .document(thought.documentId)
                    .update(NUM_LIKES, thought.numLikes + 1)
            }

            commentImage?.setOnClickListener {
                FirebaseFirestore.getInstance().collection(THOUGHTS)
                    .document(thought.documentId)
                    .update(NUM_COMMENTS, thought.numComments + 1)
            }

            itemView.setOnClickListener {
                itemClick(thought)
            }

            optionsImage?.visibility = View.INVISIBLE

            if (FirebaseAuth.getInstance().currentUser?.uid == thought.userId) {
                optionsImage?.visibility = View.VISIBLE
                optionsImage?.setOnClickListener {
                    thoughtsOptionsClickListener.thoughtsOptionsMenuClicked(thought)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).
            inflate(R.layout.thought_list_view,
                parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindThought(thoughts[position])
    }

    override fun getItemCount(): Int {
        return thoughts.count()
    }


}