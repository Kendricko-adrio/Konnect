package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object EducationRepository {
    private var db = FirebaseFirestore.getInstance()
    val TAG = "EDUCATION_REPO"

    suspend fun getEducationDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }
}