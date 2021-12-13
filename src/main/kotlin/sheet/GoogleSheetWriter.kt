package sheet

import com.google.api.services.sheets.v4.model.ClearValuesRequest
import com.google.api.services.sheets.v4.model.ValueRange
import language.LanguageModel
import utils.OperationPerform
import utils.ProgressBar
import utils.printErrorMessage
import xml.XmlReader
import java.io.File

class GoogleSheetWriter {
    private var progressBar: ProgressBar? = null

   init {
       progressBar = ProgressBar()
   }


    fun readXmlAndWriteSheet(
        xml: File,
        languages: ArrayList<LanguageModel>,
        spreadsheetId: String?,
        sheetName: String?,
        operationPerform: OperationPerform
    ) {
        val xmlReader =XmlReader()
        val nodeList = xmlReader.readXml(xml)
        try {
            SheetsServiceUtil.getSheetsService().spreadsheets().values()
                .clear(spreadsheetId, sheetName, ClearValuesRequest()).execute()
            val list = ArrayList<ArrayList<String>>()
            list.add(headerItem(languages))
            progressBar?.update(0, nodeList!!.length)
            for (i in 0 until nodeList!!.length) {
                val currentItem = nodeList?.item(i)
                if (currentItem.attributes.getNamedItem("name") != null) {
                    val key = currentItem.attributes.getNamedItem("name").nodeValue
                    list.add(rowValue(key, currentItem.textContent, languages, i))
                }
                progressBar?.update(i, nodeList.length)

                /*Arrays.asList(key, currentItem.getTextContent(), "=GOOGLETRANSLATE(B" + (i + 2) + ",\"en\",\"es\")", "=GOOGLETRANSLATE(B" + (i + 2) + ",\"en\",\"hi\")")*/
            }
            val vr: ValueRange = ValueRange().setValues(list as List<MutableList<Any>>?)
            SheetsServiceUtil.getSheetsService().spreadsheets().values()
                .update(spreadsheetId, sheetName, vr)
                .setValueInputOption("USER_ENTERED")
                .setIncludeValuesInResponse(true)
                .execute()
            operationPerform.success()
        } catch (e: Exception) {
          printErrorMessage("Wrong sheet input entered")
        }
    }

    private fun rowValue(
        key: String,
        textContent: String,
        languages: ArrayList<LanguageModel>,
        position: Int
    ): ArrayList<String> {
        val list = ArrayList<String>()
        list.add(key)
        list.add(textContent)
        for (i in languages.indices) {
            list.add("=GOOGLETRANSLATE(B" + (position + 2) + ",\"en\",\"" + languages[i].code + "\")")
        }
        return list
    }

    private fun headerItem(languages: ArrayList<LanguageModel>): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("")
        list.add("English")
        for (i in languages.indices) {
            list.add(languages[i].name)
        }
        return list
    }

}