package com.flassie.Mime

object Mime {
    @JvmStatic var mimes: List<Section> = MimeParser.parseMimes(javaClass.getResourceAsStream("magic"))
        private set

    @JvmStatic fun match(filename: String): String? {
        var lastMime = ""
        mimes.forEach {
            sect ->

            var indentLevel = 0
            var matched = true

            sect.rules.forEach {
                rule ->
                if (rule.indent == indentLevel) {
                    indentLevel++
                    if(rule.matchFile(filename)) {
                        lastMime = sect.mimeType
                        matched = true
                    } else {
                        indentLevel--
                        matched = false
                    }
                }
            }

            if(matched) return lastMime
        }

        return null
    }
}