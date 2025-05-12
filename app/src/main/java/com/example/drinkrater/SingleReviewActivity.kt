package com.example.drinkrater

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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinkrater.SharedPreferencesUtil.getToken
import com.example.drinkrater.ui.theme.DrinkRaterTheme
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class SingleReviewActivity : ComponentActivity() {
    private var reviewId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reviewId = intent.getIntExtra("REVIEW_ID", -1)
        enableEdgeToEdge()
        setContent {
            DrinkRaterTheme {
                reviewId?.let { id ->
                    ReviewDetailScreen(id)
                }
            }
        }
    }

    @Composable
    fun ReviewDetailScreen(id: Int) {
        val review = remember { mutableStateOf<Review?>(null) }

        // Загрузка отзыва
        LaunchedEffect(Unit) {
            fetchReviewById(id) { fetchedReview ->
                review.value = fetchedReview
            }
        }

        review.value?.let { review ->
            Scaffold { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(text = review.name, fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Рейтинг: ${review.rating}/10", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Цена: ${review.price} руб.", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Описание: ${review.description}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Производитель: ${review.producer}", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ABV: ${review.abv}%", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ID пользователя: ${review.userId}", fontSize = 16.sp)
                }
            }
        } ?: run {
            Text(text = "Загрузка отзыва...")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReviewDetailScreen(review: Review) {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = review.name, fontSize = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Рейтинг: ${review.rating}/10", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Цена: ${review.price} руб.", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Описание: ${review.description}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Производитель: ${review.producer}", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ABV: ${review.abv}%", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ID пользователя: ${review.userId}", fontSize = 16.sp)
            }
        }
    }

    private fun fetchReviewById(id: Int, onResult: (Review?) -> Unit) {
        val authToken = getToken(this)

        if (authToken != null) {
            val call = ApiClient.apiService.getReviewById(authToken, id)
            call.enqueue(object : Callback<Review> {
                override fun onResponse(call: Call<Review>, response: Response<Review>) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        Log.e("ReviewDetailActivity", "Error: ${response.errorBody()?.string()}")
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<Review>, t: Throwable) {
                    Log.e("ReviewDetailActivity", "Failed to fetch review: ${t.message}")
                    onResult(null)
                }
            })
        } else {
            Log.e("ReviewListActivity", "Error: you're bot authorized. Cannot fetch reviews.")
            Toast.makeText(this, "Вы не авторизованы. Пожалуйста, войдите в систему.", Toast.LENGTH_SHORT).show()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewReviewCard() {
        val reviewsToShow = listOf(
            Review(
                id = 1,
                name = "Root Beer",
                rating = 5,
                price = 199,
                description = "Watery sweet but having shitty taste",
                producer = "P&W",
                abv = 0.0.toFloat(),
                userId = 123
            ),
            Review(
                id = 2,
                name = "Shrimp Gose",
                rating = 9,
                price = 199,
                description = "Spicy, delicious",
                producer = "Alaska",
                abv = 3.9.toFloat(),
                userId = 123
            ),
            Review(
                id = 3,
                name = "Imperial Stout",
                rating = 8,
                price = 199,
                description = "Intense taste, dense",
                producer = "Konig",
                abv = 12.9.toFloat(),
                userId = 123
            )
        )
        DrinkRaterTheme {
            ReviewDetailScreen(reviewsToShow[1])
        }
    }
}