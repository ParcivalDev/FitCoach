package com.example.fitcoach.ui.screens.calendar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

// Datos que guardamos en Firestore
data class WorkoutNoteData(
    val date: String = "",    // Fecha en formato "2024-03-25"
    val note: String = "",    // Texto de la nota
    val rating: String = ""   // Rating como texto (GOOD, BAD, etc)
)

// Estados posibles al cargar/guardar datos
sealed class CalendarResult<out T> {
    data class Success<T>(val data: T) : CalendarResult<T>()
    data class Error(val exception: Exception) : CalendarResult<Nothing>()
    data object Loading : CalendarResult<Nothing>()
}

object CalendarRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Habilitar persistencia offline (llamar una vez al iniciar la app)
    fun enableOfflineData() {
        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
    }

    fun saveNote(
        date: String,
        note: WorkoutNote,
        onResult: (CalendarResult<Unit>) -> Unit
    ) {
        // Verificar si hay usuario logueado
        val userId = auth.currentUser?.uid ?: run {
            onResult(CalendarResult.Error(Exception("Usuario no autenticado")))
            return
        }

        // Crear objeto para guardar
        val noteData = WorkoutNoteData(
            date = date,
            note = note.note,
            rating = note.rating.name
        )

        // Guardar en Firestore
        db.collection("users")
            .document(userId)
            .collection("notes")
            .document(date)
            .set(noteData)
            .addOnSuccessListener {
                onResult(CalendarResult.Success(Unit))
            }
            .addOnFailureListener { e ->
                onResult(CalendarResult.Error(e))
            }
    }

    fun loadNotes(onResult: (CalendarResult<Map<String, WorkoutNote>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onResult(CalendarResult.Error(Exception("Usuario no autenticado")))
            return
        }

        onResult(CalendarResult.Loading)

        db.collection("users")
            .document(userId)
            .collection("notes")
            .get()
            .addOnSuccessListener { snapshot ->
                val notes = snapshot.documents.mapNotNull { doc ->
                    val noteData = doc.toObject(WorkoutNoteData::class.java)
                    noteData?.let {
                        doc.id to WorkoutNote(it.note, WorkoutRating.valueOf(it.rating))
                    }
                }.toMap()
                onResult(CalendarResult.Success(notes))
            }
            .addOnFailureListener { e ->
                onResult(CalendarResult.Error(e))
            }
    }

    fun deleteNote(
        date: String,
        onResult: (CalendarResult<Unit>) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onResult(CalendarResult.Error(Exception("Usuario no autenticado")))
            return
        }

        db.collection("users")
            .document(userId)
            .collection("notes")
            .document(date)
            .delete()
            .addOnSuccessListener {
                onResult(CalendarResult.Success(Unit))
            }
            .addOnFailureListener { e ->
                onResult(CalendarResult.Error(e))
            }
    }
}