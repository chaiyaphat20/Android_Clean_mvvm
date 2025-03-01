// app/src/main/java/com/example/cleanarchitecturemvvmandroid/presentation/theme/Theme.kt
package com.example.cleanarchitecturemvvmandroid.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.cleanarchitecturemvvmandroid.presentation.user.theme.Pink40
import com.example.cleanarchitecturemvvmandroid.presentation.user.theme.Pink80
import com.example.cleanarchitecturemvvmandroid.presentation.user.theme.Purple40
import com.example.cleanarchitecturemvvmandroid.presentation.user.theme.Purple80
import com.example.cleanarchitecturemvvmandroid.presentation.user.theme.PurpleGrey40
import com.example.cleanarchitecturemvvmandroid.presentation.user.theme.PurpleGrey80
import com.example.cleanarchitecturemvvmandroid.presentation.user.theme.Typography

// สีสำหรับธีมสว่าง
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// สีสำหรับธีมมืด
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// ฟังก์ชัน Composable ธีมหลักของแอพ
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}