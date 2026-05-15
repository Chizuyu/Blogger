package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.DetailPostScreen
// Import halaman Anda (Sesuaikan jika nama folder/package berbeda)
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.SignInScreen
import com.example.myapplication.ui.screens.UpdateProfileScreen

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
    }
}