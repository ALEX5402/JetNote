package com.example.quick_note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common_ui.DataStoreVM
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Quick() {
                    finish()
                }
            }
        }
    }

    @Composable
    private fun AppTheme(
        dataStoreVM: DataStoreVM = hiltViewModel(),
        content: @Composable () -> Unit
    ) {
        val currentTheme = remember(dataStoreVM, dataStoreVM::getTheme).collectAsState()
        val systemUiController = rememberSystemUiController()
        val isDarkUi = isSystemInDarkTheme()

        val theme = when {
            isSystemInDarkTheme() -> darkColorScheme()
            currentTheme.value == "GRID" -> darkColorScheme()
            else -> lightColorScheme()
        }

        SideEffect {
            systemUiController.apply {
                setStatusBarColor(Color.Transparent, !isDarkUi)
                setNavigationBarColor(
                    if (currentTheme.value == "GRID"|| isDarkUi) Color(red = 28, green = 27, blue = 31)
                    else Color(red = 255, green = 251, blue = 254)
                )
            }
        }

        MaterialTheme(colorScheme = theme, content = content)
    }
}

