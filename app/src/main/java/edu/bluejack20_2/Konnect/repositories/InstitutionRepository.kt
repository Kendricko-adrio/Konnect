package edu.bluejack20_2.Konnect.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

object InstitutionRepository {
    private val TAG = "INSTITUTION_REPO"
    private val db = FirebaseFirestore.getInstance()
    suspend fun getInstitutionDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    fun getAllInstitution(): Task<QuerySnapshot> {
        return db.collection("institutions").get()
    }

}