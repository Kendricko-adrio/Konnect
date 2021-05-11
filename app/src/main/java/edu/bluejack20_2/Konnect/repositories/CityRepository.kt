package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object CityRepository {

    private var db = FirebaseFirestore.getInstance()
    val TAG = "CITYREPOSITORY"

    // Get all cities data from firebase
    fun getAll(): CollectionReference {
        return db.collection("cities")
    }

    fun getAllByCountryName(): CollectionReference? {
        //TODO: Implement the firebase query
        return null
    }
}