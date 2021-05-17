package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.User
import kotlinx.coroutines.tasks.await

object UserRepository {

    private const val TAG = "USER_REPO"

    private val db = FirebaseFirestore.getInstance()

    public fun getUserByDoc(user: String): Task<DocumentSnapshot>{
        return db.collection("users").document(user).get()
    }

    public fun createUserFirebase(user: FirebaseUser) {
        val findDoc = db.collection("users").document(user.uid)
        findDoc.get()
                .addOnSuccessListener { document ->
                    if(!document.exists()){
                        // Create new user
                        val data = hashMapOf(
                                "name" to user.displayName,
                                "email" to user.email,
                                "phoneNumber" to user.phoneNumber,
                                "providerId" to user.providerId,
                                "photoUrl" to user.photoUrl.toString()
                        )
                        db.collection("users").document(user.uid).set(data)
                    }else{
                        // User already exists
                        Log.wtf("test", "DocumentSnapshot data: ${document.get("name")}")
                    }
                }

    }

    public suspend fun getCurrentUser(): User? {
        // Get the current user authentication
        val email = FirebaseAuth.getInstance().currentUser?.email

        if(email != null) {
            val userRef = db.collection("users")
            val query = userRef.whereEqualTo("email", email).get().await()
            val user: User
            try {
                user = query.documents.first().toObject(User::class.java)!!
                user.id = query.documents.first().id
                return user
            }catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
        }
        return null
    }

    public suspend fun getUserByDocument(id: String): User? {
        // We need to load several references from the user document
        // City : Reference
        // Experiences : Array<Reference>

        val userRef = db.collection("users").document(id)
        val query = userRef.get().await()
        val user: User
        try {
            // City
            val cityRef = query["city_ref"] as DocumentReference
            val city = CityRepository.getCityUser(cityRef)

            // Experiences
            val experiencesRef = query["experiences_ref"] as MutableList<DocumentReference>
            Log.wtf(TAG, experiencesRef.toString())

            user = query.toObject(User::class.java)!!
            val cityObj = city.toObject(City::class.java)

            if(cityObj != null) {
                user.city = cityObj
            }

            return user
        }catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return null
    }
}