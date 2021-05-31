package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.EmploymentType
import edu.bluejack20_2.Konnect.models.Experience
import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.repositories.ExperienceRepository
import edu.bluejack20_2.Konnect.repositories.InstitutionRepository

class ExperienceInputViewModel {
    suspend fun getAllEmploymentType(): MutableList<EmploymentType> {
        return ExperienceRepository.getAllEmploymentType()
    }

    suspend fun getExperienceById(id: String): Experience {
        return ExperienceRepository.getExperienceById(id)
    }

    suspend fun getAllInstitutions(): MutableList<Institution> {
        return InstitutionRepository.getAllInstitutions()
    }

    suspend fun updateExperience(experience: Experience) {
        ExperienceRepository.updateExperience(experience)
    }

    suspend fun addExperience(experience: Experience, userId: String) {
        ExperienceRepository.addExperience(experience, userId)
    }
}