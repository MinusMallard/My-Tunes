package com.example.mytunes

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytunes.ui.viewModel.HomeViewModel
import com.example.mytunes.ui.viewModel.SearchViewModel
import com.example.mytunes.ui.viewModel.SplashViewModel

object AppViewModelProvider {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    val Factory = viewModelFactory {
        initializer {
            SplashViewModel(
                this.myTunesApplication()
            )
        }
        initializer {
            HomeViewModel(
                this.myTunesApplication().container.myTunesDataRepository,
                this.myTunesApplication().container.myTunesDataRepository2,
                languages = myTunesApplication().getSharedPreferences("Languages", Context.MODE_PRIVATE).getString("languages", "")!!
            )
        }
        initializer {
            SearchViewModel(
                this.myTunesApplication().container.myTunesDataRepository
            )
        }
    }
}

fun CreationExtras.myTunesApplication(): MyTunesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyTunesApplication)