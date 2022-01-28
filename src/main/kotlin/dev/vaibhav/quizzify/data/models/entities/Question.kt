package dev.vaibhav.quizzify.data.models.entities

data class Question(
    val questionId: String,
    val question: String,
    val options: List<Option>,
    val points: Int = 3,
) {
    val correctOptionId = options.find { it.isCorrect }?.optionId ?: -1
}
