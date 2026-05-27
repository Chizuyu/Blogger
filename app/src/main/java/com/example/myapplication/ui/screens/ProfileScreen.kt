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
    user: User,
    postList: List<com.example.myapplication.model.Post>,
    isOwnProfile: Boolean,
    isLoading: Boolean,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavController,
    onDeletePost: (com.example.myapplication.model.Post) -> Unit = {}
) {
    val tabs = if (isOwnProfile) listOf("MY POST", "LIKED POSTS") else listOf("POSTS")

    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        item(span = { GridItemSpan(2) }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Header Profil (Foto, Nama, Join Date)
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Box(Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = "${RetroFitClient.BASE_URL}uploads/users/${user.photo}",
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${user.dateOfBirth?.substring(0, 10)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Text(
                    text = "Join at ${user.joinDate?.substring(0, 10)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Gray
                )

                // TOMBOL AKSI: Hanya muncul jika profil milik sendiri
                if (isOwnProfile) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { navController.navigate("update_profile") }, modifier = Modifier.weight(1f)) {
                            Text("Update Profile")
                        }
                        Button(onClick = { navController.navigate("create_post") }, modifier = Modifier.weight(1f)) {
                            Text("Add Post")
                        }
                    }
                } else {
                    Spacer(Modifier.height(16.dp))
                }

                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(selected = selectedTabIndex == index, onClick = { onTabSelected(index) },
                            text = { Text(title) })
                    }
                }
            }
        }
        // Grid Postingan
        items(postList) { post ->
            Box {
                AsyncImage(
                    model = "${RetroFitClient.BASE_URL}uploads/thumbnails/${post.thumbnail}",
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(1f).clickable {
                        navController.navigate("detail_screen/${post.id}")
                    },
                    contentScale = ContentScale.Crop
                )
                // Tombol Edit/Delete: Hanya jika isOwnProfile & Tab pertama
                if (isOwnProfile && selectedTabIndex == 0) {
                    // ... (Tetap gunakan IconButton Edit & Delete yang lama)
                }
            }
        }
    }
}
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel(), navController: NavHostController) {
    val profile = viewModel.profileData
    val isLoading = viewModel.isLoading

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
        viewModel.fetchMyPost() // Default tab pertama
    }

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 0) {
            viewModel.fetchMyPost()
        } else {
            viewModel.fetchLikedPost()
        }
    }

    if (isLoading && profile == null) { // Loading hanya jika profil belum ada
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        profile?.let {
            ProfileItem(
                profile = it,
                viewModel = viewModel,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { newIndex -> selectedTabIndex = newIndex },
                navController = navController
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

