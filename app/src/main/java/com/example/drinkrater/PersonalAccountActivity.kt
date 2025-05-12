package com.example.drinkrater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinkrater.SharedPreferencesUtil.clearToken
import com.example.drinkrater.SharedPreferencesUtil.getToken
import com.example.drinkrater.ui.theme.DrinkRaterTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrinkRaterTheme {
                PersonalAccountScreen()
            }
        }
    }

    @Composable
    fun PersonalAccountScreen() {
        val user = remember { mutableStateOf<User?>(null) }

        LaunchedEffect(Unit) {
            fetchAccount { fetchedUser ->
                user.value = fetchedUser
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {

            user.value?.let { user ->
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(text = user.name, fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Логин: ${user.login}", fontSize = 20.sp)
                    }
                }
            } ?: run {
                Text(text = "Загрузка отзыва...")
            }

            Button(
                onClick = {
                    clearToken(this@ProfileActivity)
                    val intent =
                        Intent(this@ProfileActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text("Выход")
            }
        }
    }

    private fun fetchAccount(onResult: (User?) -> Unit) {
        val authToken = getToken(this)

        if (authToken != null) {
            val call = ApiClient.apiService.getUser(authToken)
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        Log.e("PersonalAccountActivity", "Error: ${response.errorBody()?.string()}")
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("PersonalAccountActivity", "Failed to fetch account info: ${t.message}")
                    onResult(null)
                }
            })
        } else {
            Log.e("PersonalAccountActivity", "Error: you're bot authorized. Cannot fetch reviews.")
            Toast.makeText(this, "Вы не авторизованы. Пожалуйста, войдите в систему.", Toast.LENGTH_SHORT).show()
        }
    }
}