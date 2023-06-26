package com.bekmnsrw.inotes.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.bekmnsrw.inotes.core.navigation.NavigationHost
import com.bekmnsrw.inotes.ui.custom.Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme(
                darkTheme = false
            ) {
                CompositionLocalProvider() {
                    NavigationHost()
                }
            }
        }
    }
}
