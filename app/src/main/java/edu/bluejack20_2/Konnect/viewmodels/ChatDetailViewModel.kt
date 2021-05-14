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
                Log.wtf("ada ga datanya? " , "test")
                if (documentSnapshot != null) {
                    val temp = mutableListOf<Chat>()
//                    listChat.value?.clear()
//                    Log.wtf("data live", documentSnapshot.toString())
                    for(data in documentSnapshot){
//                        Log.wtf("data saat masuk ke chat detail", data.toString())
                        val chat = Chat()
                        chat.from = data["from"] as DocumentReference
                        chat.created_at = data["created_at"] as Timestamp
                        chat.to = data["to"] as DocumentReference
                        chat.text = data["text"].toString()
//                        Log.wtf("chat yang masuk", chat.text)
                        temp.add(chat)
//                        var isBerhasil = listChat.value?.add(chat)
//                        Log.wtf("chat yang masuk", isBerhasil.toString())
                    }
                    listChat.value = temp
                }
            }
    }

}