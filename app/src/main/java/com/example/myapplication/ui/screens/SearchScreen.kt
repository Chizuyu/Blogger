package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Post
import com.example.myapplication.ui.viewModel.PostViewModel

@Composable
fun SearchScreen(navController: NavController, viewModel: PostViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.onSearch(it) },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text("Search post title or content...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (viewModel.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearch("") }) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (viewModel.postList.isEmpty() && viewModel.searchQuery.isNotEmpty()) {
                Text(
                    text = "No posts found for '${viewModel.searchQuery}'",
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = spacedBy(8.dp),
                verticalArrangement = spacedBy(8.dp)
            ) {
                items(viewModel.postList) { post ->
                    PostCard(
                        post = post,
                        onClick = {
                            navController.navigate("detail_screen/${post.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PostCard(post: Post, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            AsyncImage(
                model = "${RetroFitClient.BASE_URL}uploads/thumbnails/${post.thumbnail}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            Text(
                text = post.title,
                modifier = Modifier.padding(8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}