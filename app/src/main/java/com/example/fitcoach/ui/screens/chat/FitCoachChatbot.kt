package com.example.fitcoach.ui.screens.chat

class FitCoachChatbot {
    private val responses = mapOf(
        // Ejercicios
        "ejercicios" to "En la biblioteca de ejercicios encontrarás una amplia variedad de ejercicios organizados por grupos musculares, cada uno con su video explicativo.",
        "biblioteca" to "Accede a la biblioteca desde el menú lateral. Podrás filtrar ejercicios por grupo muscular como pectoral, espalda, piernas, etc.",

        // Academia
        "academia" to "La sección Academia contiene módulos educativos con videos formativos sobre entrenamiento, técnica y nutrición.",
        "módulos" to "En la Academia encontrarás diferentes módulos. Cada módulo contiene lecciones en video para mejorar tu conocimiento.",

        // Timer
        "temporizador" to "El temporizador te ayuda a controlar tus descansos entre series. Accede desde la barra inferior y configura el tiempo que necesites.",
        "descanso" to "Para los descansos entre series usa el temporizador. Te avisará con una notificación cuando termine el tiempo.",

        // Calendario
        "calendario" to "Usa el calendario para registrar tus entrenamientos. Puedes añadir notas y valorar cada sesión con diferentes emojis.",
        "registro" to "Registra tus entrenamientos en el calendario y mantén un seguimiento de tu progreso.",

        // Blog
        "blog" to "En el blog encontrarás artículos sobre entrenamiento, nutrición, lesiones y más temas relacionados con el fitness.",
        "artículos" to "Los artículos del blog están organizados por categorías: hipertrofia, lesiones, recomposición corporal, etc.",

        // General
        "ayuda" to "¿En qué puedo ayudarte? Las principales secciones son: biblioteca de ejercicios, academia, temporizador, calendario y blog.",
        "funciones" to "FitCoach te ofrece: biblioteca de ejercicios con videos, academia formativa, temporizador para descansos, calendario de seguimiento y blog informativo."
    )

    fun getResponse(input: String): String {
        val query = input.lowercase()

        for ((keyword, response) in responses) {
            if (query.contains(keyword)) {
                return response
            }
        }

        return "Como asistente de FitCoach, puedo ayudarte con información sobre cualquier sección de la app: biblioteca de ejercicios, academia, temporizador, calendario o blog. ¿Qué te gustaría conocer?"
    }
}