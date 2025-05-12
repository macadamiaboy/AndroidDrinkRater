package com.example.drinkrater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.drinkrater.SharedPreferencesUtil.getToken
import com.example.drinkrater.ui.theme.DrinkRaterTheme
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class ReviewCreationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrinkRaterTheme {
                ReviewForm()
            }
        }
    }

    @Composable
    fun ReviewForm() {
        var name by remember { mutableStateOf("") }
        var rating by remember { mutableStateOf(0) }
        var price by remember { mutableStateOf(0) }
        var description by remember { mutableStateOf("") }
        var producer by remember { mutableStateOf("") }
        var abv by remember { mutableStateOf(0f) }
        //var userId by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = name, onValueChange = { name = it }, label = { Text("Название") })
            TextField(value = rating.toString(), onValueChange = { rating = it.toIntOrNull() ?: 0 }, label = { Text("Рейтинг (0-10)") })
            TextField(value = price.toString(), onValueChange = { price = it.toIntOrNull() ?: 0 }, label = { Text("Цена") })
            TextField(value = description, onValueChange = { description = it }, label = { Text("Описание") }, modifier = Modifier.width(280.dp), maxLines = Int.MAX_VALUE, singleLine = false)
            TextField(value = producer, onValueChange = { producer = it }, label = { Text("Производитель") })
            TextField(value = abv.toString(), onValueChange = { abv = it.toFloatOrNull() ?: 0f }, label = { Text("ABV") })
            //TextField(value = userId.toString(), onValueChange = { userId = it.toIntOrNull() ?: 0 }, label = { Text("ID пользователя") })

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val review = Review(null, name, rating, price, description, producer, abv)
                val intent = Intent(this@ReviewCreationActivity, ReviewListActivity::class.java)
                submitReview(review)
                startActivity(intent)
                finish()
            },
                modifier = Modifier
                    .height(56.dp)
                    .padding(top = 16.dp)
            ) {
                Text("Сохранить", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    private fun submitReview(review: Review) {
        val authToken = getToken(this)

        if (authToken != null) {
            ApiClient.apiService.postData(authToken, review).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ReviewCreationActivity, "Отзыв успешно сохранен", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ReviewCreationActivity, "Ошибка при сохранении отзыва", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ReviewCreationActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.e("ReviewListActivity", "Error: you're bot authorized. Cannot fetch reviews.")
            Toast.makeText(this, "Вы не авторизованы. Пожалуйста, войдите в систему.", Toast.LENGTH_SHORT).show()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewCreationActivity() {
        DrinkRaterTheme {
            ReviewForm()
        }
    }
}