package com.example.mytunes.ui.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.mytunes.MyTunesApplication

class SplashViewModel(application: Application): ViewModel() {

    private val sharedPref: SharedPreferences = application.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("Languages", Context.MODE_PRIVATE)

    var name = sharedPref.getString("name","").toString()
    var lang = sharedPreferences.getString("languages", "").toString()

    fun onStart(openAndPopUp: (String, String) -> Unit) {
        if (name.isNotEmpty() && lang.isNotEmpty()) openAndPopUp("home","splashScreen")
        else if (name.isNotEmpty() && lang.isEmpty()) openAndPopUp("getLanguages","splashScreen")
        else openAndPopUp("getName", "splashScreen")
    }
}