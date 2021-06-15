package edu.bluejack20_2.Konnect.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.ChatTextAdapter
import edu.bluejack20_2.Konnect.base.BaseActivity
import edu.bluejack20_2.Konnect.models.Chat
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.ChatRepository
import edu.bluejack20_2.Konnect.viewmodels.ChatDetailViewModel
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class ChatDetailActivity : BaseActivity() {


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

        viewModel.listenToNewChat(chatDetail?.chatRoomDoc as String, chatDetail.fromPhotoUrl, chatDetail.photoURL) // sinkronus

        viewModel.getListChat().observe(this, Observer {
            val adapter = ChatTextAdapter(it)
            recycle.adapter = adapter
        })

        var btn = findViewById<ImageButton>(R.id.btn_send)
        var msg = findViewById<EditText>(R.id.et_user_text)
        Glide.with(this).load(R.drawable.send).apply(RequestOptions().override(50, 50)).into(btn)
        btn.setOnClickListener(View.OnClickListener {
            if(msg.text.toString() == ""){
                Toast.makeText(applicationContext, "Text cannot be empty", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
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
//                "created_at" to ServerValue.TIMESTAMP,
                "to" to toRef,
                "header" to headerRef
            )

            ChatRepository.setChatText(map)
            msg.setText("")

            lifecycleScope.launch {
                viewModel.addNotification(toRef.id, userRef.id, "chat", "")
            }

        })
    }
}