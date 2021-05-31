package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.Institution
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.User
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
    fun getAllInstitution(): Task<QuerySnapshot> {
        return db.collection("institutions").get()
    }

    suspend fun getInstitutionById(id: String): Institution? {
        val instRef = db.collection("institutions").document(id)
        val query = instRef.get().await()
        val inst: Institution
        val relations: MutableList<User> = mutableListOf<User>()

        try {
            inst = query.toObject(Institution::class.java)!!
            inst.id = query.id

            // City
            val city = loadCity(query)

            inst.city = city

            return inst
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return Institution()
    }

    suspend fun loadCity(query: DocumentSnapshot): City {
        if(query["city_ref"] == null) {
            return City()
        }
        val cityRef = query["city_ref"] as DocumentReference
        val city = CityRepository.getCityDocRef(cityRef)
        val cityObj = city.toObject(City::class.java)!!
        return cityObj
    }

    suspend fun loadRelations(instId: String): MutableList<User> {
        val usersRef = db.collection("users").get().await()
        for(userRef in usersRef) {
            var u = userRef.toObject(User::class.java)
            // Take the user experience
            Log.wtf(TAG, userRef["connections_ref"].toString())
        }
        return mutableListOf()
    }

}