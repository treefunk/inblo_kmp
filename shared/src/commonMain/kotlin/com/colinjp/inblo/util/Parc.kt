package com.colinjp.inblo.util


expect interface AndroidParcel

@OptIn(ExperimentalMultiplatform::class)
//@kotlin.ExperimentalMultiplatform
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class AndroidParcelize()

