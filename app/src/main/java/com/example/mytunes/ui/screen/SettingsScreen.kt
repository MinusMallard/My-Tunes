package com.example.mytunes.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit,
    changeName: (String) -> Unit
) {
    var newName by rememberSaveable {
        mutableStateOf("")
    }
    var visible by rememberSaveable {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navigateTo("getLanguages") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Change Language")
            }
            AnimatedVisibility(visible = !visible) {
                Button(
                    onClick = {
                        visible = !visible
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(8.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Change Name")
                }
            }
            AnimatedVisibility(visible = visible) {
                Column {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { 
                            newName = it
                        },

                        modifier = Modifier
                            .fillMaxWidth()

                            .padding(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                        shape = RoundedCornerShape(18.dp),
                        maxLines = 1,
                        singleLine = true,
                        label = { Text(text = "Change Name")},

                    )
                    Row {
                        Button(
                            onClick = { changeName(newName) },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(0.5f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = "save")
                        }
                        Button(onClick = {
                            visible = !visible
                            newName = ""
                        },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(0.5f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = "cancel")
                        }
                    }
                }
            }
        }
    }
}

