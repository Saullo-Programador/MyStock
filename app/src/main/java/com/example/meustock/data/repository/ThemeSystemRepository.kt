package com.example.meustock.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val darkModePreference = booleanPreferencesKey("dark_mode")

class ThemeSystemRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val isDarkMode = dataStore.data
        .map {
            it[darkModePreference] ?: false
        }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[darkModePreference] = isDarkMode
        }

    }
}