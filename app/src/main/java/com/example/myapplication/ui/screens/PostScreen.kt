package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Author
import com.example.myapplication.model.Category
import com.example.myapplication.model.Post
import com.example.myapplication.model.User
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewModel.PostViewModel


@Composable
fun PostItem(post: Post, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("detail_screen/${post.id}")
            }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                AsyncImage(
                    model = "${RetroFitClient.BASE_URL}uploads/thumbnails/${post.thumbnail}",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground)
                )
            }
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${post.user.firstName} ${post.user.lastName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = post.date.split("T")[0],
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = post.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = post.category.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = post.content,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${post.likeCount} Likes",
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun DaftarPostScreen(navController: NavController, viewModel: PostViewModel = viewModel()) {
    val listPost = viewModel.postList
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }

    if(isLoading) {
        CircularProgressIndicator()
    }else{
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(listPost) {post ->
                PostItem(post = post, navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(navController: NavController) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Esemka Blogger", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    )
    { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            BottomNavGraph(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPost() {
    val dummyPost = Post(
        id = "1",
        title = "Tips Belajar Jetpack Compose untuk Pemula",
        content = "Jetpack Compose adalah toolkit modern untuk membangun UI Android asli. Ini menyederhanakan dan mempercepat pengembangan UI di Android dengan lebih sedikit kode.",
        thumbnail = "sample.jpg", // Ini tidak akan muncul gambarnya karena AsyncImage butuh internet
        date = "2024-05-20T10:00:00",
        imageContent = "",
        likeCount = 150,
        user = Author(
            firstName = "Budi",
            lastName = "Pratama",
        ),
        category = Category(
            id = "1",
            name = "Programming"
        ),
    )
    MyApplicationTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            PostItem(post = dummyPost, navController = rememberNavController())
        }
    }
}