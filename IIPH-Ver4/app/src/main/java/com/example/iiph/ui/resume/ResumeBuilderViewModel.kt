package com.example.iiph.ui.resume

import android.app.Application
import android.content.ContentValues
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import java.io.IOException

class ResumeBuilderViewModel(application: Application) : AndroidViewModel(application) {

    var name by mutableStateOf("")
    var degree by mutableStateOf("")
    var skills by mutableStateOf("")
    var projects by mutableStateOf("")

    fun generatePdf() {
        val context = getApplication<Application>().applicationContext

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        // Title
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 24f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(name, (canvas.width / 2).toFloat(), 80f, paint)

        // Reset paint for content
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 12f
        paint.textAlign = Paint.Align.LEFT

        var yPos = 140f

        // Degree
        canvas.drawText("Degree:", 40f, yPos, paint)
        canvas.drawText(degree, 120f, yPos, paint)
        yPos += 40f

        // Skills
        canvas.drawText("Skills:", 40f, yPos, paint)
        canvas.drawText(skills, 120f, yPos, paint)
        yPos += 40f

        // Projects
        canvas.drawText("Projects:", 40f, yPos, paint)
        canvas.drawText(projects, 120f, yPos, paint)

        pdfDocument.finishPage(page)

        // Save the PDF
        val fileName = "${name.replace(" ", "_")}_Resume.pdf"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            try {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                Toast.makeText(context, "PDF saved to Downloads folder", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Error creating PDF file", Toast.LENGTH_LONG).show()
        }

        pdfDocument.close()
    }
}