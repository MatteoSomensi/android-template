package com.example.androidtemplate.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

class Navigator(
    private val backStack: MutableList<NavKey>,
) {
    fun navigate(destination: NavKey) {
        if (backStack.lastOrNull() != destination) backStack.add(destination)
    }

    fun replaceAll(vararg destinations: NavKey) {
        backStack.clear()
        backStack.addAll(destinations)
    }

    fun goBack(): Boolean = backStack.removeLastOrNull() != null
}

typealias EntryProviderInstaller = EntryProviderScope<NavKey>.(Navigator) -> Unit
