package edu.bluejack20_2.Konnect.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.ChatRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.view.ChatDetailActivity

class UserListAdapter(var connectionList: MutableList<DocumentReference>) :
    RecyclerView.Adapter<UserListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_template, parent, false)
        return UserListHolder(view)
    }

    override fun getItemCount(): Int {
        return connectionList.size
    }

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {
        val connection = connectionList[position]
        connection.get().addOnSuccessListener { it ->
            holder.name.text = it["name"].toString()
            Glide.with(holder.itemView).load(it["photoUrl"].toString())
                .apply(RequestOptions().override(150, 150)).into(holder.photo)
            holder.itemView.setOnClickListener { view ->
                UserRepository.getUserByDoc(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener {fbUser ->
                    ChatRepository.createChatRoom(connection).get().addOnSuccessListener { data ->
                        val chatModel = ChatDetail()
                        val temp = data.size()
                        val currUser = FirebaseAuth.getInstance().currentUser.uid
                        val currReference = UserRepository.getUserByDoc(currUser)

                        Log.wtf("data chat room", temp.toString())
                        for (doc in data) {
                            val array = doc["members"] as List<DocumentReference>
                            if ((array[0] == currReference && array[1] == connection) || (array[1] == currReference && array[0] == connection)) {
                                chatModel.connectionDoc = connection.id
                                chatModel.connectionName = it["name"].toString()
                                chatModel.chatRoomDoc = doc.id
                                chatModel.photoURL = it["photoUrl"].toString()
                                chatModel.fromPhotoUrl = fbUser["photoUrl"].toString()
                                var chatDetail =
                                    Intent(holder.itemView.context, ChatDetailActivity::class.java)
                                chatDetail.putExtra("chat_detail", chatModel)
                                holder.itemView.context.startActivity(chatDetail)
                                return@addOnSuccessListener
                            }
                        }


                        val reference = UserRepository.getUserByDoc(currUser)
                        val data = hashMapOf(
                            "members" to listOf(reference, connection)
                        )
                        FirebaseFirestore.getInstance().collection("chat_rooms").add(data)
                            .addOnSuccessListener { docRoom ->
                                chatModel.chatRoomDoc = docRoom.id
                                chatModel.connectionDoc = connection.id
                                chatModel.connectionName = it["name"].toString()
                                chatModel.photoURL = it["photoUrl"].toString()
                                chatModel.fromPhotoUrl = fbUser["photoUrl"].toString()
                                var chatDetail =
                                    Intent(holder.itemView.context, ChatDetailActivity::class.java)
                                chatDetail.putExtra("chat_detail", chatModel)
                                holder.itemView.context.startActivity(chatDetail)
                            }

                    }
                }
            }
        }
    }
}

class UserListHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var photo: ImageView
    lateinit var name: TextView

    init {
        photo = view.findViewById(R.id.iv_Connection_pp)
        name = view.findViewById(R.id.tv_connection_name)
    }
}