package com.colinjp.inblo.android.domain.model

import com.colinjp.inblo.android.R

enum class EventType(val type: String, val colorDark: Int, val colorDarker: Int, val colorLight: Int) {
    FINAL_CUT_OFF("最終追切", R.color.colorEventTypeDark_1, R.color.colorEventTypeDarker_1, R.color.colorEventTypeLight_1),
    INTERM_CUT_OFF("中間追切", R.color.colorEventTypeDark_1, R.color.colorEventTypeDarker_1, R.color.colorEventTypeLight_1),
    RACE_SCHED("レース予定", R.color.colorEventTypeDark_2, R.color.colorEventTypeDarker_2, R.color.colorEventTypeLight_2),
    FARRIER("装蹄", R.color.colorEventTypeDark_2, R.color.colorEventTypeDarker_2, R.color.colorEventTypeLight_2),
    STABLES_RETIRE("退厩", R.color.colorEventTypeDark_3, R.color.colorEventTypeDarker_3, R.color.colorEventTypeLight_3),
    STABLES_RETURN("帰厩", R.color.colorEventTypeDark_3, R.color.colorEventTypeDarker_3, R.color.colorEventTypeLight_3),
    STABLE_RELATED("厩舎関連", R.color.colorEventTypeDark_3, R.color.colorEventTypeDarker_3, R.color.colorEventTypeLight_3),
    BUSINESS_TRIP("出張・不在予定", R.color.colorEventTypeDark_4, R.color.colorEventTypeDarker_4, R.color.colorEventTypeLight_4),
    OTHERS("その他", R.color.colorEventTypeDark_4, R.color.colorEventTypeDarker_4, R.color.colorEventTypeLight_4)
}