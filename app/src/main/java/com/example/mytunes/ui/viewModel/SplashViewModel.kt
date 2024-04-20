package com.example.mytunes.ui.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.mytunes.MyTunesApplication

class SplashViewModel(application: Application): ViewModel() {

    private val sharedPref: SharedPreferences = application.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    var name = sharedPref.getString("name","").toString()

    fun onStart(openAndPopUp: (String, String) -> Unit) {
        if (name.isNotEmpty()) openAndPopUp("home","splashScreen")
        else openAndPopUp("getName", "splashScreen")
    }
}