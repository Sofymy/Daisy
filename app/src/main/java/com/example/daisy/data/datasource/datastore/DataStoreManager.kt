package com.example.daisy.data.datasource.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    companion object {
        val RECENT_SEARCHES_KEY = stringSetPreferencesKey("recent_searches")
    }

    val recentSearches: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[RECENT_SEARCHES_KEY] ?: emptySet()
        }

    suspend fun saveSearch(search: String) {
        context.dataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCHES_KEY]?.toMutableSet() ?: mutableSetOf()
            currentSearches.add(search)

            if (currentSearches.size > 5) {
                currentSearches.remove(currentSearches.first())
            }

            preferences[RECENT_SEARCHES_KEY] = currentSearches
        }
    }
}
