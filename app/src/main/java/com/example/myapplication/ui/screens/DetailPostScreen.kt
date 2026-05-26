package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.ui.viewModel.DetailPostViewModel

@Composable
fun DetailPostScreen(postId: String, navController: NavController, viewModel: DetailPostViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchPostDetail(postId)
    }
    val isOwnPost = viewModel.isOwnPost

    if (!isOwnPost) { // Hanya muncul jika BUKAN postingan sendiri
        IconButton(onClick = { viewModel.likePost(postId) }) {
            Icon(
                imageVector = if (viewModel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (viewModel.isLiked) Color.Red else Color.Gray
            )
        }
    } else {
        // Opsional: Tampilkan teks bahwa ini postingan anda
        Text("Ini postingan Anda", style = TextStyle(color = Color.Gray, fontSize = 12.sp))
    }

    val post = viewModel.post

    Scaffold(

    ) { innerPadding ->
        if (viewModel.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (post != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(shape = RoundedCornerShape(12.dp)) {
                    AsyncImage(
                        model = "${RetroFitClient.BASE_URL}uploads/posts/${post?.imageContent}",
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    Text(text = "${post.user.firstName} ${post.user.lastName}", fontWeight = FontWeight.Bold)
                    Text(text = post.date.split("T")[0], color = Color.Gray, fontSize = 12.sp)
                }

                Spacer(Modifier.height(16.dp))

                Text(text = post.title, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = post.category.name, color = Color.Gray, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(16.dp))

                Text(text = post.content, fontSize = 16.sp, lineHeight = 24.sp)

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "${viewModel.likeCount} Likes",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    color = Color.Gray
                )

                Spacer(Modifier.height(16.dp))

                if (!isOwnPost) {
                    Button (
                        onClick = { viewModel.likePost(post.id) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !viewModel.isLikeLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewModel.isLiked) Color(0xFFE91E63) else Color(0xFF2196F3)
                        )
                    ) {
                        if (viewModel.isLikeLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Icon(
                                imageVector = if (viewModel.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(if (viewModel.isLiked) "Unlike Post" else "Like Post")
                        }
                    }
                }
            }
        }
    }
}