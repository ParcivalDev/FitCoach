package com.example.fitcoach.ui.screens.academy

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AcademyViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _modules = MutableStateFlow<List<Module>>(emptyList())
    val modules: StateFlow<List<Module>> = _modules.asStateFlow()

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _selectedModuleId = MutableStateFlow<String?>(null)
    val selectedModuleId: StateFlow<String?> = _selectedModuleId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadModules()
    }

    private fun loadModules() {
        _isLoading.value = true
        _error.value = null

        db.collection("modules")
            .get()
            .addOnSuccessListener { documents ->
                _modules.value = documents.mapNotNull { doc ->
                    // Incluimos el ID del documento en el objeto Module
                    doc.toObject(Module::class.java).copy(id = doc.id)
                }.sortedBy { it.order }  // Ordenamos por el campo order
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _error.value = e.message
                _isLoading.value = false
            }
    }

    fun selectModule(moduleId: String) {
        _selectedModuleId.value = moduleId
        loadLessons(moduleId)
    }

    fun clearSelectedModule() {
        _selectedModuleId.value = null
        _lessons.value = emptyList()
    }

    private fun loadLessons(moduleId: String) {
        _isLoading.value = true
        _error.value = null

        db.collection("lessons")
            .whereEqualTo("moduleId", moduleId)
            .get()
            .addOnSuccessListener { documents ->
                _lessons.value = documents.mapNotNull { doc ->
                    doc.toObject(Lesson::class.java)
                }.sortedBy { it.order } // Ordenamos localmente por el campo order
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _error.value = e.message
                _isLoading.value = false
            }
    }

    fun initialize(moduleId: String?) {
        moduleId?.let { selectModule(it) }
    }
}