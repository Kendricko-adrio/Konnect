package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object SkillRepository {
    private var db = FirebaseFirestore.getInstance()
    val TAG = "SKILL_REPO"

    suspend fun getSkillDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }
}