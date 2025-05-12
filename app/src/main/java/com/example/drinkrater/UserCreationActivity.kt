package com.example.drinkrater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.drinkrater.SharedPreferencesUtil.getToken
import com.example.drinkrater.SharedPreferencesUtil.saveToken
import com.example.drinkrater.ui.theme.DrinkRaterTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserCreationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrinkRaterTheme {
                UserCreationForm()
            }
        }
    }

    @Composable
    fun UserCreationForm() {
        var name by remember { mutableStateOf("") }
        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var second_password_attempt by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ваше имя") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Ваш логин") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Придумайте пароль") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = second_password_attempt,
                onValueChange = { second_password_attempt = it },
                label = { Text("Повторите пароль пароль") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (password == second_password_attempt) {
                    val user = User(null, login, name, password)
                    submitUser(user)
                } else {
                    Toast.makeText(
                        this@UserCreationActivity,
                        "Пароли не совпадают, повторите попытку",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
                modifier = Modifier
                    .height(56.dp)
                    .padding(top = 16.dp)
            ) {
                Text("Создать пользователя", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    private fun submitUser(user: User) {
        val userRequest = UserRequest(user)
        ApiClient.apiService.postUser(userRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UserCreationActivity, "Пользователь создан", Toast.LENGTH_SHORT).show()

                    val token = response.body()?.token
                    saveToken(this@UserCreationActivity, token)

                    val intent = Intent(this@UserCreationActivity, ReviewListActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@UserCreationActivity, "Ошибка при сохранении данных", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(this@UserCreationActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewCreationActivity() {
        DrinkRaterTheme {
            UserCreationForm()
        }
    }
}