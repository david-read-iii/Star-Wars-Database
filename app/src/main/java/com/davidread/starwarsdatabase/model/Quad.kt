package com.davidread.starwarsdatabase.model

/**
 * Helper object to use with RxJava disposables. Specifically, with the `zip`, `combineLatest`, and
 * similar operators.
 */
data class Quad<T1, T2, T3, T4>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4
)