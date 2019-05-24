package ru.terrakok.gitlabclient.ui.global.view.custom.codehighlight

object CodeHighlightJsPage {

    fun generatePage(rawCode: String, style: String) =
        "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "   <meta charset=\"utf-8\">\n" +
            "   <style type=\"text/css\">\n" +
            "       pre {\n" +
            "           margin: 0;\n" +
            "       }\n" +
            "   </style>\n" +
            "   <link rel=\"stylesheet\" href=\"./styles/$style\"/>\n" +
            "   <style type=\"text/css\">\n" +
            "       .hljs-line-numbers {\n" +
            "           \ttext-align: right;\n" +
            "           \tborder-right: 1px solid #ccc;\n" +
            "           \tcolor: #999;\n" +
            "           \t-webkit-touch-callout: none;\n" +
            "           \t-webkit-user-select: none;\n" +
            "           \t-khtml-user-select: none;\n" +
            "           \t-moz-user-select: none;\n" +
            "           \t-ms-user-select: none;\n" +
            "           \tuser-select: none; " +
            "       }\n" +
            "    </style>\n" +
            "    <script src=\"./highlight.pack.js\"></script>\n" +
            "    <script src=\"./highlightjs-line-numbers.min.js\"></script>\n" +
            "    <script>hljs.initHighlightingOnLoad();</script>\n" +
            "    <script>hljs.initLineNumbersOnLoad();</script>\n" +
            "</head>\n" +
            "<body style=\"margin: 0; padding: 0\" class=\"hljs\">\n" +
            "<pre><code>${rawCode.replace("<".toRegex(), "&lt;").replace(">".toRegex(), "&gt;")}</code></pre>\n" +
            "</body>\n</html>\n"
}