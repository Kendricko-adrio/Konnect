package edu.bluejack20_2.Konnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Story
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.GlideApp
import kotlinx.android.synthetic.main.story_item.view.*
import java.util.*
import kotlin.collections.HashMap

class StoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items: HashMap<User, MutableList<Story>> = HashMap<User, MutableList<Story>>()

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
                items.get(key)?.let { holder.bind(key, it) }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitData(storyMap: HashMap<User, MutableList<Story>>) {
        items = storyMap
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

    private fun userInfo(viewHolder: UserAdapter.ViewHolder, user: User, pos: Int) {

    }

}