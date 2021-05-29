package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object PostCommentRepository {

    private var db = FirebaseFirestore.getInstance()
    private val TAG = "POST_COMMENT_REPO"

    suspend fun getCommentDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun addPostComment(postId: String, userId: String, content: String) {
        val ref = db.collection("post_comments").document()

        val post = hashMapOf(
            "content" to content,
            "createdAt" to Timestamp.now(),
            "user_ref" to db.document("/users/$userId")
        )

        ref.set(post).await()

        val postRef = db.collection("activity_posts").document(postId)
        postRef.update("comments_ref", FieldValue.arrayUnion(db.document("/post_comments/${ref.id}")))
            .await()
    }

}