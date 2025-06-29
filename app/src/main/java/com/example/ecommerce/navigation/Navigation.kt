package com.example.ecommerce.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ecommerce.screens.DetailsScreen
import com.example.ecommerce.screens.HomeScreen

@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(navController = navController)
        }
        composable<Screen.Details> { backStackEntry ->
            val screenArgs: Screen.Details = backStackEntry.toRoute()
            val title = screenArgs.title
            val thumbnail = screenArgs.thumbnail
            val rating = screenArgs.rating
            val price = screenArgs.price
            val discountPercentage = screenArgs.discountPercentage
            val stock = screenArgs.stock
            DetailsScreen(
                title = title,
                thumbnail = thumbnail,
                rating = rating,
                price = price,
                discountPercentage = discountPercentage,
                stock = stock,
                backStack = { navController.popBackStack() }
            )
        }
    }
}