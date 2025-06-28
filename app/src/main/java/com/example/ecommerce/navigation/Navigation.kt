package com.example.ecommerce.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.screens.HomeScreen

@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Details.route) {
            //TODO add details screen
            /*val productId = it.arguments?.getString("productId")
            DetailsScreen(
                navController = navController,
                productId = productId,
                backStack = navController::popBackStack
            )*/
        }
    }
}