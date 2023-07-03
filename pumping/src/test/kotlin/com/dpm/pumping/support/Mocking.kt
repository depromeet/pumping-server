package com.dpm.pumping.support

import org.mockito.Mockito

object Mocking {
    fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}
