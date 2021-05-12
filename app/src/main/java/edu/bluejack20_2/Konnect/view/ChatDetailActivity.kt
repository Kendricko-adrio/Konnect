package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ChatDetail

class ChatDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        var chatDetail = intent.getParcelableExtra<ChatDetail>("chat_detail")
        if (chatDetail != null) {
            setTitle(chatDetail.connectionName)
        }
    }
}