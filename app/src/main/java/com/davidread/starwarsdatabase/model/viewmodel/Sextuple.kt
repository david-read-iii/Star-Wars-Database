package com.davidread.starwarsdatabase.model.viewmodel

/**
 * Helper object to use with RxJava disposables. Specifically, with the `zip`, `combineLatest`, and
 * similar operators.
 */
data class Sextuple<T1, T2, T3, T4, T5, T6>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4,
    val fifth: T5,
    val sixth: T6
)