package com.example.meustock.di

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        // This is a simplified runner.
        // Usually, you'd extend AndroidJUnitRunner and override newApplication.
    }
}
