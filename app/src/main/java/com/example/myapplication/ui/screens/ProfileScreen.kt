package com.example.myapplication.ui.screens

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Profile
import com.example.myapplication.model.User
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewModel.ProfileViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProfileLayout(
    firstName: String?,
    lastName: String?,
    photo: String?,
    joinDate: String?,
    dateOfBirth: String?,
    postList: List<com.example.myapplication.model.Post>,
    isOwnProfile: Boolean,
    isLoading: Boolean,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavController,
    onDeletePost: (com.example.myapplication.model.Post) -> Unit = {},
    followersCount: Int = 0,
    followingCount: Int = 0,
    actionButton: @Composable (() -> Unit)? = null,
) {
    val tabs = if (isOwnProfile) listOf("MY POST", "LIKED POSTS") else listOf("POSTS")
    var showDeleteDialog by remember { mutableStateOf(false) }
    var postToDelete by remember { mutableStateOf<com.example.myapplication.model.Post?>(null) }

    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        item(span = { GridItemSpan(2) }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = RoundedCornerShape(12.dp)) {
                    Box(Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = "${RetroFitClient.BASE_URL}uploads/users/$photo",
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Text(
                    text = "$firstName $lastName",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${dateOfBirth?.take(10)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Join at ${joinDate?.take(10)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$followersCount", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Followers", fontSize = 12.sp, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$followingCount", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Following", fontSize = 12.sp, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${postList.size}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Posts", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                if (isOwnProfile) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { navController.navigate("update_profile") }, modifier = Modifier.weight(1f)) { Text("Update Profile") }
                        Button(onClick = { navController.navigate("create_post") }, modifier = Modifier.weight(1f)) { Text("Add Post") }
                    }
                } else {
                    Box(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        actionButton?.invoke()
                    }
                }

                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(selected = selectedTabIndex == index, onClick = { onTabSelected(index) }, text = { Text(title) })
                    }
                }
            }
        }

        items(postList) { post ->
            Card(modifier = Modifier.padding(8.dp).aspectRatio(1f)) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = "${RetroFitClient.BASE_URL}uploads/thumbnails/${post.thumbnail}",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clickable { navController.navigate("detail_screen/${post.id}") }
                    )

                    // ICON EDIT/DELETE HANYA MUNCUL JIKA MILIK SENDIRI
                    if (isOwnProfile && selectedTabIndex == 0) {
                        Row(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)) {
                            IconButton(onClick = { navController.navigate("edit_post/${post.id}") }, modifier = Modifier.size(32.dp)) {
                                Card(colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))) {
                                    Icon(Icons.Default.Edit, null, tint = Color(0xFF2196F3), modifier = Modifier.padding(4.dp).size(20.dp))
                                }
                            }
                            IconButton(onClick = { postToDelete = post; showDeleteDialog = true }, modifier = Modifier.size(32.dp)) {
                                Card(colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))) {
                                    Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.padding(4.dp).size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Postingan?") },
            confirmButton = {
                TextButton(onClick = { postToDelete?.let { onDeletePost(it) }; showDeleteDialog = false }) { Text("Hapus", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") } }
        )
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel(), navController: NavHostController) {
    val profile = viewModel.profileData
    val isLoading = viewModel.isLoading
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
        viewModel.fetchMyPost()
    }

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 0) viewModel.fetchMyPost() else viewModel.fetchLikedPost()
    }

    if (isLoading && profile == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    } else {
        profile?.let {
            ProfileLayout(
                firstName = it.firstName,
                lastName = it.lastName,
                photo = it.photo,
                joinDate = it.joinDate,
                dateOfBirth = it.dateOfBirth,
                postList = viewModel.postList,
                isOwnProfile = true,
                isLoading = isLoading,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { newIndex -> selectedTabIndex = newIndex },
                navController = navController,
                onDeletePost = { post -> viewModel.deletePostById(post.id) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScreenProfile() {
    val dummyProfile = Profile(
        firstName = "Saiful",
        lastName = "Hafiz",
        dateOfBirth = "14 Sep 2003",
        joinDate = "02-Jun-2024",
        photo = ""
    )

    MyApplicationTheme {
        ProfileScreen(navController = rememberNavController())
    }
}

