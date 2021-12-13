package sheet

import language.InputModel
import org.w3c.dom.Document
import sheet.SheetsServiceUtil.getSheetsService
import utils.ProgressBar
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class GoogleSheetReader {
    private var columnList: ArrayList<String>? = null
    private var documentList: ArrayList<Document>? = null
    private var progressBar: ProgressBar? = null

    init {
        progressBar = ProgressBar()
        columnList = ArrayList()
        documentList = ArrayList()
    }

    //Read Data From Google sheet
    fun readGoogleSheet(inputModel: InputModel) {
        try {
            val response =
                getSheetsService().spreadsheets().values()[inputModel.sheetId, inputModel.sheetName].execute()
            val values = response.getValues()
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            if (values == null || values.isEmpty()) {
                println("No data found.")
            } else {
                columnList!!.addAll(values[0] as List<String>)
                progressBar!!.update(0, columnList!!.size)
                for (i in 2 until columnList!!.size) {
                    val docWrite = dBuilder.newDocument()
                    val rootElement = docWrite.createElement("resources")
                    docWrite.appendChild(rootElement)
                    for (j in 1 until values.size) {
                        val row = values[j]
                        // root element : Write xml
                        val string = docWrite.createElement("string")
                        val attrType = docWrite.createAttribute("name")
                        attrType.value = row[0].toString()
                        string.setAttributeNode(attrType)
                        string.appendChild(docWrite.createTextNode(row[i].toString()))
                        rootElement.appendChild(string)
                    }
                    documentList!!.add(docWrite)
                    progressBar!!.update(i, columnList!!.size)
                }
            }
            // write the content into xml file
            if (documentList!!.size > 0) {
                for (i in documentList!!.indices) {
                    val docWrite = documentList!![i]
                    val transformerFactory = TransformerFactory.newInstance()
                    val transformer = transformerFactory.newTransformer()
                    val source = DOMSource(docWrite)
                    val path = if (inputModel.xmlFile.parentFile.path.substringAfterLast("/") == "values") {
                        inputModel.xmlFile.parentFile.parentFile
                    } else {
                        inputModel.xmlFile.parentFile
                    }
                    val destinationPath = File(path, "values-${inputModel.list.get(i).code}")
                    if (!destinationPath.exists()) {
                        destinationPath.mkdir()
                    }
                    val file = File(
                        destinationPath,
                        getBaseName(inputModel.xmlFile.name) + ".xml"
                    )
                    val result = StreamResult(file)
                    transformer.transform(source, result)
                    println("Generated File :$file")
                }
            }
        }catch (e: Exception) {
        }
    }

    private fun getBaseName(fileName: String): String {
        val index = fileName.lastIndexOf('.')
        return if (index == -1) {
            fileName
        } else {
            fileName.substring(0, index)
        }
    }
}