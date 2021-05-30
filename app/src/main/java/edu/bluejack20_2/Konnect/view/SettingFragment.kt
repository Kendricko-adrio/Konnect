package edu.bluejack20_2.Konnect.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.viewmodels.SettingViewModel
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {
    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModel: SettingViewModel
    private lateinit var appSettingPrefs: SharedPreferences
    private lateinit var sharedPrefsEdit: SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        appSettingPrefs = this.requireActivity().getSharedPreferences("AppSettingPrefs", 0)
        sharedPrefsEdit = appSettingPrefs.edit()
        val isNightMode: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        fontSizeSetting(view)

//        val sizeArr = arrayListOf("Large", "Medium", "Small")
//
//        val adapter = context?.let { ArrayAdapter<String>(it, R.layout.support_simple_spinner_dropdown_item, sizeArr) }
//        val font_size = view.findViewById<Spinner>(R.id.spn_font_size)
//        font_size.adapter = adapter


//        font_size.setSelection(defSize)
//
//        font_size.onItemSelectedListener = object :
//            OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                Log.wtf("Test", sizeArr[position])
//                if(position == 0){
//                    sharedPrefsEdit.putString("FONT_SIZE", "Large")
//
//                }else if(position == 1){
//                    sharedPrefsEdit.putString("FONT_SIZE", "Medium")
//
//                }else if(position == 2){
//                    sharedPrefsEdit.putString("FONT_SIZE", "Small")
//
//                }
//
//                sharedPrefsEdit.apply()
////                activity?.finish()
////                activity?.()
//
////                startActivity(Intent(context, HomeActivity::class.java))
//            }
//
//        }

        val btn_change_theme = view.findViewById<Button>(R.id.btn_change_theme)
        btn_change_theme.setOnClickListener {
            if(isNightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
            }
        }
        val btn_logout_var = view.findViewById<Button>(R.id.btn_logout)
        btn_logout_var.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val googleSignIn = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            ).requestIdToken("395354993933-5n0j46qs1rd4fccmiglv0so81vb4c14f.apps.googleusercontent.com").
            requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(requireActivity(), googleSignIn)
            googleClient.signOut().addOnSuccessListener {
                Log.wtf("sukses", "sukses")
            }

            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        return view
    }


    private fun fontSizeSetting(view: View){
        val currSize = appSettingPrefs.getString("FONT_SIZE", "Medium")

        val rbMedium = view.findViewById<RadioButton>(R.id.rb_medium)
        val rbLarge = view.findViewById<RadioButton>(R.id.rb_large)
        val rbSmall = view.findViewById<RadioButton>(R.id.rb_small)

        Log.wtf("test", currSize)

        rbMedium.isChecked = true
        if(currSize == "Large"){
            rbLarge.isChecked = true
        }else if(currSize == "Small"){
            rbSmall.isChecked = true
        }

        rbMedium.setOnClickListener {
            sharedPrefsEdit.putString("FONT_SIZE", "Medium")
            sharedPrefsEdit.apply()
            startActivity(Intent(context, HomeActivity::class.java))
        }

        rbLarge.setOnClickListener {
            sharedPrefsEdit.putString("FONT_SIZE", "Large")
            sharedPrefsEdit.apply()
            startActivity(Intent(context, HomeActivity::class.java))
        }

        rbSmall.setOnClickListener {
            sharedPrefsEdit.putString("FONT_SIZE", "Small")
            sharedPrefsEdit.apply()
            startActivity(Intent(context, HomeActivity::class.java))
        }
    }


    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}