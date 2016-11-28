package com.flassie.Mime

import java.io.File
import java.util.*

data class Rule(val indent: Int, val startOffset: Int, val value: ByteArray, val mask: ByteArray, val wordSize: Int, val range: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Rule

        if(other.indent  == this.indent && other.startOffset == this.startOffset &&
                Arrays.equals(other.value, this.value) && Arrays.equals(other.mask, this.mask))
        {
            return true
        } else return false
    }

    override fun hashCode(): Int {
        return indent.hashCode() + startOffset.hashCode() + Arrays.hashCode(value) + Arrays.hashCode(mask)
    }

    fun matchFile(pathname: String): Boolean {
        val file = File(pathname)
        if(!file.canRead()) return false

        val stream = file.inputStream()

        for (offset in startOffset..startOffset+range) {
            stream.channel.position(0)
            if(stream.available() < (offset + value.size)) {
                return false
            }

            stream.skip(offset.toLong())
            val sample = ByteArray(value.size)
            stream.read(sample)

            if(equalsMaskedValue(sample))
                return true
        }

        return false
    }

    private fun equalsMaskedValue(sample: ByteArray): Boolean {
        for (i in 0..sample.size-1) {
            if ((value[i].toInt() and mask[i].toInt()) != (sample[i].toInt() and mask[i].toInt())) {
                return false
            }
        }
        return true
    }
}