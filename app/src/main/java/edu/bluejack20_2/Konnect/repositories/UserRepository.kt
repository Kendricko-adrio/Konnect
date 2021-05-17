package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.auth.User
import java.util.function.DoubleConsumer
import kotlin.coroutines.coroutineContext

object UserRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getUserByDoc(user: String): DocumentReference{
        return db.collection("users").document(user)
    }


    fun acceptConnection(user: String, userDoc: DocumentReference){
        deleteDocFromInbound(user, userDoc)
        deleteDocFromOutbound(user, userDoc)
        addToConnections(user, userDoc)
    }

    fun declineConnection(user: String, userDoc: DocumentReference) {
        deleteDocFromInbound(user, userDoc)
        deleteDocFromOutbound(user, userDoc)
    }

    fun deleteDocFromInbound(user: String, deleteDoc: DocumentReference){
        db.collection("users").document(user).update(hashMapOf<String, Any>(
            "inbound_request" to FieldValue.arrayRemove(deleteDoc)
        )).addOnCompleteListener{
            Log.wtf("berhasil ga?", "berhasil gan menghapus data di array")
        }
    }

    fun unfriendConnection(userCurr: String, userDoc: DocumentReference){
        val curr = db.collection("users").document(userCurr)
        db.collection("users").document(userCurr).update(hashMapOf<String, Any>(
            "connections_ref" to FieldValue.arrayRemove(userDoc)
        )).addOnCompleteListener{
            Log.wtf("tag", "data masuk sih")
        }
        db.collection("users").document(userDoc.id).update(hashMapOf<String, Any>(
            "connections_ref" to FieldValue.arrayRemove(curr)
        )).addOnCompleteListener{
        }
    }

    fun deleteDocFromOutbound(userDelete: String, outboundUser: DocumentReference){
        val userToDelete = db.collection("users").document(userDelete)
        db.collection("users").document(outboundUser.id).update(hashMapOf<String, Any>(
            "outbound_request" to FieldValue.arrayRemove(userToDelete)
        )).addOnCompleteListener{
            Log.wtf("berhasil ga?", "berhasil gan menghapus data di array")
        }
    }

    fun addToConnections(userCurr: String, userOutbound: DocumentReference){
        val userCurrent = db.collection("users").document(userCurr)
        db.collection("users").document(userCurr).update(hashMapOf<String, Any>(
            "connections_ref" to FieldValue.arrayUnion(userOutbound)
        ))

        db.collection("users").document(userOutbound.id).update(hashMapOf<String, Any>(
            "connections_ref" to FieldValue.arrayUnion(userCurrent)
        ))

    }

    fun createUserFirebase(user: FirebaseUser) {
        val findDoc = db.collection("users").document(user.uid)
        findDoc.get()
                .addOnSuccessListener { document ->
//                    Log.wtf("test","masuk sa = " + (document.data?.size))
                    if(!document.exists()){
//                        Log.wtf("test", "DocumentSnapshot data: ${document.get("name")}")
                        val data = hashMapOf(
                                "name" to user.displayName,
                                "email" to user.email,
                                "phoneNumber" to user.phoneNumber,
                                "providerId" to user.providerId,
                                "photoUrl" to user.photoUrl.toString()
                        )
                        db.collection("users").document(user.uid).set(data)
                    }else{
                        Log.wtf("test", "DocumentSnapshot data: ${document.get("name")}")
                    }
                }

    }
}