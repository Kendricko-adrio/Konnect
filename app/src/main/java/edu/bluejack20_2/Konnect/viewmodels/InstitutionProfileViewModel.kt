package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.InstitutionRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository

class InstitutionProfileViewModel {

    suspend fun loadRelations(instId: String): MutableList<User> {
        return InstitutionRepository.loadRelations(instId)
    }

    suspend fun getInstitutionById(instId: String): Institution? {
        return InstitutionRepository.getInstitutionById(instId)
    }

    suspend fun getCurrentUser(): User? {
        return UserRepository.getCurrentUser()
    }

}