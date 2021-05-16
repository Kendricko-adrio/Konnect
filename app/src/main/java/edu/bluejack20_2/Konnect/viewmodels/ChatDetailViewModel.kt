package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.models.Chat
import edu.bluejack20_2.Konnect.repositories.ChatRepository

class ChatDetailViewModel : ViewModel(){

    private val listChat = MutableLiveData<MutableList<Chat>>()

    fun getListChat(): LiveData<MutableList<Chat>>{
        return listChat
    }
//    suspend fun loadChat(header: String): MutableList<Chat>{
//
//        listChat.value = ChatRepository.loadChatText(header)
//        return listChat.value!!
//    }

    fun listenToNewChat(doc: String){

            ChatRepository.listenChatText(doc).addSnapshotListener{ documentSnapshot, e ->
                if (e != null) {
                    Log.w("error", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (documentSnapshot != null) {
                    val temp = mutableListOf<Chat>()
                    for(data in documentSnapshot){
                        val chat = Chat()
                        chat.from = data["from"] as DocumentReference
                        chat.created_at = data["created_at"] as Timestamp
                        chat.to = data["to"] as DocumentReference
                        chat.text = data["text"].toString()
                        temp.add(chat)
                    }
                    listChat.value = temp
                }
            }
    }

}