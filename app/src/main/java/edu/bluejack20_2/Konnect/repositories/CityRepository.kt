package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object CityRepository {

    private var db = FirebaseFirestore.getInstance()
    val TAG = "CITY_REPOSITORY"

    // Get all cities data from firebase
    suspend fun getAll(): List<DocumentSnapshot> {
        val snapshot = db.collection("cities").get().await()
        return snapshot.documents
    }

    fun getAllByCountryName(): CollectionReference? {
        //TODO: Implement the firebase query
        return null
    }
}