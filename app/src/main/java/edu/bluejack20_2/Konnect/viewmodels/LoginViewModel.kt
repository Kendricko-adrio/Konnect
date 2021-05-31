package edu.bluejack20_2.Konnect.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.Konnect.repositories.UserRepository
import kotlinx.android.synthetic.main.activity_phone_o_t_p.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class LoginViewModel: ViewModel() {

    private var isUserInDb: MutableLiveData<String?> = MutableLiveData(null)


    fun getUserStatus(): MutableLiveData<String?>{
        return isUserInDb
    }

    fun checkUser(email: String, password: String, context: Context){
        UserRepository.getUserByUsernameAndPassword(email, password).get().addOnSuccessListener {
            Log.wtf("data", it.toString())
            for (data in it){
                if(data == null){

                    isUserInDb.value = null

                }else{
                    isUserInDb.value = data["phoneNumber"].toString()

                }
            }
        }.addOnFailureListener{
            Log.wtf("error", it.toString())
        }
    }


}