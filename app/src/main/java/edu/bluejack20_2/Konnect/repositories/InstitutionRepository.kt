package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.Institution
import kotlinx.coroutines.tasks.await

object InstitutionRepository {
    private val TAG = "INSTITUTION_REPO"
    private var db = FirebaseFirestore.getInstance()

    suspend fun getInstitutionDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun getAllInstitutions(): MutableList<Institution> {
        var insts: MutableList<Institution> = mutableListOf()
        val ref = db.collection("institutions").get().await()
        for(doc in ref.documents) {
            var inst = doc.toObject(Institution::class.java)
            if (inst != null) {
                inst.id = doc.id
                insts.add(inst)
            }
        }
        return insts
    }
}