package edu.bluejack20_2.Konnect.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var btnSignIn: SignInButton
    private lateinit var googleClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
        super.onCreate(savedInstanceState)
        val check = checkAuth()
        if(check){
            finish()
            return;
        }

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(R.layout.activity_login)
        init()
        toRegisterButton()
        toLoginButton()

        val googleSignIn = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("395354993933-5n0j46qs1rd4fccmiglv0so81vb4c14f.apps.googleusercontent.com").
                requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, googleSignIn)
        btnSignIn.setOnClickListener(View.OnClickListener {
            val intent: Intent = googleClient.signInIntent
            startActivityForResult(intent, 100)
        })
    }

    private fun toLoginButton(){
        btnLogin.setOnClickListener {
            val email = txtUsername.text.toString()
            val pass = txtPassword.text.toString()
            viewModel.checkUser(email, pass, this)
            viewModel.getUserStatus().observe(this, Observer {
                if(it != ""){
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PhoneOTPActivity::class.java)
                    intent.putExtra("phone", it)
                    startActivity(intent)
                }
            })
        }
    }

    private fun toRegisterButton(){
        btn_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun checkAuth(): Boolean{
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            goToHome(auth.currentUser)
            return true;
        }
        return false;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            val signInAccountTask :Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            if(signInAccountTask.isSuccessful){
                Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_SHORT).show()
                try {
                    val account: GoogleSignInAccount = signInAccountTask.getResult(ApiException::class.java)!!
                    Log.d("tag", "firebaseAuthWithGoogle:"+ account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e: ApiException){
                    Log.w("tag", "Google sign in failed", e)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Tag", "signInWithCredential:success")
                        val user = auth.currentUser
                        UserRepository.createUserFirebase(user)
                        goToHome(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Tag", "signInWithCredential:failure", task.exception)
//                        goToHome(null)
                    }
                }
    }

    private fun goToHome(user: FirebaseUser){
        if(user != null){

            Toast.makeText(this, "sukses", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }else{
            Toast.makeText(this, "U didnt signed in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init(){
        btnSignIn = findViewById(R.id.btnGoogle)

    }
}