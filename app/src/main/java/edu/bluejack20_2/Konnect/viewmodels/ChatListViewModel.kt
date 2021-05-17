package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.adapters.ChatDetailAdapter
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.ChatRepository

class ChatListViewModel: ViewModel() {

    private val chatList = MutableLiveData<MutableList<ChatDetail>>()

    private val fbUser = FirebaseAuth.getInstance().currentUser

    fun getChatList(): MutableLiveData<MutableList<ChatDetail>>{
        return chatList
    }

    fun loadChatRoom(){
        val connectionList = ArrayList<ChatDetail>()
        ChatRepository.loadLastChat().addOnSuccessListener { document ->

            for (doc in document){
                val test = doc.data["members"] as List<DocumentReference>
                Log.wtf("id dari docnya ", doc.id)
                for(id in test)
                {
                    val chatDetail = ChatDetail()
                    if(id.id == fbUser.uid) {
                        continue
                    }


                    id.get().addOnSuccessListener { it ->
                        chatDetail.chatRoomDoc = doc.id
                        chatDetail.connectionName = it["name"].toString()
                        chatDetail.connectionDoc = id.id
                        chatDetail.photoURL = it["photoUrl"].toString()

                        ChatRepository.getLastMessage(doc.id).addOnSuccessListener { lastmsg ->
                            Log.wtf("apakah ada data", lastmsg.isEmpty.toString())
                            for (msg in lastmsg){
                                Log.wtf("apakah ada data", msg["text"].toString())
                                chatDetail.lastMessage = msg["text"].toString()
                            }
                            connectionList += chatDetail
                            chatList.value = connectionList
//                            val adapter = ChatDetailAdapter(connectionList)
//                            recyclerView.adapter = adapter
//                            recyclerView.setHasFixedSize(true)
                        }

                    }

                }

            }

        }
    }
}