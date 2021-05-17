package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

object InstitutionRepository {
    private val TAG = "INSTITUTION_REPO"

    suspend fun getInstitutionDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }
}