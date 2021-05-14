package edu.bluejack20_2.Konnect.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.ChatTextAdapter
import edu.bluejack20_2.Konnect.models.Chat
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.ChatRepository
import edu.bluejack20_2.Konnect.viewmodels.ChatDetailViewModel
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class ChatDetailActivity : AppCompatActivity() {

    fun sendButton() {

    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        var viewModel = ViewModelProvider(this).get(ChatDetailViewModel::class.java)


        var chatDetail = intent.getParcelableExtra<ChatDetail>("chat_detail")
        if (chatDetail != null) {
            setTitle(chatDetail.connectionName)
        }
        var recycle = findViewById<RecyclerView>(R.id.rv_chat)
        var linearLayout = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        linearLayout.stackFromEnd = true
        recycle.layoutManager = linearLayout
        recycle.setHasFixedSize(true)


        lifecycleScope.launch {


        }
        viewModel.listenToNewChat(chatDetail?.chatRoomDoc as String)

        viewModel.getListChat().observe(this, Observer {
            val adapter = ChatTextAdapter(it)
            recycle.adapter = adapter
        })
//        if (chatDetail != null) {
//            Log.wtf("data saat masuk ke chat detail", "kesini ga lu")
//            ChatRepository.listenChatText(chatDetail.chatRoomDoc as String).addSnapshotListener{ documentSnapshot, e ->
//                if (e != null) {
//                    Log.w("error", "Listen failed.", e)
//                    return@addSnapshotListener
//                }
//                val chatArr = ArrayList<Chat>()
////                Log.wtf("ada ga datanya? " , documentSnapshot.isEmpty.toString())
//                if (documentSnapshot != null) {
//                    for(data in documentSnapshot){
//                        Log.wtf("data saat masuk ke chat detail", data.toString())
//                        val chat = Chat()
//                        chat.from = data["from"] as DocumentReference
//                        chat.created_at = data["created_at"] as Timestamp
//                        chat.to = data["to"] as DocumentReference
//                        chat.text = data["text"].toString()
//                        chatArr += chat
//                    }
//                }
//                val adapter = ChatTextAdapter(chatArr)
//                recycle.adapter = adapter
//            }
//        }


        var btn = findViewById<ImageButton>(R.id.btn_send)
        var msg = findViewById<EditText>(R.id.et_user_text)

        btn.setOnClickListener(View.OnClickListener {
            val userRef = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser.uid)
            val toRef = FirebaseFirestore.getInstance().collection("users")
                .document(chatDetail?.connectionDoc as String)
            val headerRef = FirebaseFirestore.getInstance().collection("chat_rooms")
                .document(chatDetail?.chatRoomDoc.toString())

            val map = hashMapOf(
                "from" to userRef,
                "text" to msg.text.toString(),
                "created_at" to Timestamp.now(),
                "to" to toRef,
                "header" to headerRef
            )

            ChatRepository.setChatText(map)
            msg.setText("")
//                FirebaseFirestore.getInstance().collection("chat_rooms").document(chatDetail.chatRoomDoc as String)
//                    .update("messages", {
//                        "ricko" to FieldValue.arrayUnion(),
//                    } )
        })
    }
}