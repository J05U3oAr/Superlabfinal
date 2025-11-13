package uvg.arodi.chavez.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "crypto_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val LAST_UPDATE_KEY = longPreferencesKey("last_update_timestamp")
    }

    val lastUpdateTimestamp: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[LAST_UPDATE_KEY]
    }

    suspend fun saveLastUpdateTimestamp(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_UPDATE_KEY] = timestamp
        }
    }

    suspend fun clearLastUpdateTimestamp() {
        context.dataStore.edit { preferences ->
            preferences.remove(LAST_UPDATE_KEY)
        }
    }
}