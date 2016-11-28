package com.flassie.KMime

import java.io.*
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

internal object MimeParser {
    @JvmStatic private val LOGGER = Logger.getLogger(MimeParser::class.java.name)!!
    @JvmStatic private val HEADER = byteArrayOf('M', 'I', 'M', 'E', '-', 'M', 'a', 'g', 'i', 'c',  0.toChar(), '\n')
    @JvmStatic private fun byteArrayOf(vararg elements: Char): ByteArray {
        val ret: ByteArray = ByteArray(elements.size)
        for (i in 0..elements.size-1)
            ret[i] = elements[i].toByte()
        return ret
    }

    @JvmStatic fun parseMimes(inStream: InputStream): List<Section> {
        val inputStream = PushbackInputStream(BufferedInputStream(inStream))

        val header: ByteArray = ByteArray(HEADER.size)
        inputStream.read(header)

        if(!Arrays.equals(header, HEADER)) {
            LOGGER.log(Level.SEVERE, "Magic file is corrupted")
            return listOf()
        }

        val ret = parseSections(inputStream)
        return ret
    }

    @JvmStatic private fun parseSections(fileInput: PushbackInputStream) : List<Section> {
        val list = mutableListOf<Section>()
        while (fileInput.available() != 0) {
            val section = parseSection(fileInput)
            if(section != null)
                list.add(section)
        }

        return list
    }

    @JvmStatic private fun parseSection(fileInput: PushbackInputStream) : Section? {
        val priority: Int
        val mimeType: String

        if(fileInput.read().toChar() != '[') return null
        priority = fileInput.readIntUntil(':'.toInt())
        mimeType = fileInput.readUntil(']'.toInt()).toString(Charsets.UTF_8)

        val rules = parseRules(fileInput)
        return Section(priority, mimeType, rules)
    }

    @JvmStatic private fun parseRules(fileInput: PushbackInputStream) : List<Rule> {
        val rules = mutableListOf<Rule>()

        while(true) {
            val indent = fileInput.readIntUntil('>'.toInt())
            val startOffset = fileInput.readIntUntil('='.toInt())
            val valueSize = fileInput.readShort().toInt()
            val value = ByteArray(valueSize)
            val mask = ByteArray(valueSize)
            var wordSize = 0
            var range = 0
            fileInput.read(value)
            var nextSymbol = fileInput.read().toChar()

            Arrays.fill(mask, 0xFF.toByte())

            if (nextSymbol != '\n') {
                if (nextSymbol == '&') {
                    fileInput.read(mask)
                    nextSymbol = fileInput.read().toChar()
                }
                if (nextSymbol == '~') {
                    wordSize = fileInput.readIntUntil('+'.toInt(), '\n'.toInt(), skipLast = false)
                    nextSymbol = fileInput.read().toChar()
                }
                if (nextSymbol == '+') {
                    range = fileInput.readIntUntil('\n'.toInt())
                }
            }

            rules.add(Rule(indent, startOffset, value, mask, wordSize, range))

            if(fileInput.available() == 0 || fileInput.isNext('[')) break
        }

        return rules
    }
}