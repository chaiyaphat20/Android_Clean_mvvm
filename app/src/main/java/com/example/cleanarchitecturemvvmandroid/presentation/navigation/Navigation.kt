// app/src/main/java/com/example/cleanarchitecturemvvmandroid/presentation/navigation/AppNavigation.kt
package com.example.cleanarchitecturemvvmandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cleanarchitecturemvvmandroid.presentation.user.UserDetailScreen
import com.example.cleanarchitecturemvvmandroid.presentation.user.UserScreen

/**
 * การกำหนดการนำทางภายในแอพ
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "users"
    ) {
        // หน้าแสดงรายการผู้ใช้
        composable("users") {
            UserScreen(
                onUserClick = { userId ->
                    // นำทางไปยังหน้ารายละเอียดผู้ใช้
                    navController.navigate("user_detail/$userId")
                }
            )
        }

        // หน้าแสดงรายละเอียดผู้ใช้
        composable("user_detail/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: -1
            UserDetailScreen(
                userId = userId,
                navController = navController
            )
        }
    }
}