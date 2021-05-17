package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

object EducationDegreeRepository {
    private val TAG = "EDUCATION_DEG_REPO"

    suspend fun getEducationDegreeDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }
}