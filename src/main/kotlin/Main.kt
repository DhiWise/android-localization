import language.InputModel
import language.LanguageModel
import sheet.GoogleSheetReader
import sheet.GoogleSheetWriter
import utils.OperationPerform
import utils.printErrorMessage
import utils.printMessage
import java.io.File
import java.util.*

lateinit var scanner: Scanner

const val downloadFile = "downloadFiles"
fun main(args: Array<String>) {
    processInputs()
}

fun processInputs() {
    scanner = Scanner(System.`in`)
    val sheetId = getSheetId()
    val sheetName = getSheetName()
    val list = getLanguage()
    val xmlFile = getXmlFile()
    val inputModel = InputModel(sheetId, sheetName, xmlFile, list)
    if (xmlFile.exists()) {
        writeSheets(inputModel, object : OperationPerform {
            override fun success() {
                printMessage(
                    """Please check google sheet https://docs.google.com/spreadsheets/d/${inputModel.sheetId}/edit#gid=0
 For download translated language files type '$downloadFile'"""
                )
                downloadFiles(inputModel)
            }
        })
    }

}

private fun downloadFiles(inputModel: InputModel) {
    val name: String = scanner.nextLine()
    if (name.isEmpty()) {
        printMessage("For download translated language files type '$downloadFile'")
        downloadFiles(inputModel)
        return
    }
    if (downloadFile.equals(name, ignoreCase = true)) {
        val readSheet = GoogleSheetReader()
        readSheet.readGoogleSheet(inputModel)
    } else {
        printMessage("You want to download translated language files ? (Y?N)")
        val ans: String = scanner.nextLine()
        if (ans.equals("Y", ignoreCase = true)) {
            printMessage("For download translated language files type '$downloadFile'")
            downloadFiles(inputModel)
        }
    }
}

private fun writeSheets(inputModel: InputModel, operationPerform: OperationPerform) {
    val writeSheet = GoogleSheetWriter()
    writeSheet.readXmlAndWriteSheet(
        inputModel.xmlFile,
        inputModel.list,
        inputModel.sheetId,
        inputModel.sheetName,
        operationPerform
    )
}

/*  if (url.contains("/d/") && url.contains("/edit")) {
            sheetUrl = url;
            String _d[] = url.split("/d/");
            String _id[] = _d[1].split("/edit");
            spreadsheetId = _id[0];
        } else {
            Messages.showErrorMessage("Invalid google sheet url.");
        }
*/

private fun getSheetId(): String {
    printMessage("Please enter google sheet url : ")
    val url: String = scanner.nextLine()
    if (url.isNullOrEmpty()) {
        getSheetId()
    }
    var id = ""
    if (url.contains("/d/") && url.contains("/edit")) {
        val _d = url.split("/d/").toTypedArray()
        val _id = _d[1].split("/edit").toTypedArray()
        id = _id[0]
    } else {
        printErrorMessage("Invalid google sheet url.")
        getSheetId()
    }

    return id
}

private fun getSheetName(): String {
    printMessage("Please enter google sheet name : ")
    val name: String = scanner.nextLine()
    if (name.isNullOrEmpty()) {
        getSheetName()
    }
    return name
}

private fun getLanguage(): ArrayList<LanguageModel> {
    val languageList = arrayListOf<LanguageModel>()
    printMessage("Please enter language code (i.e. German=de,French(Standard)=fr,French(Belgium)=fr-be )")
    val language: String = scanner.nextLine()
    if (language.isNullOrEmpty()) {
        getLanguage()
    } else {
        try {
            val list = language.split(",").toTypedArray()

            for (i in list.indices) {
                val codes = list[i].split("=").toTypedArray()
                languageList.add(LanguageModel(codes[0], codes[1]))
            }
        } catch (e: Exception) {
            printErrorMessage("Language should be in 'LanguageName=LanguageCode' format.")
            getLanguage()
        }
    }

    return languageList
}

private fun getXmlFile(): File {
    printMessage("Please enter 'String.xml' file path (i.e.D:\\string.xml): ")
    val file: String = scanner.nextLine()
    val xmlFile = File(file)
    return if (file.isEmpty()) {
        getXmlFile()
    } else if (!xmlFile.exists()) {
        printMessage("File $file not found.")
        getXmlFile()
    } else {
        xmlFile
    }

}


