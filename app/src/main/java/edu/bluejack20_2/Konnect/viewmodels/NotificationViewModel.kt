package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.models.Notification
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.UserRepository
import java.util.*

class NotificationViewModel : ViewModel() {
    private val TAG = "NOTIF_VM"

    private var notificationList = MutableLiveData<MutableList<Notification>>()

    fun getUserNotification(id: String) {
        UserRepository.getUserNotification(id)
    }

    suspend fun getCurrentUser(): User {
        val user = UserRepository.getCurrentUser()
        if(user != null) {
            return user
        }
        return User()
    }

    fun getNotificationList(): MutableLiveData<MutableList<Notification>> {
        return notificationList
    }

    fun listenToNewNotification(id: String) {
        UserRepository.getUserNotification(id).addSnapshotListener { value, error ->
            if(error != null) {
                Log.w("Error", "Listen failed", error)
                return@addSnapshotListener
            }
            if(value != null) {
                notificationList.value = mutableListOf<Notification>()
                val temp: MutableList<Notification> = mutableListOf()
                 for(data in value) {
                     var notif = data.toObject(Notification::class.java)
                     Log.wtf(TAG, data["related_user_ref"].toString())
                     UserRepository.getUserDocumentReference(data["related_user_ref"] as DocumentReference).addOnSuccessListener {
                         val relatedUser = it.toObject(User::class.java)
                         if(relatedUser != null) {
                             relatedUser.id = it.id
                            notif.relatedUser = relatedUser
                         }
                         temp.add(notif)
                         notificationList.postValue(temp)
                     }
                 }
//                notificationList.value = temp
            }
        }
    }

}