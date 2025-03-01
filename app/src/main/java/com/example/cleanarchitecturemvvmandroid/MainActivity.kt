// app/src/main/java/com/example/cleanarchitecturemvvmandroid/MainActivity.kt
package com.example.cleanarchitecturemvvmandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.cleanarchitecturemvvmandroid.presentation.navigation.AppNavigation
import com.example.cleanarchitecturemvvmandroid.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity หลักของแอพที่เป็นจุดเริ่มต้นของการแสดง UI
 *
 * @AndroidEntryPoint เป็น annotation ที่บอก Hilt ให้ inject dependencies ในคลาสนี้
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // ใช้ธีมที่กำหนดไว้ในแอพ
            AppTheme {
                // สร้าง Surface เป็นพื้นหลังของแอพ
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // สร้าง NavController สำหรับการนำทางในแอพ
                    val navController = rememberNavController()

                    // นำเข้า Navigation Graph ที่กำหนดไว้
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}