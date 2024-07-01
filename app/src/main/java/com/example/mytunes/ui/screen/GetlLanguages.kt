package com.example.mytunes.ui.screen

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
@Composable
fun GetLanguages(
    sharedPreference: SharedPreferences,
    navigateHome:() -> Unit
) {

    val langs = mutableListOf("Hindi", "English", "Punjabi", "Tamil",
        "Telugu", "Marathi", "Gujarati", "Bengali",
        "Kannada", "Bhojpuri", "Malayalam", "Urdu",
        "Haryanvi", "Rajasthani", "Odia", "Assamese")

    val selectedLang = remember {
        mutableListOf(false,false,false,false,
            false,false,false,false,
            false,false,false,false,
            false,false,false,false)
    }

    var languages by rememberSaveable {
        mutableStateOf("")
    }

    var enabled by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .scrollable(orientation = Orientation.Vertical, state = rememberScrollState()) ,
        contentAlignment = Alignment.Center
        
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select languages",
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                items(langs) { lang ->
                    selectedLang[langs.indexOf(lang)] = langButton(selectedLang = selectedLang, index = langs.indexOf(lang), language = lang)
                    enabled = selectedLang.contains(true)
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
            TextButton(
                onClick = {
                    for (item in langs) {
                        if (selectedLang[langs.indexOf(item)]) {
                            languages += if (languages.isEmpty()) item.lowercase()
                            else ",${item.lowercase()}"
                        }
                    }
                    with (sharedPreference.edit()) {
                        putString("languages", languages)
                        apply()
                    }
                    navigateHome()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            ) {
                Text(
                    text = "Next ->",
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Composable
fun langButton(
    selectedLang: MutableList<Boolean>,
    index: Int,
    language: String,
): Boolean {
    var selected by rememberSaveable {
        mutableStateOf(false)
    }
    var buttonColor by remember {
        mutableStateOf( Color.Gray)
    }
    val color = MaterialTheme.colorScheme.primaryContainer
    Button(
        onClick = { selected = !selected
                  if (selected) {
                      buttonColor = color
                      selectedLang[index] = true

                  }
                  else {
                      buttonColor = Color.Gray
                      selectedLang[index] = false
                  }
                  },
        modifier = Modifier
            .padding(4.dp)
            .height(60.dp),
        colors = ButtonColors(containerColor = buttonColor,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray)
    ) {
        Text(
            text = language,
            fontSize = 18.sp,
            fontWeight = FontWeight.Light
        )
    }

    return selected
}