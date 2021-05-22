package edu.bluejack20_2.Konnect.viewmodels

import androidx.lifecycle.ViewModel
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.ActivityPostRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository

class PostDetailViewModel: ViewModel() {
    val TAG = "POST_DETAIL_VM"

    suspend fun getPostByDocument(id: String): ActivityPost {
        val post = ActivityPostRepository.getPostByDocument(id)
        if(post != null) {
            return post
        }
        return ActivityPost()
    }

    suspend fun getCurrentUser(): User {
        val user = UserRepository.getCurrentUser()
        if(user != null) {
            return user
        }
        return User()
    }
}