package com.krakos.minijp.data

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException


object FileUtils {
    private const val FILENAME = "searches.txt"

    /**
     * Creates file "searches.txt" in apps directory
     */
    private fun createFile(file: File) {
        try {
            file.createNewFile()
        }
        catch (e: IOException) {
            Log.e("FileUtils_createFile", "Error creating file: ${e.message.toString()}")
        }
    }

    /**
     * Appends given string to the file and, if doesn't exists, creates file.
     */
    fun appendSearch(context: Context, search: String) {
        val file = File(context.filesDir, FILENAME)

        // Check if exists
        if (!file.exists()) {
            createFile(file)
        }

        // Delete oldest search if file contains more than 10 entries
        // >= would be wrong since a new line is automatically appended
        if (getSearches(context).size > 10) removeOldestSearch(context)

        // Append search text with new line
        try {
            file.appendText("$search\n")
        }
        catch (e: IOException) {
            Log.e("FileUtils_appendSearch", "Error appending data: ${e.message.toString()}")
        }
    }

    /** Retrieve all saved searches from file */
    fun getSearches(context: Context): List<String> {
        val file =  File(context.filesDir, FILENAME)

        return try {
            file.useLines { it.toList() }
        } catch (e: Exception) {
            Log.e("FileUtils_getSearches", "Error reading lines: ${e.message}")
            emptyList() // Return an empty list in case of errors
        }
    }


    /** Retrieve only last saved search from file */
    fun getLastSearch(context: Context): String? {
        val allSearches = getSearches(context)
        return if (allSearches.isEmpty()) null
        else allSearches.last()
    }


    /** Retrieve all searches, deletes oldest (first), writes all remaining searches */
    private fun removeOldestSearch(context: Context) {
        val file = File(context.filesDir, FILENAME)
        val fileContent: MutableList<String> = getSearches(context).toMutableList()

        file.writeText("")
        fileContent.drop(1).forEach { file.appendText(it) }
    }


    fun removeFile(context: Context) {
        val success = context.deleteFile(FILENAME)
        if (!success) Log.e("FileUtils_removeFile", "File not deleted")
    }
}