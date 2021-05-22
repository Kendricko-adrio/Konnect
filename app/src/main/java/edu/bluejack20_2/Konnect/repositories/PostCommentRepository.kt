package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object PostCommentRepository {

    private var db = FirebaseFirestore.getInstance()
    private val TAG = "POST_COMMENT_REPO"

    suspend fun getCommentDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

}