package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Home : Screen("home_screen/{name}")
    object SignIn : Screen("signin_screen")
    object Detail : Screen("detail_screen/{postId}")
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
) {
    object Post : BottomBarScreen("post_screen", "Post")
    object User : BottomBarScreen("user_screen", "User")
    object Profile : BottomBarScreen("profile_screen", "Profile")
}