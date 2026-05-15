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

@Composable
fun ProfileItem(
    profile: Profile,
    viewModel: ProfileViewModel = viewModel(),
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavController
    ){
    val tabs = listOf("MY POST", "LIKED POSTS")

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), 
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item(span = { GridItemSpan(2) })
        {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(Modifier.fillMaxWidth().height(200.dp).padding(8.dp), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = "${RetroFitClient.BASE_URL}images/${profile.photo}",
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "${profile.firstName} ${profile.lastName}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${profile.dateOfBirth?.substring(0, 10)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Text(
                    text = "Join at ${profile.joinDate?.substring(0, 10)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Gray
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            //Update Profile
                            navController.navigate("update_profile")
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        ),
                    ) {
                        Text("Update Profile")
                        CornerRadius(4)
                    }
                    Button(
                        onClick = {

                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        ),
                    ) {
                        Text("Add Post")
                        CornerRadius(4)
                    }
                }
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    contentColor = Color(0xFF2196F3)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { onTabSelected(index) },
                            text = {
                                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                    color = if (selectedTabIndex == index) Color(0xFF2196F3) else Color.Gray)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxWidth().height(2.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        items(viewModel.postList) { post ->
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = "${RetroFitClient.BASE_URL}images/${post.thumbnail}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable{
                        navController.navigate("detail_screen/${post.id}")
                    },
                    placeholder = painterResource(R.drawable.ic_launcher_foreground)
                )
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

