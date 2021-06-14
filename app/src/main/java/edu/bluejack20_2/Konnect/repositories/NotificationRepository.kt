package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object NotificationRepository {

    private val TAG = "NOTIF_REPO"
    private val db = FirebaseFirestore.getInstance()

    suspend fun addNotification(userId: String, relatedUserId: String, type: String, postId: String) {
        val ref = db.collection("notifications").document()

        val notification = hashMapOf(
            "content" to "This is a content",
            "createdAt" to Timestamp.now(),
            "related_user_ref" to db.document("users/$relatedUserId"),
            "title" to "This is the title",
            "type" to type,
            "user_ref" to db.document("users/$userId"),
            "postId" to postId
        )

        ref.set(notification).await()
    }
}