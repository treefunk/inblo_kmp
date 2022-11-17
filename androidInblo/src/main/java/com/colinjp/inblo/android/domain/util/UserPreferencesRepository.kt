package com.colinjp.inblo.android.domain.util

import android.content.Context
import androidx.datastore.core.DataStore

class UserPreferencesRepository(
    private val UserPreferences: DataStore<UserPreferences>,
    private val context: Context
)