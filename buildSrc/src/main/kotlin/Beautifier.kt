import org.gradle.api.Project
import org.jsoup.Jsoup
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import java.io.File

class Beautifier(private val project: Project, private val modules: Set<Project>) {

    private val dimRegexp = Regex("""^\d+x\d+$""")

    fun replaceImageLinksWithImgs(doc: Document) {
        doc.select("a[href]").filter { dimRegexp.matches(it.text()) }.forEach {
            val parts = it.text().split("x")
            it.replaceWith(Element(Tag.valueOf("img"), "", Attributes().apply {
                put("src", it.attr("href"))
                put("width", parts[0])
                put("height", parts[1])
            }))
        }
    }

    private fun beautifyHtml(file: File) {
        val doc = Jsoup.parse(
                file.readText().replace("&nbsp;/&nbsp;", ""),
                "UTF-8"
        )

        replaceImageLinksWithImgs(doc)

        println(doc.outerHtml())
    }

    private fun copy(from: String, to: String) {
        val source = File(from)
        val target = File(to)
        if (!source.exists()) {
            return
        }
        target.deleteRecursively()
        source.copyTo(target, true)
    }

    fun beautify() {
        copy("buildSrc/style.css", "html-docs/style.css")

        modules.forEach {
            val module = it

            copy("${module.name}/static", "html-docs/${module.name}/static")

            File("html-docs/${module.name}").walk().forEach {
                if (it.isFile && it.name.endsWith(".html")) {
                    beautifyHtml(it)
                }
            }

        }

        val projectDivs = modules.map {
            var description = ""
            if (it.description != null) {
                description = "<p>${it.description}</p>"
            }
            """
            <div class="section">
                <h3><a href="${it.name}">${it.name} (${it.version})</a></h3>
                $description
                <code>
                compile ('${it.group}.${it.name}:${it.version}@aar') {
                    transitive = true
                }
                </code>
            </div>
            """
        }

        val indexFile = File("html-docs/index.html")
        indexFile.writeText("""
        <html>
            <head>
                <meta charset="UTF-8">
                <title>${project.name} (${project.version})</title>
                <link rel="stylesheet" href="style.css">
            </head>
            <body>
                <div class="breadcrumbs">
                    / <a href="/">index</a>
                </div>
                ${projectDivs.joinToString("\n")}
            </body>
        </html>
        """)

    }

}