package com.dhug.quick_math.data.local.entities

import javax.inject.Singleton

@Singleton
object QuickMath {

    enum class Operation(val symbol: String) {
        ADD("+"), SUB("-"), MUL("×"), DIV("÷")
    }

    enum class Level {
        EASY, MEDIUM, HARD
    }

    enum class QuestionType {
        BASIC,
        MULTI_STEP,
        COMPARE_EXPRESSIONS,
        FILL_IN_BLANK,
        FIND_CORRECT_ONE,
        TRICK_QUESTION,
        REVERSE_CALCULATION
    }

    enum class TrickType {
        ADD_THEN_MUL,
        MUL_THEN_ADD,
        WITH_ZERO,
        WITH_NEGATIVE,
        WITH_PAREN
    }

    data class Question(
        val questionText: String = "",
        val options: List<String> = emptyList(),
        val correctIndex: Int = 0,
        val type: QuestionType  = QuestionType.BASIC,
        val level: Level = Level.EASY
    )

    fun generateQuestion(level: Level = Level.EASY): Question {
        val type = QuestionType.entries.toTypedArray().random()
        return when (type) {
            QuestionType.BASIC -> generateBasicQuestion(level)
            QuestionType.MULTI_STEP -> generateMultiStepQuestion(level)
            QuestionType.COMPARE_EXPRESSIONS -> generateCompareQuestion(level)
            QuestionType.FILL_IN_BLANK -> generateFillInBlank(level)
            QuestionType.FIND_CORRECT_ONE -> generateFindCorrect(level)
            QuestionType.TRICK_QUESTION -> generateTrickQuestion()
            QuestionType.REVERSE_CALCULATION -> generateReverseCalculation(level)
        }
    }

    // BASIC
    private fun generateBasicQuestion(level: Level): Question {
        val op = Operation.entries.toTypedArray().random()
        val (a, b, result) = generateOperandsAndResult(op, level)
        val options = generateSmartOptions(result)
        val correctIndex = options.indexOf(result)
        return Question("$a ${op.symbol} $b = ?", options.map { it.toString() }, correctIndex, QuestionType.BASIC, level)
    }

    // MULTI STEP: e.g., 4 + 3 * 2
    private fun generateMultiStepQuestion(level: Level): Question {
        while (true) {
            val a = (1..10).random()
            val b = (1..10).random()
            val c = (1..10).random()
            val op1 = Operation.entries.toTypedArray().random()
            val op2 = Operation.entries.toTypedArray().random()

            val result = evalPrecise(a, b, c, op1, op2)

            if (result % 1.0 == 0.0) {
                val finalResult = result.toInt()
                val expression = "$a ${op1.symbol} $b ${op2.symbol} $c"
                val options = generateSmartOptions(finalResult)
                val correctIndex = options.indexOf(finalResult)
                return Question("$expression = ?", options.map { it.toString() }, correctIndex, QuestionType.MULTI_STEP, level)
            }
        }
    }

    private fun evalPrecise(a: Int, b: Int, c: Int, op1: Operation, op2: Operation): Double {
        val first: Double
        val second: Double

        return if (op2 == Operation.MUL || op2 == Operation.DIV) {
            second = calcPrecise(b, c, op2)
            calcPrecise(a, second, op1)
        } else {
            first = calcPrecise(a, b, op1)
            calcPrecise(first, c, op2)
        }
    }

    private fun calcPrecise(x: Number, y: Number, op: Operation): Double {
        return when (op) {
            Operation.ADD -> x.toDouble() + y.toDouble()
            Operation.SUB -> x.toDouble() - y.toDouble()
            Operation.MUL -> x.toDouble() * y.toDouble()
            Operation.DIV -> {
                if (y.toDouble() == 0.0) Double.NaN
                else x.toDouble() / y.toDouble()
            }
        }
    }

