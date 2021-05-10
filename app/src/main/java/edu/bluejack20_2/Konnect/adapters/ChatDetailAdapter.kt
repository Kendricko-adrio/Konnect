package edu.bluejack20_2.Konnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.common.collect.ImmutableList
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Chat
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.models.User


class ChatDetailAdapter(private val userChats: ArrayList<ChatDetail>) : RecyclerView.Adapter<ChatDetailHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatDetailHolder {
//        TODO("Not yet implemented")
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.chat_detail, parent,false)
        return ChatDetailHolder(view)
    }

    override fun getItemCount(): Int {

        return userChats.size
    }

    override fun onBindViewHolder(holder: ChatDetailHolder, position: Int) {
        val user: ChatDetail = userChats[position]
        holder.name.text = user.connectionName
    }

}

class ChatDetailHolder(view: View): RecyclerView.ViewHolder(view){

    public lateinit var name: TextView

    init {
        name = view.findViewById(R.id.ivUserName)
    }
}