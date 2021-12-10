package xml

import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class XmlReader {
    fun readXml(xmlFile: File): NodeList? {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(xmlFile)
            val xPathfactory = XPathFactory.newInstance()
            val xpath = xPathfactory.newXPath()
            val expr = xpath.compile("//resources/string[@name]")

            return expr.evaluate(doc, XPathConstants.NODESET) as NodeList
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}