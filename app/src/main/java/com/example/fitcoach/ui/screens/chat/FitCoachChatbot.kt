package com.example.fitcoach.ui.screens.chat

// Asistente de chatbot para responder a preguntas sobre la app
// Implementa un diccionario de respuestas para palabras clave
class FitCoachChatbot {
    private val responses = mapOf(
        // Ejercicios
        "ejercicios" to "En la biblioteca de ejercicios encontrarás una amplia variedad de ejercicios organizados por grupos musculares, cada uno con su video explicativo para relizar una buena técnica.",
        "biblioteca" to "Accede a la biblioteca de ejercicios mediante el la flecha y en el menú lateral podrás filtrar ejercicios por grupo muscular como pectoral, espalda, piernas, etc.",
        "técnica" to "Cada ejercicio en la biblioteca incluye un video detallado sobre la técnica correcta. Es importante seguir estas instrucciones para evitar lesiones.",

        // Academia
        "academia" to "La sección Academia contiene módulos educativos con videos formativos sobre entrenamiento, técnica y nutrición.",
        "módulos" to "En la Academia encontrarás diferentes módulos. Cada módulo contiene lecciones en video para mejorar tu conocimiento.",
        "cursos" to "Nuestros cursos en la Academia cubren temas como nutrición deportiva, anatomía muscular, y principios de entrenamiento avanzado.",

        // Temporizador
        "temporizador" to "El temporizador te ayuda a controlar tus descansos entre series. Accede desde la barra inferior y configura el tiempo que necesites o usa las opciones rápidas de 1:30 y 3:00 minutos. Te avisará con una notificación cuando termine.",
        "descanso" to "Para los descansos entre series usa el temporizador. Te avisará con una notificación cuando termine el tiempo.",

        // Calendario
        "calendario" to "El calendario te permite registrar cada entrenamiento con notas y emoticonos según cómo te hayas sentido. Accede desde la barra inferior y pulsa en cualquier día para añadir o ver tus registros.",
        "registrar" to "Registra tus entrenamientos en el calendario y mantén un seguimiento de tu progreso.",

        // Blog
        "blog" to "En el blog encontrarás artículos sobre entrenamiento, nutrición, lesiones y más temas relacionados con el fitness.",
        "artículos" to "Los artículos del blog están organizados por categorías: hipertrofia, lesiones, recomposición corporal, etc.",

        // Tienda
        "tienda" to "En la tienda podrás adquirir productos relacionados con el fitness como guías de ejercicios, ebooks y el diario de entrenamiento.",
        "comprar" to "Para comprar productos en la tienda, selecciona el producto que te interese y serás redirigido a un enlace seguro donde podrás completar la transacción.",

        // General
        "ayuda" to "¿En qué puedo ayudarte? Las secciones actualmente disponibles son: biblioteca de ejercicios, academia, temporizador, calendario, blog y tienda. Algunas funciones como el seguimiento de progreso y rutinas personalizadas estarán disponibles próximamente.",
        "funciones" to "FitCoach actualmente te ofrece: biblioteca de ejercicios con videos, academia formativa, temporizador para descansos, calendario de seguimiento, blog informativo y una tienda. Estamos trabajando en nuevas funciones que estarán disponibles próximamente.",
        "problemas" to "Si tienes problemas técnicos con la app, visita nuestra sección de Soporte en el menú lateral para obtener ayuda.",
        "soporte" to "Para problemas técnicos o dudas sobre la app, visita la sección de Soporte en el menú lateral y contacta con nuestro equipo.",
        "contacto" to "Para contactar con nuestro equipo, visita la sección de Soporte en el menú lateral y envíanos un mensaje con tus dudas o problemas.",

        //En desarrollo
        "entrenamiento" to "La sección de entrenamiento está actualmente en desarrollo. Gracias por su paciencia.",
        "progreso" to "La sección de progreso está en desarrollo y estará disponible próximamente. Por ahora, puedes usar el calendario para registrar tus entrenamientos y sensaciones día a día.",
        "rutina" to "La función para ver tu rutina está en desarrollo. Gracias por su paciencia.",
        "medidas" to "El registro de medidas corporales y peso está en desarrollo. Por ahora, puedes usar el calendario para hacer anotaciones sobre tu progreso.",

        )

    // Obtiene la respuesta del chatbot para una entrada dada
    fun getResponse(input: String): String {
        val query = input.lowercase() // Convertir a minúsculas para comparar

        // Busca palabras clave en la consulta y devuelve la respuesta correspondiente
        for ((keyword, response) in responses) {
            if (query.contains(keyword)) {
                return response
            }
        }

        // Respuesta por defecto si no se encuentra ninguna palabra clave
        return "Como asistente de FitCoach, puedo ayudarte con información sobre cualquier sección de la app: biblioteca de ejercicios, academia, temporizador, calendario o blog. ¿Qué te gustaría conocer?"
    }
}