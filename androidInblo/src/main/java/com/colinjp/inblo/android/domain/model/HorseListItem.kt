package com.colinjp.inblo.android.domain.model

data class HorseListItem(
    var stableName: String? = null,
    var horseName: String? = null,
    var personInCharge: String? = null,
    var nextRunSchedule: String? = null,
    var id: Int? = 0,
)