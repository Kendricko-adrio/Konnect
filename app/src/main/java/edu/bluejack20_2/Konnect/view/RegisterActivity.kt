package edu.bluejack20_2.Konnect.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.base.BaseActivity
import edu.bluejack20_2.Konnect.models.RegisterData
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    val emailPatter =
        "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

    override fun onCreate(savedInstanceState: Bundle?) {
        changeFont()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        btn_register.setOnClickListener {
            val email = et_register_email.text.toString()
            val password = et_register_password.text.toString()
            val name = et_register_name.text.toString()

            if (email == "" || password == "" || name == "") {
                Toast.makeText(applicationContext, "Field must be filled with value!", Toast.LENGTH_SHORT)
                    .show()

            } else if (!email.matches(emailPatter.toRegex())) {
                Toast.makeText(applicationContext, "Invalid Email Format!", Toast.LENGTH_SHORT)
                    .show()

            } else if (password.length < 6) {
                Toast.makeText(applicationContext, "Password must be more than 6 characters!", Toast.LENGTH_SHORT)
                    .show()
            }else{
                Log.wtf("asdfasdf", "masuk")
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
}