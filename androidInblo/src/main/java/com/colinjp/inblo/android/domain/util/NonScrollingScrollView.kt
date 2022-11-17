package com.colinjp.inblo.android.domain.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView

class NonScrollingScrollView @JvmOverloads constructor(c: Context, attr: AttributeSet): HorizontalScrollView(c,attr){
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}