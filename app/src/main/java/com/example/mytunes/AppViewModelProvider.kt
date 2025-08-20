package com.example.mytunes

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytunes.ui.viewModel.AlbumViewModel
import com.example.mytunes.ui.viewModel.ExploreCardViewModel
import com.example.mytunes.ui.viewModel.HomeViewModel
import com.example.mytunes.ui.viewModel.PlaylistViewModel
import com.example.mytunes.ui.viewModel.SearchViewModel
import com.example.mytunes.ui.viewModel.SongPlayerViewModel
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
                this.myTunesApplication().container.myTunesDataRepository2,
                this.myTunesApplication().container.myTunesDataRepository,
                languages = myTunesApplication().getSharedPreferences("Languages", Context.MODE_PRIVATE).getString("languages", "")!!
            )
        }
        initializer {
            SearchViewModel(
                this.myTunesApplication().container.myTunesDataRepository
            )
        }
        initializer {
            ExploreCardViewModel(
                this.myTunesApplication().container.myTunesDataRepository
            )
        }

        initializer {
            AlbumViewModel(
                this.myTunesApplication().container.myTunesDataRepository
            )
        }

        initializer {
            PlaylistViewModel(this.myTunesApplication().container.myTunesDataRepository)
        }

        initializer {
            SongPlayerViewModel(this.myTunesApplication())
        }
    }
}

fun CreationExtras.myTunesApplication(): MyTunesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyTunesApplication)