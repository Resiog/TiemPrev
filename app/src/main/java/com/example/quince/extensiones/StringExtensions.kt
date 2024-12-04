package com.example.quince.extensiones

import java.text.Normalizer
import java.util.regex.Pattern
import java.nio.charset.StandardCharsets

fun String.decodeTildesAVersiAhora(): String {
    // Primero, reemplazo las secuencias Unicode con sus caracteres correspondientes
    var result = this
        .replace("\\u00e1", "á")
        .replace("\\u00e9", "é")
        .replace("\\u00ed", "í")
        .replace("\\u00f3", "ó")
        .replace("\\u00fa", "ú")
        .replace("\\u00c1", "Á")
        .replace("\\u00c9", "É")
        .replace("\\u00cd", "Í")
        .replace("\\u00d3", "Ó")
        .replace("\\u00da", "Ú")
        .replace("\\u00f1", "ñ")
        .replace("\\u00d1", "Ñ")
        .replace("\\u00c3\\u00a9", "é")
        .replace("\\u00c3\\u00a1", "á")
        .replace("\\u00c3\\u00ad", "í")
        .replace("\\u00c3\\u00b3", "ó")
        .replace("\\u00c3\\u00ba", "ú")
        .replace("\\u00c3\\u00b1", "ñ")

    // Paso 2: Decodifo caracteres mal interpretados (como 'Ã©' -> 'é')
    result = String(result.toByteArray(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)

    // Paso 3: Normalizo los caracteres para asegurar que no haya problemas de codificación
    result = Normalizer.normalize(result, Normalizer.Form.NFC)

    // Paso 4: Decodifico caracteres como M\u00e1laga y Andaluc\u00eda ya que me dan problemas
    result = result.replace("\\u00e1", "á")
        .replace("\\u00ed", "í")
        .replace("\\u00e9", "é")
        .replace("\\u00fa", "ú")
        .replace("\\u00f3", "ó")
        .replace("\\u00f1", "ñ")

    return result
}

fun String.decodeTildes(): String {
    // Mapeo manual de secuencias Unicode problemáticas
    val replacements = mapOf(
        "\\u00e1" to "á",
        "\\u00e9" to "é",
        "\\u00c3\\u00a9" to "é",
        "Ã©" to "é",
        "\\u00ed" to "í",
        "\\u00f3" to "ó",
        "\\u00fa" to "ú",
        "\\u00c1" to "Á",
        "\\u00c9" to "É",
        "\\u00cd" to "Í",
        "\\u00d3" to "Ó",
        "\\u00da" to "Ú",
        "\\u00f1" to "ñ",
        "\\u00d1" to "Ñ",
        "\\u00c3\\u00a1" to "á",
        "\\u00c3\\u00a9" to "é",
        "\\u00c3\\u00ad" to "í",
        "\\u00c3\\u00b3" to "ó",
        "\\u00c3\\u00ba" to "ú",
        "\\u00c3\\u00b1" to "ñ"
    )

    var result = this
    replacements.forEach { (unicode, replacement) ->
        result = result.replace(unicode, replacement)
    }

    return Normalizer.normalize(result, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}

// Método de extensión para limpiar caracteres Unicode a ver si ahora sí
//fun String.cleanUnicode(): String {
//    return Normalizer.normalize(this, Normalizer.Form.NFC)
//}
//Lo comento porque es poco robusto y seguía dando errores.

fun String.decodeUnicodeCompletely(): String {
    // Primero, normalizo los caracteres
    var result = Normalizer.normalize(this, Normalizer.Form.NFC)

    // Decodifico secuencias de escape Unicode explícitas
    val unicodePattern = Pattern.compile("\\\\u([0-9A-Fa-f]{4})")
    val matcher = unicodePattern.matcher(result)
    val buffer = StringBuffer()

    while (matcher.find()) {
        val unicodeChar = matcher.group(1).toInt(16).toChar()
        matcher.appendReplacement(buffer, unicodeChar.toString())
    }
    matcher.appendTail(buffer)

    result = buffer.toString()

    // Limpio cualquier otra secuencia de escape Unicode restante
    result = result.replace("\\\\u[0-9A-Fa-f]{4}".toRegex(), "")

    return result
}