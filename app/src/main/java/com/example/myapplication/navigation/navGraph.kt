package com.example.myapplication.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.CreatePostScreen
import com.example.myapplication.ui.screens.DetailPostScreen
import com.example.myapplication.ui.screens.EditPostScreen
// Import halaman Anda (Sesuaikan jika nama folder/package berbeda)
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.PostItem
import com.example.myapplication.ui.screens.SearchScreen
import com.example.myapplication.ui.screens.SignInScreen
import com.example.myapplication.ui.screens.UpdateProfileScreen
import com.example.myapplication.ui.screens.UserDetailScreen
import com.example.myapplication.ui.screens.UserItem
import com.example.myapplication.ui.viewModel.PostViewModel
import com.example.myapplication.ui.viewModel.UserViewModel

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route,
        ) { backStackEntry ->
            HomeScreen(rootNavController = navController)
        }
        composable(
            route = Screen.SignIn.route
        ){
            SignInScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("postId") ?: ""
            DetailPostScreen(postId = id, navController = navController)
        }
        composable(
            route =  Screen.UpdateProfile.route
        ){
            UpdateProfileScreen(navController = navController)
        }
        composable(
            route =  Screen.CreatePost.route
        ){
            CreatePostScreen(navController = navController)
        }
        // Di file navigation/navGraph.kt
        composable(
            route = Screen.EditPost.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            EditPostScreen(navController = navController, postId = postId)
        }
        composable(
            route = Screen.UserDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserDetailScreen(userId = userId, navController = navController)
        }
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(
            route = "unified_search/{type}",
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "post"
            UnifiedSearchScreen(navController = navController, initialTab = type)
        }
    }
}

@Composable
fun UnifiedSearchScreen(
    navController: NavController,
    initialTab: String,
    postViewModel: PostViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    // State untuk tab: 0 untuk Post, 1 untuk User
    var selectedTabIndex by remember { mutableIntStateOf(if (initialTab == "post") 0 else 1) }
    val tabs = listOf("Posts", "Users")
    val focusRequester = remember { FocusRequester() }

    // Efek agar keyboard otomatis muncul saat masuk ke layar ini
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(modifier = Modifier.fillMaxSize()) {
        // --- REAL Search Field ---
        OutlinedTextField(
            value = if (selectedTabIndex == 0) postViewModel.searchQuery else userViewModel.userSearchQuery,
            onValueChange = {
                if (selectedTabIndex == 0) postViewModel.onSearch(it)
                else userViewModel.onSearch(it) // Pastikan onSearch ada di UserViewModel juga
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester),
            placeholder = { Text("Search ${tabs[selectedTabIndex]}...") },
            leadingIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // --- Tabs ---
        TabRow(selectedTabIndex = selectedTabIndex, contentColor = Color(0xFF2196F3)) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = FontWeight.Bold) }
                )
            }
        }

        // --- Result List ---
        Box(modifier = Modifier.fillMaxSize()) {
            if (selectedTabIndex == 0) {
                // Gunakan LazyColumn yang sama dengan DaftarPostScreen Anda
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(postViewModel.postList) { post ->
                        PostItem(post = post, navController = navController)
                        Spacer(modifier = Modifier.height(8.dp)) // Jarak antar item
                    }
                }
            } else {
                // Gunakan LazyVerticalGrid yang sama dengan UserScreen Anda
                LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(16.dp)) {
                    items(userViewModel.userList) { user ->
                        UserItem(user = user, modifier = Modifier.clickable {
                            navController.navigate("user_detail/${user.id}")
                        })
                    }
                }
            }
        }
    }
}