    // COMPARE EXPRESSIONS
    private fun generateCompareQuestion(level: Level): Question {
        val a1 = (1..20).random()
        val b1 = (1..10).random()
        val op1 = Operation.entries.toTypedArray().random()
        val expr1 = "$a1 ${op1.symbol} $b1"
        val res1 = calc(a1, b1, op1)

        val a2 = (1..20).random()
        val b2 = (1..10).random()
        val op2 = Operation.entries.toTypedArray().random()
        val expr2 = "$a2 ${op2.symbol} $b2"
        val res2 = calc(a2, b2, op2)

        val question = "$expr1 ... $expr2"
        val answer = when {
            res1 < res2 -> "<"
            res1 == res2 -> "="
            else -> ">"
        }
        val options = listOf("<", "=", ">")
        val correctIndex = options.indexOf(answer)

        return Question(question, options, correctIndex, QuestionType.COMPARE_EXPRESSIONS, level)
    }

    // FILL IN THE BLANK
    private fun generateFillInBlank(level: Level): Question {
        val a = (1..20).random()
        val b = (1..20).random()
        val result = a + b
        val question = "__ + $b = $result"
        val options = generateSmartOptions(a)
        val correctIndex = options.indexOf(a)
        return Question(question, options.map { it.toString() }, correctIndex, QuestionType.FILL_IN_BLANK, level)
    }

    // FIND CORRECT ONE
    private fun generateFindCorrect(level: Level): Question {
        val correctExpr = generateRandomExpression()
        val correctResult = evalExpr(correctExpr)

        val wrongExpressions = mutableSetOf<String>()
        while (wrongExpressions.size < 3) {
            val wrong = mutateExpressionWrongly(correctExpr)
            if (wrong != correctExpr && evalExpr(wrong) != correctResult) {
                wrongExpressions.add(wrong)
            }
        }

        val allOptions = (wrongExpressions + correctExpr).shuffled()
        val correctIndex = allOptions.indexOf(correctExpr)

        return Question(
            questionText = "Chọn biểu thức đúng:",
            options = allOptions,
            correctIndex = correctIndex,
            type = QuestionType.FIND_CORRECT_ONE,
            level = level
        )
    }

    private fun mutateExpressionWrongly(expr: String): String {
        val parts = expr.split(" ")
        if (parts.size < 3) return expr

        return when ((0..2).random()) {
            0 -> {
                val ops = listOf("+", "-", "×", "÷").filter { it != parts[1] }
                "${parts[0]} ${ops.random()} ${parts[2]} = ${parts[4]}"
            }
            1 -> {
                "${parts[2]} ${parts[1]} ${parts[0]} = ${parts[4]}"
            }
            2 -> {
                // Đổi kết quả cho sai
                val wrongResult = parts[4].toInt() + (-5..5).filter { it != 0 }.random()
                "${parts[0]} ${parts[1]} ${parts[2]} = $wrongResult"
            }
            else -> expr
        }
    }

    // TRICK QUESTION
    private fun generateTrickQuestion(): Question {
        while (true) {
            val type = TrickType.entries.toTypedArray().random()
            val (expr, correct, wrongOptions) = when (type) {
                TrickType.ADD_THEN_MUL -> {
                    val a = (1..10).random()
                    val b = (1..5).random()
                    val c = (1..5).random()
                    val expr = "$a + $b × $c"
                    val correct = a + b * c
                    val wrong = listOf((a + b) * c, a * b + c, a + b + c)
                    Triple(expr, correct, wrong)
                }

                TrickType.MUL_THEN_ADD -> {
                    val a = (1..5).random()
                    val b = (1..10).random()
                    val c = (1..5).random()
                    val expr = "$a × $b + $c"
                    val correct = a * b + c
                    val wrong = listOf(a * (b + c), a + b * c, (a + b) * c)
                    Triple(expr, correct, wrong)
                }

                TrickType.WITH_ZERO -> {
                    val a = (5..15).random()
                    val b = (1..10).random()
                    val expr = "$a + $b × 0"
                    val correct = a + b * 0
                    val wrong = listOf(0, a * b, a + b)
                    Triple(expr, correct, wrong)
                }

                TrickType.WITH_NEGATIVE -> {
                    val a = (1..10).random()
                    val b = (1..5).random()
                    val expr = "$a - -$b"
                    val correct = a - -b
                    val wrong = listOf(a - b, b - a, a + b - 1)
                    Triple(expr, correct, wrong)
                }

                TrickType.WITH_PAREN -> {
                    val a = (1..5).random()
                    val b = (1..5).random()
                    val c = (1..5).random()
                    val expr = "($a + $b) × $c"
                    val correct = (a + b) * c
                    val wrong = listOf(a + b * c, a * b + c, a + b + c)
                    Triple(expr, correct, wrong)
                }
            }

            val options = (wrongOptions + correct).shuffled()
            val correctIndex = options.indexOf(correct)

            // Đảm bảo không trùng đáp án
            if (options.distinct().size == 4) {
                return Question(
                    questionText = "$expr = ?",
                    options = options.map { it.toString() },
                    correctIndex = correctIndex,
                    type = QuestionType.TRICK_QUESTION,
                    level = Level.MEDIUM
                )
            }
        }
    }

