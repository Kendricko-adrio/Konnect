package edu.bluejack20_2.Konnect.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.RegisterData
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {
            startActivity(Intent(this, PhoneOTPActivity::class.java))
            val email = et_register_email.text.toString()
            val password = et_register_password.text.toString()
            val name = et_register_name.text.toString()
            val register = RegisterData()
            register.email = email
            register.password = password
            register.name = name

            val intent = Intent(this, PhoneOTPActivity::class.java)
            intent.putExtra("user", register)
            startActivity(intent)

        }
    }
}