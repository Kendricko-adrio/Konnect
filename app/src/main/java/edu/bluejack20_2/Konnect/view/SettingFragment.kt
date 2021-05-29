package edu.bluejack20_2.Konnect.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.viewmodels.SettingViewModel
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {
    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        val appSettingPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightMode: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val sizeArr = arrayListOf("Large", "Medium", "Small")

        val adapter = context?.let { ArrayAdapter<String>(it, R.layout.support_simple_spinner_dropdown_item, sizeArr) }
        val font_size = view.findViewById<Spinner>(R.id.spn_font_size)
        font_size.adapter = adapter

        val currSize = appSettingPrefs.getString("FONT_SIZE", "Medium")

        var defSize = 1
        if(currSize == "Large"){
            defSize = 0
        }else if(currSize == "Small"){
            defSize = 2
        }

//        spn_font_size.setSelection(defSize)

        font_size.onItemSelectedListener = object :
            OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                Log.wtf("Test", sizeArr[position])
                if(position == 0){
                    sharedPrefsEdit.putString("FONT_SIZE", "Large")

                }else if(position == 1){
                    sharedPrefsEdit.putString("FONT_SIZE", "Medium")

                }else if(position == 2){
                    sharedPrefsEdit.putString("FONT_SIZE", "Small")

                }

                sharedPrefsEdit.apply()
                activity?.finish()
//                activity?.()
                startActivity(activity?.intent)
//                startActivity(Intent(context, HomeActivity::class.java))
            }

        }

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
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        return view
    }



    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}