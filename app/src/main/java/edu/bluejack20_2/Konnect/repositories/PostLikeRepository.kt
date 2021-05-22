package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object PostLikeRepository {

    private var db = FirebaseFirestore.getInstance()
    private val TAG = "POST_LIKE_REPO"

    suspend fun getLikeDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

}