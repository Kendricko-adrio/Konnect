package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.Institution
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.Experience
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
        var admins: MutableList<User> = mutableListOf()

        try {
            inst = query.toObject(Institution::class.java)!!
            inst.id = query.id

            // City
            val city = loadCity(query)

            // Admins
            admins = loadAdmins(query)

            inst.city = city
            inst.admins = admins

            return inst
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return Institution()
    }

    private suspend fun loadCity(query: DocumentSnapshot): City {
        if(query["city_ref"] == null) {
            return City()
        }
        val cityRef = query["city_ref"] as DocumentReference
        val city = CityRepository.getCityDocRef(cityRef)
        val cityObj = city.toObject(City::class.java)!!
        return cityObj
    }

    private suspend fun loadAdmins(query: DocumentSnapshot): MutableList<User> {
        val admins = mutableListOf<User>()
        Log.wtf(TAG, query["admins_ref"].toString())
        if(query["admins_ref"] != null) {
            return admins
        }
        val adminsRef = query["admins_ref"] as List<DocumentReference>

        for(adminRef in adminsRef) {
            val admin = UserRepository.getUserDocRef(adminRef)
            val adminObj = admin.toObject(User::class.java)

            if(adminObj != null) {
                adminObj.id = admin.id
                admins.add(adminObj)
            }
        }

        return admins
    }

    suspend fun loadRelations(instId: String): MutableList<User> {
        val users: MutableList<User> = mutableListOf()
        val usersRef = db.collection("users").get().await()
        for(userRef in usersRef) {
            var u = userRef.toObject(User::class.java)
            val experiences: MutableList<Experience> = mutableListOf()
            // Take the user experience
            if(userRef["experiences_ref"] != null) {
                val expRefs = userRef["experiences_ref"] as List<DocumentReference>
                for(expRef in expRefs) {
                    val exp = expRef.get().await()
                    val expObj = ExperienceRepository.getExperienceById(exp.id)
                    experiences.add(expObj)
                }
            }
            if(isExists(instId, experiences)) {
                u.experiences = getTargetInstitutionExperience(instId, experiences)
                users.add(u)
            }
        }
        return users
    }

    private fun isExists(institutionId: String, experiences: MutableList<Experience>): Boolean {
        for(experience in experiences) {
            if(experience.institution.id == institutionId) {
                return true
            }
        }
        return false
    }

    private fun getTargetInstitutionExperience(institutionId: String, experiences: MutableList<Experience>): MutableList<Experience> {
        val experiences: MutableList<Experience> = mutableListOf()
        for(experience in experiences) {
            if(experience.institution.id == institutionId) {
                experiences.add(experience)
                return experiences
            }
        }
        return experiences
    }

    suspend fun updateInstitution(institution: Institution) {
        db.collection("institutions").document(institution.id).update(
            "name", institution.name,
            "photoUrl", institution.photoUrl,
            "summary", institution.summary,
            "city_ref", db.document("/cities/" + institution.city.id)
        ).await()
    }

}