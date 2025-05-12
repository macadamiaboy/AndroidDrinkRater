package com.example.drinkrater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.drinkrater.SharedPreferencesUtil.getToken
import com.example.drinkrater.ui.theme.DrinkRaterTheme
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class ReviewListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrinkRaterTheme {
                ReviewListScreen()
            }
        }
    }

    @Composable
    fun ReviewListScreen() {
        val reviews = remember { mutableStateListOf<Review>() }

        LaunchedEffect(Unit) {
            fetchReviews { fetchedReviews ->
                reviews.clear()
                reviews.addAll(fetchedReviews)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Отображение списка отзывов
            LazyColumn {
                items(reviews) { review ->
                    ReviewCard(review) {
                        val intent = Intent(this@ReviewListActivity, SingleReviewActivity::class.java)
                        intent.putExtra("REVIEW_ID", review.id)
                        startActivity(intent)
                    }
                }
            }

            // Кнопка с иконкой плюсика
            FloatingActionButton(
                onClick = {
                    // Открытие нового Activity
                    val intent = Intent(this@ReviewListActivity, ReviewCreationActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить отзыв")
            }
        }
    }

    private fun fetchReviews(onResult: (List<Review>) -> Unit) {
        Log.d("ReviewListActivity", "Fetching reviews...")
        val authToken = getToken(this)

        if (authToken != null) {
            val call = ApiClient.apiService.getReviews(authToken)
            call.enqueue(object : Callback<List<Review>> {
                override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                    Log.d("ReviewListActivity", "Response received: ${response.code()}")
                    if (response.isSuccessful) {
                        onResult(response.body() ?: emptyList())
                    } else {
                        Log.e("ReviewListActivity", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                    Log.e("ReviewListActivity", "Failed to fetch reviews: ${t.message}")
                }
            })
        } else {
            Log.e("ReviewListActivity", "Error: you're bot authorized. Cannot fetch reviews.")
            onResult(emptyList())
        }
    }

    private fun ratingView(number: Int): String = "$number / 10"

    @Composable
    fun ReviewCard(review: Review, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Text(
                        text = review.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = ratingView(review.rating),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = review.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewReviewList() {
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
            ReviewCard(reviewsToShow[0], onClick = {})
        }
    }
}