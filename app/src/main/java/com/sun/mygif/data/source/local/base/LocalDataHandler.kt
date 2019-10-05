package com.sun.mygif.data.source.local.base

interface LocalDataHandler<P, T> {
    @Throws(Exception::class)
    fun execute(params: P): T
}
