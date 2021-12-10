package language

import java.io.File
import java.util.ArrayList

data class LanguageModel(val name: String, val code: String)

data class InputModel(val sheetId: String, val sheetName: String, val xmlFile: File, val list: ArrayList<LanguageModel>)