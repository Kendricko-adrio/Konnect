package edu.bluejack20_2.Konnect.view

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.RegisterData
import kotlinx.android.synthetic.main.activity_phone_o_t_p.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PhoneOTPActivity : AppCompatActivity() {

    val fbAuth = FirebaseAuth.getInstance()

    lateinit var callbacks: OnVerificationStateChangedCallbacks
    lateinit var pd: ProgressDialog
    lateinit var mVerificationId: String
    lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    lateinit var register: RegisterData
    private var isRegister: Boolean = false
    lateinit var phoneFromLogin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_o_t_p)

        // kalau register
        try {
            register = intent.getParcelableExtra<RegisterData>("user")!!
            isRegister = true
        }catch (e: Exception){
            isRegister = false
        }
        // kalau login
        try {
            phoneFromLogin = intent.getStringExtra("phone").toString()
            Log.wtf("phone from login", phoneFromLogin)
        }catch (e: Exception){

        }


        ll_otp.visibility = View.GONE
        ll_phone.visibility = View.VISIBLE

        pd = ProgressDialog(this)
        pd.setTitle("Please wait...")
        pd.setCanceledOnTouchOutside(false)

        callbacks = object :OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                pd.dismiss()
                Log.w("wrong", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                mVerificationId = p0
                forceResendingToken = p1
                pd.dismiss()

                ll_phone.visibility = View.GONE
                ll_otp.visibility = View.VISIBLE
            }

        }

        tv_resend_otp.setOnClickListener {
            val phoneNumber = et_phone_number.text.toString()
            resendVerificationCode(phoneNumber, forceResendingToken)
        }

        btn_send_otp.setOnClickListener {
            val phoneNumber = et_phone_number.text.toString()

            if(!isRegister && phoneNumber != phoneFromLogin){
                Toast.makeText(this, "Phone Number must be the same as in Database!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startPhoneNumberVerification(phoneNumber)
        }

        btn_validate_otp.setOnClickListener {
            val code = et_otp.text.toString()
            verifyOTPCode(code)
        }

    }

    private fun verifyOTPCode(code: String){
        pd.setMessage("Verifying Code")
        pd.show()

        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        pd.setMessage("Loggin in")
        fbAuth.signInWithCredential(credential).addOnSuccessListener {

            if(isRegister){
                val user = hashMapOf(
                    "name" to register.name,
                    "password" to register.password,
                    "email" to register.email,
                    "phoneNumber" to it.user.phoneNumber,
                    "photoUrl" to "https://static.thenounproject.com/png/630740-200.png"
                )

                FirebaseFirestore.getInstance().collection("users").document(it.user.uid).set(user)
            }
            pd.dismiss()
            Log.wtf("test", "masuk dan sukses")
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }.addOnFailureListener{
            pd.dismiss()
            Toast.makeText(this, "Wrong OTP Code", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resendVerificationCode(phone: String, token: PhoneAuthProvider.ForceResendingToken){
        pd.setMessage("Resend OTP")
        pd.show()
        val options = PhoneAuthOptions.newBuilder(fbAuth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun startPhoneNumberVerification(phone: String){
        pd.setMessage("Verifying Phone Number")
        pd.show()
        val options = PhoneAuthOptions.newBuilder(fbAuth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

}