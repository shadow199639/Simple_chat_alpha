package ru.skillbranch.devintensive.models

import android.graphics.Color
import androidx.core.graphics.component1

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        var validate = question.validate(answer)
        return when {
            question == Question.IDLE -> "${question.question}" to status.color
            question.answer.contains(answer)  -> {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            }
            status == Status.CRITICAL -> {
                question = Question.NAME
                status = status.nextStatus()
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
            !question.answer.contains(answer) && validate != "" ->{
                "$validate\n${question.question}" to status.color
            }
            else -> {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(var question: String, var answer: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validate(answer: String): String =
                if (answer.isNotEmpty() && answer.first().isUpperCase()) ""
            else "Имя должно начинаться с заглавной буквы"
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validate(answer: String): String =
                if(answer.isNotEmpty() && answer.first().isLowerCase()) ""
            else "Профессия должна начинаться со строчной буквы"
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun validate(answer: String): String =
                if(!answer.contains(Regex("\\d"))) ""
            else "Материал не должен содержать цифр"
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validate(answer: String): String =
                if(answer.contains(Regex("^\\d+$"))) ""
            else "Год моего рождения должен содержать только цифры"
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validate(answer: String): String =
                if(answer.contains(Regex("^\\d{7}$"))) ""
            else "Серийный номер содержит только цифры, и их 7"
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validate(answer: String): String = ""
        };

        abstract fun nextQuestion(): Question
        abstract fun validate(answer: String):String
    }
}