    // REVERSE CALCULATION
    private fun generateReverseCalculation(level: Level): Question {
        val sum = (10..30).random()
        val a = (1 until sum).random()
        val b = sum - a
        val question = "Tổng hai số là $sum. Một số là $a. Số còn lại là?"
        val options = generateSmartOptions(b)
        val correctIndex = options.indexOf(b)
        return Question(question, options.map { it.toString() }, correctIndex, QuestionType.REVERSE_CALCULATION, level)
    }

    // Helpers
    private fun generateSmartOptions(correct: Int): List<Int> {
        val options = mutableSetOf(correct)
        val range = maxOf(3, correct / 5)
        while (options.size < 4) {
            val fake = correct + (-range..range).random()
            if (fake != correct && fake >= 0) options.add(fake)
        }
        return options.shuffled()
    }

    private fun calc(a: Int, b: Int, op: Operation): Int {
        return when (op) {
            Operation.ADD -> a + b
            Operation.SUB -> a - b
            Operation.MUL -> a * b
            Operation.DIV -> if (b != 0) a / b else 0
        }
    }

    private fun eval(a: Int, b: Int, c: Int, op1: Operation, op2: Operation): Int {
        return try {
            when {
                (op2 == Operation.MUL || op2 == Operation.DIV) -> {
                    val part2 = calc(b, c, op2)
                    calc(a, part2, op1)
                }
                else -> {
                    val part1 = calc(a, b, op1)
                    calc(part1, c, op2)
                }
            }
        } catch (e: Exception) {
            0
        }
    }

    private fun generateRandomExpression(): String {
        val a = (1..10).random()
        val b = (1..10).random()
        val op = Operation.entries.toTypedArray().random()
        return "$a ${op.symbol} $b = ${calc(a, b, op)}"
    }

    private fun evalExpr(expr: String): Int {
        return try {
            val match = Regex("""(\d+)\s*([\+\-\×÷])\s*(\d+)\s*=\s*(\d+)""").find(expr)
                ?: return -1
            val (aStr, opStr, bStr, resultStr) = match.destructured
            val a = aStr.toInt()
            val b = bStr.toInt()
            val expected = resultStr.toInt()
            val actual = calc(a, b, when (opStr) {
                "+" -> Operation.ADD
                "-" -> Operation.SUB
                "×" -> Operation.MUL
                "÷" -> Operation.DIV
                else -> return -1
            })
            if (actual == expected) expected else -1
        } catch (e: Exception) {
            -1
        }
    }

    private fun generateOperandsAndResult(op: Operation, level: Level): Triple<Int, Int, Int> {
        return when (op) {
            Operation.ADD -> {
                val a = (1..50).random()
                val b = (1..50).random()
                Triple(a, b, a + b)
            }

            Operation.SUB -> {
                val a = (10..100).random()
                val b = (1 until a).random()
                Triple(a, b, a - b)
            }

            Operation.MUL -> {
                val a = (1..12).random()
                val b = (1..12).random()
                Triple(a, b, a * b)
            }

            Operation.DIV -> {
                val b = (1..12).random()
                val result = (1..12).random()
                val a = b * result
                Triple(a, b, result)
            }
        }
    }
}
