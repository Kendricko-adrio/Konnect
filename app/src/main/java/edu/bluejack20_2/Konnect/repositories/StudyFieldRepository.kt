package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

object StudyFieldRepository {
    private val TAG = "STUDY_FIELD_REPO"

    suspend fun getStudyFieldDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }
}