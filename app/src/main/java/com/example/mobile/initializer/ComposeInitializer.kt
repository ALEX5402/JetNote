package com.example.mobile.initializer

import android.content.Context
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ProcessLifecycleInitializer
import androidx.startup.Initializer
import androidx.work.WorkManager

internal class ComposeInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        ComposeView(context)
    }

    override fun dependencies(): MutableList<Class<ProcessLifecycleInitializer>> {
        return mutableListOf(
            ProcessLifecycleInitializer::class.java
        )
    }
}