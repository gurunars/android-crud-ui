package com.gurunars.box

/**
 * Entity meant to hold the value and notify the observers about its
 * changes.
 */
interface IRoBox<Type> {
    /**
     * Fetches the current value.
     */
    fun get(): Type
    /**
     * Subscribes to changes of the value. Aware of the previous state.
     *
     * @param listener a function called with a new value after the change takes place
     * @param hot if true, immediately executes the listener with the current value
     *            otherwise just adds it to the collection of subscribers
     */
    fun onChange(hot: Boolean = true, listener: (value: Type) -> Unit): Bond
}