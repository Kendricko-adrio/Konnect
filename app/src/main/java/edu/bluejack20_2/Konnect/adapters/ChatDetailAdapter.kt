package edu.bluejack20_2.Konnect.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.view.ChatDetailActivity


class ChatDetailAdapter(private val userChats: MutableList<ChatDetail>) : RecyclerView.Adapter<ChatDetailHolder>() {
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
        holder.lastMessage.text = user.lastMessage
        Glide.with(holder.itemView).load(user.photoURL).apply(RequestOptions().override(150, 150)).into(holder.photoURL)
        holder.itemView.setOnClickListener(View.OnClickListener {
            Toast.makeText(holder.itemView.context , "untuk orang : " + user.connectionName +
                    " dengan doc : " + user.connectionDoc, Toast.LENGTH_SHORT).show()
            UserRepository.getUserByDoc(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener {
                user.fromPhotoUrl = it["photoUrl"].toString()
                var chatDetail = Intent(holder.itemView.context, ChatDetailActivity::class.java)
                chatDetail.putExtra("chat_detail", user)
                holder.itemView.context.startActivity(chatDetail)
            }

        })
    }

}

class ChatDetailHolder(view: View): RecyclerView.ViewHolder(view){

    public lateinit var name: TextView
    public lateinit var photoURL: ImageView
    public lateinit var lastMessage: TextView
    init {
        name = view.findViewById(R.id.ivUserName)
        photoURL = view.findViewById(R.id.ivUserPhoto)
        lastMessage = view.findViewById(R.id.ivUserLastMsg)
    }
}