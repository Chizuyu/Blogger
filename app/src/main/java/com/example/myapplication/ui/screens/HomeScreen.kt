package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.model.Post
import com.example.myapplication.navigation.BottomBarScreen
import com.example.myapplication.navigation.Screen
import com.example.myapplication.navigation.UnifiedSearchScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewModel.LoginViewModel
import com.example.myapplication.ui.viewModel.PostViewModel

@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        BottomBarScreen.Post,
        BottomBarScreen.User,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        tonalElevation = 8.dp
    ) {
        screens.forEach { Screen ->
            NavigationBarItem(
                label = { Text(text = Screen.title) },
                icon = { },
                selected = currentDestination?.hierarchy?.any { it.route == Screen.route } == true,
                onClick = {
                    navController.navigate(Screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Post.route
    ) {
        composable(route = BottomBarScreen.Post.route) {
            DaftarPostScreen(navController = navController)
        }
        composable(route = BottomBarScreen.User.route) {
            UserListScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("postId") ?: ""
            DetailPostScreen(postId = id, navController = navController)
        }
        composable(route = Screen.UpdateProfile.route) {
            UpdateProfileScreen(navController = navController)
        }
        composable(route = Screen.CreatePost.route) {
            CreatePostScreen(navController = navController)
        }
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
fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(rootNavController: NavController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Esemka Blogger", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                ),
                actions = {
                    IconButton(onClick = {
                        rootNavController.navigate("unified_search")
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search Everything")
                    }

                    IconButton(onClick = {
                        // 1. Hapus Token di GlobalData
                        com.example.myapplication.util.GlobalData.tokenUser = ""

                        // 2. Navigasi balik ke Login dan hapus semua backstack
                        // Gunakan "login" sesuai dengan route di NavGraph utama Anda
                        rootNavController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true } // Menghapus semua riwayat navigasi
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(navController = bottomNavController)
        }
    )
    { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            BottomNavGraph(navController = bottomNavController)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ScreenHome() {
    MyApplicationTheme {
        HomeScreen(
            rootNavController = rememberNavController()
        )
    }
}