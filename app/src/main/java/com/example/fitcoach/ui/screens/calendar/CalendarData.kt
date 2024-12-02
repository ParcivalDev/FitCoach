package com.example.fitcoach.ui.screens.calendar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Datos que guardamos en Firestore
data class WorkoutNoteData(
    val date: String = "",    // Fecha en formato "2024-03-25"
    val note: String = "",    // Texto de la nota
    val rating: String = ""   // Rating como texto (GOOD, BAD, etc)
)

// Estados de la nota
sealed class CalendarResult<out T> {
    data class Success<T>(val data: T) : CalendarResult<T>()
    data class Error(val exception: Exception) : CalendarResult<Nothing>()
    data object Loading : CalendarResult<Nothing>()
}

// Repositorio para guardar y cargar notas
object CalendarRepository {
    // Inicializar Firestore y Auth
    // usamos lazy para inicializar db solo cuando se necesite
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth = FirebaseAuth.getInstance()


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
        db.collection("users") // Colección de usuarios
            .document(userId) // Documento del usuario actual
            .collection("notes") // Colección de notas
            .document(date) // Documento con la fecha
            .set(noteData) // Guardar datos
            .addOnSuccessListener { // Si se guarda correctamente
                onResult(CalendarResult.Success(Unit))
            }
            .addOnFailureListener { e -> // Si hay un error
                onResult(CalendarResult.Error(e))
            }
    }

    // Cargar notas de Firestore
    fun loadNotes(onResult: (CalendarResult<Map<String, WorkoutNote>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onResult(CalendarResult.Error(Exception("Usuario no autenticado")))
            return
        }

        onResult(CalendarResult.Loading)

        db.collection("users")
            .document(userId)
            .collection("notes")
            .get() // Obtener todas las notas
            .addOnSuccessListener { snapshot ->
                val notes = snapshot.documents.mapNotNull { doc ->
                    // Convertir a WorkoutNote y mapear por fecha
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

    // Borrar nota de Firestore
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
            .document(date) // Documento con la fecha a borrar
            .delete() // Borrar documento
            .addOnSuccessListener {
                onResult(CalendarResult.Success(Unit))
            }
            .addOnFailureListener { e ->
                onResult(CalendarResult.Error(e))
            }
    }
}