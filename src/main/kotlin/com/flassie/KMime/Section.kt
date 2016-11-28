package com.flassie.Mime

data class Section(val priority: Int, val mimeType: String, val rules: List<Rule>)