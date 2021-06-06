package edu.bluejack20_2.Konnect.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Story
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.view.AddStoryActivity
import edu.bluejack20_2.Konnect.view.StoryActivity
import kotlinx.android.synthetic.main.story_item.view.*
import java.util.*
import kotlin.collections.HashMap

class StoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val TAG = "STORY_ADAPTER"
    private var items: HashMap<User, MutableList<Story>> = HashMap<User, MutableList<Story>>()
    private lateinit var userId: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.add_story_item, parent, false)
            StoryViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
            StoryViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is StoryViewHolder -> {
                val key = items.keys.toTypedArray()[position]
                items.get(key)?.let {
                    val stories = it
                    holder.bind(key, stories)
                    holder.itemView.setOnClickListener {
                        if(position == 0) {
                            // Story of the current user
                            if(stories.size > 0) {
                                // Ask the user to add a story or view story
                                val alertDialog = AlertDialog.Builder(holder.itemView.context).create()

                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "View Story",
                                object: DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        // View story
                                        val intent = Intent(holder.itemView.context, StoryActivity::class.java)
                                        intent.putExtra("userId", userId)
                                        holder.itemView.context.startActivity(intent)
                                    }
                                })

                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story",
                                object: DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        val intent = Intent(holder.itemView.context, AddStoryActivity::class.java)
                                        intent.putExtra("userId", userId)
                                        holder.itemView.context.startActivity(intent)
                                        dialog?.dismiss()
                                    }
                                })
                                alertDialog.show()
                            }
                            else {
                                // Directly go to add story page
                                val intent = Intent(holder.itemView.context, AddStoryActivity::class.java)
                                intent.putExtra("userId", userId)
                                holder.itemView.context.startActivity(intent)
                            }
                        }
                        else {
                            // View story
                            val intent = Intent(holder.itemView.context, StoryActivity::class.java)
                            intent.putExtra("userId", key.id)
                            holder.itemView.context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitData(storyMap: HashMap<User, MutableList<Story>>, userId: String) {
        items = storyMap
        this.userId = userId
    }

    class StoryViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(user: User, story: MutableList<Story>) {
            itemView.story_username.text = user.username
            GlideApp.with(itemView.context)
                .load(user.photoUrl)
                .into(itemView.story_photo)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0) {
            return 0
        }
        return 1
    }
}