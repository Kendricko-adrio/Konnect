package edu.bluejack20_2.Konnect.base

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import edu.bluejack20_2.Konnect.R

open class BaseActivity: AppCompatActivity() {
    protected fun changeFont(){
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val isNightMode: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val fontSizePref = appSettingPrefs.getString("FONT_SIZE", "Medium")

        var themeId = R.style.FontSizeMedium
        if(fontSizePref == "Small"){
            themeId = R.style.FontSizeSmall
        }else if(fontSizePref == "Large"){
            themeId = R.style.FontSizeLarge
        }
        Log.wtf("For base activity", fontSizePref)
        setTheme(themeId)
    }
    override fun onStart() {
        super.onStart()
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val isNightMode: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val fontSizePref = appSettingPrefs.getString("FONT_SIZE", "Medium")

        var themeId = R.style.FontSizeMedium
        if(fontSizePref == "Small"){
            themeId = R.style.FontSizeSmall
        }else if(fontSizePref == "Large"){
            themeId = R.style.FontSizeLarge
        }
        Log.wtf("For base activity", fontSizePref)
        setTheme(themeId)
    }
}