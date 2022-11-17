package com.colinjp.inblo


class Greeting {
    fun greeting(): String {

        return "Hello, ${Platform().platform}!"
    }
}