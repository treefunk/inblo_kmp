package com.colinjp.inblo.android.domain.util

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val SHOW_USER_ID = intPreferencesKey("show_user_id")
    val SHOW_STABLE_ID = intPreferencesKey("show_stable_id")
    val SHOW_USER_NAME = stringPreferencesKey("show_user_name")
    val SHOW_USER_ROLE = stringPreferencesKey("show_user_role")
}