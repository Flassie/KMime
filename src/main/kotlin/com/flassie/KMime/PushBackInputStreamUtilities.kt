package com.flassie.Mime

import java.io.ByteArrayOutputStream
import java.io.PushbackInputStream

fun PushbackInputStream.readUntil(vararg b: Int, skipLast: Boolean = true): ByteArray {
    if(this.available() == 0) return ByteArray(0)

    val byteArrayOutput = ByteArrayOutputStream()
    var lastByte: Int = 0
    while(this.available() != 0) {
        lastByte = this.read()
        if(lastByte in b) break
        byteArrayOutput.write(lastByte)
    }

    if(!skipLast) this.unread(lastByte)

    return byteArrayOutput.toByteArray()
}

fun PushbackInputStream.readIntUntil(vararg b: Int, skipLast: Boolean = true): Int {
    val byteArray = this.readUntil(*b, skipLast = skipLast)
    var ret = 0
    byteArray.forEach {
        val c = it.toChar()
        if(c.isDigit()) {
            ret *= 10
            ret += Integer.parseInt(c.toString())
        }
    }

    return ret
}

fun PushbackInputStream.readShort(): Short {
    val fb = this.read()
    val sb = this.read()

    return ((fb shl 8) or (sb and 0xFF)).toShort()
}

fun PushbackInputStream.isNext(char: Char) : Boolean {
    val next = this.read().toChar()
    this.unread(next.toInt())
    return next == char
}