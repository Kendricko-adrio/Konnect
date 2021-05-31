package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.Institution
import kotlinx.coroutines.tasks.await

object CityRepository {

    private var db = FirebaseFirestore.getInstance()
    val TAG = "CITY_REPOSITORY"

    // Get all cities data from firebase
    suspend fun getAll(): QuerySnapshot {
        return db.collection("cities").get().await()
    }

    suspend fun getCityDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun getAllCities(): MutableList<City> {
        var cities: MutableList<City> = mutableListOf()
        val ref = db.collection("cities").get().await()
        for(doc in ref.documents) {
            var city = doc.toObject(City::class.java)
            if (city != null) {
                city.id = doc.id
                cities.add(city)
            }
        }
        return cities
    }
}