package com.example.mytunes.ui.screen

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun GetName(
    sharedPref: SharedPreferences,
    modifier: Modifier = Modifier,
    navigateGetLang:() -> Unit
) {
    var newName by rememberSaveable {
        mutableStateOf("")
    }

    Box(modifier = modifier
        .zIndex(1f)
        .fillMaxSize()// Match the size of the Scaffold

        .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Can I get your name?",
                modifier = Modifier.padding(4.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = modifier.height(32.dp))
            OutlinedTextField(
                value = newName,
                onValueChange = {
                    newName = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
                singleLine = true,
                shape = RoundedCornerShape(56.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                )

            )
            Spacer(modifier = modifier.height(32.dp))
            Button(
                onClick = {
                    with(sharedPref.edit()) {
                        putString("name", newName)
                        apply()
                    }
                    navigateGetLang()
                },
                enabled = newName.isNotBlank() && newName.isNotEmpty(),
            ) {
                Text(text = "Done")
            }
        }

    }
}