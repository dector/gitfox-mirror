package ru.terrakok.gitlabclient.model.utils

import java.net.URI

/**
 * @author Artur Badretdinov (Gaket)
 *         20.12.2016.
 */
fun getQueryParameterFromUri(url: String, queryName: String): String {
    val uri = URI(url)
    val query = uri.query
    val parameters = query.split("&")

    var code = ""
    for (parameter in parameters) {
        if (parameter.startsWith(queryName)) {
            code = parameter.substring(queryName.length + 1)
            break
        }
    }
    return code
}
