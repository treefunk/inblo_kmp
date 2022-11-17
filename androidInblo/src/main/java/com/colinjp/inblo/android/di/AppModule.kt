package com.colinjp.inblo.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.colinjp.inblo.android.InbloApp
import com.colinjp.inblo.android.domain.util.PreferenceKeys
import com.colinjp.inblo.android.domain.util.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(
        @ApplicationContext context: Context
    ): InbloApp {
        return context as InbloApp
    }

    @Singleton
    @Provides
    fun provideUserPreferencesFlow(
        @ApplicationContext context: Context
    ): Flow<UserPreferences> {
        return context.dataStore.data
            .map { preferences ->
                val stableId = preferences[PreferenceKeys.SHOW_STABLE_ID] ?: 0
                val userId = preferences[PreferenceKeys.SHOW_USER_ID] ?: 0
                val userName = preferences[PreferenceKeys.SHOW_USER_NAME] ?: ""
                val role = preferences[PreferenceKeys.SHOW_USER_ROLE] ?: ""
                UserPreferences(userId,stableId,userName,role)
            }
    }

}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
