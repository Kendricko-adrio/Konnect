package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.InstitutionRepository

class InstitutionProfileViewModel {

    suspend fun loadRelations(instId: String): MutableList<User> {
        return InstitutionRepository.loadRelations(instId)
    }

}