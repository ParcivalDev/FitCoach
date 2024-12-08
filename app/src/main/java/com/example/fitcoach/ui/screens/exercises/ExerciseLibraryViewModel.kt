package com.example.fitcoach.ui.screens.exercises


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class ExerciseLibraryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    private val _selectedMuscleGroup = MutableStateFlow<String?>(null)
    val selectedMuscleGroup: StateFlow<String?> = _selectedMuscleGroup.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun selectMuscleGroup(muscleGroup: String) {
        _selectedMuscleGroup.value = muscleGroup
        loadExercises(muscleGroup)
    }
    // Lista filtrada que combina los ejercicios con la búsqueda
    val filteredExercises = combine(_exercises, searchQuery) { exercises, query ->
        if (query.isEmpty()) {
            exercises
        } else {
            exercises.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }.sortedBy { it.name }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun initialize(initialMuscle: String?) {
        // Solo inicializamos con initialMuscle si no hay un grupo muscular ya seleccionado
        if (_selectedMuscleGroup.value == null) {
            _selectedMuscleGroup.value = initialMuscle
            loadExercises(initialMuscle)
        }
    }

    private fun loadExercises(muscleGroup: String?) {
        _isLoading.value = true
        _error.value = null

        val query = if (muscleGroup == null) {
            // Si no hay grupo muscular seleccionado, traer todos los ejercicios
            db.collection("exercises")
        } else {
            // Si hay grupo muscular, filtrar por él
            db.collection("exercises").whereEqualTo("muscleGroup", muscleGroup)
        }

        query.get()
            .addOnSuccessListener { documents ->
                _exercises.value = documents.mapNotNull { doc ->
                    doc.toObject(Exercise::class.java)
                }.sortedBy { it.name }
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _error.value = e.message
                _isLoading.value = false
            }
    }
}
