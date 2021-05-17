package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

object EmploymentTypeRepository {
    private val TAG = "EMPLOYMENT_REPO"

    suspend fun getEmploymentTypeDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }
}