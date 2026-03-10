package com.example.projectone

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var text by remember { mutableStateOf("") } //text var for TextField
            val context = LocalContext.current // Context right now
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = text,
                    onValueChange = { newText: String -> text = newText
                    },
                    label = { Text("Введите текст (номер, если хотите позвонить)") }
                )
                Button(onClick = { //no requirement for validation except not empty?

                    if (text.isNotEmpty()) {
                        val intent = Intent(context,
                            SecondActivity::class.java).apply { putExtra(
                            "message",
                            text) }
                        context.startActivity(intent)
                    }
                    else {
                        Toast.makeText(context, "Сначала введите текст", Toast.LENGTH_SHORT).show()
                    }
                    }
                        ) {
                    Text("Открыть вторую Activity")
                }
                Button(onClick = {
                    if(isValidPhoneNumber(text)){ //only russian numbers allowed
                        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$text")
                        }
                        context.startActivity(dialIntent)
                    }
                    else{
                        Toast.makeText(context, "Введите корректный номер телефона", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Позвонить другу")
                }
                Button(onClick = { //no requirement for validation except not empty?
                    if (text.isNotEmpty()) {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, text)
                        }
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Поделиться через...")
                        )
                    }
                    else {
                        Toast.makeText(context, "Нет текста, чтобы можно было им поделиться", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Поделиться в...")
                }
            }
            }
        }
    }

fun isValidPhoneNumber(number: String): Boolean {
    val regex = Regex("^(\\+7|8)\\d{10}\$")
    return regex.matches(number)
}


