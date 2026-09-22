package com.rodrigodev.calculadora.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigodev.calculadora.domain.usecase.EvaluateExpressionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val evaluateExpression: EvaluateExpressionUseCase = EvaluateExpressionUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operator -> enterOperator(action.operator)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> _state.update { it.copy(expression = "", result = "") }
            is CalculatorAction.Delete -> deleteLast()
            is CalculatorAction.Calculate -> calculateResult()
            is CalculatorAction.Percentage -> enterPercentage()
            is CalculatorAction.ToggleSign -> toggleSign()
            is CalculatorAction.MemoryClear -> _state.update { it.copy(memory = 0.0) }
            is CalculatorAction.MemoryRecall -> recallMemory()
            is CalculatorAction.MemoryAdd -> addToMemory()
            is CalculatorAction.MemorySubtract -> subtractFromMemory()
        }
    }

    private fun getCurrentValue(): Double {
        val currentState = _state.value

        if (currentState.result.isNotBlank() && currentState.result != "Error") {
            return currentState.result.replace(",", "").toDoubleOrNull() ?: 0.0
        }

        if (currentState.expression.isNotBlank()) {
            val directNumber = currentState.expression.replace(",", "").toDoubleOrNull()
            if (directNumber != null) return directNumber

            val evaluated = evaluateExpression(currentState.expression)
            if (evaluated.isNotBlank() && evaluated != "Error") {
                return evaluated.replace(",", "").toDoubleOrNull() ?: 0.0
            }
        }
        return 0.0
    }

    private fun addToMemory() {
        val value = getCurrentValue()
        _state.update { it.copy(memory = it.memory + value) }
    }

    private fun subtractFromMemory() {
        val value = getCurrentValue()
        _state.update { it.copy(memory = it.memory - value) }
    }

    private fun recallMemory() {
        val mem = _state.value.memory
        val memStr = if (mem % 1.0 == 0.0) mem.toLong().toString() else mem.toString()

        _state.update { current ->
            if (current.expression.isBlank() || current.expression == "0") {
                current.copy(expression = memStr)
            } else {
                current.copy(expression = current.expression + memStr)
            }
        }
    }

    private fun enterNumber(number: Int) {
        _state.update { it.copy(expression = it.expression + number) }
    }

    private fun enterOperator(operator: String) {
        val currentExpression = _state.value.expression
        if (currentExpression.isNotBlank() && currentExpression.last().isDigit()) {
            _state.update { it.copy(expression = it.expression + operator) }
        }
    }

    private fun enterDecimal() {
        val currentExpression = _state.value.expression
        val lastNumber = currentExpression.split('+', '-', '*', '/').lastOrNull() ?: ""

        if (!lastNumber.contains(".")) {
            _state.update { it.copy(expression = it.expression + ".") }
        }
    }

    private fun deleteLast() {
        val currentExpression = _state.value.expression
        if (currentExpression.isNotEmpty()) {
            _state.update { it.copy(expression = currentExpression.dropLast(1)) }
        }
    }

    private fun calculateResult() {
        viewModelScope.launch(Dispatchers.Default) {
            val result = evaluateExpression(_state.value.expression)
            _state.update { it.copy(result = result) }
        }
    }

    private fun enterPercentage() {
        val currentExpression = _state.value.expression
        if (currentExpression.isBlank()) return

        val delimiters = charArrayOf('+', '-', '*', '/')
        val lastOperatorIndex = currentExpression.lastIndexOfAny(delimiters)

        if (lastOperatorIndex == -1) {
            val number = currentExpression.toDoubleOrNull()
            if (number != null) {
                _state.update { it.copy(expression = (number / 100.0).toString()) }
            }
            return
        }

        val prefix = currentExpression.substring(0, lastOperatorIndex)
        val operator = currentExpression[lastOperatorIndex]
        val currentNumberStr = currentExpression.substring(lastOperatorIndex + 1)
        val currentNumber = currentNumberStr.toDoubleOrNull() ?: return

        if (operator == '+' || operator == '-') {
            val baseValueStr = evaluateExpression(prefix)
            val baseValue = baseValueStr.replace(",", "").toDoubleOrNull() ?: 0.0
            val percentageValue = baseValue * (currentNumber / 100.0)
            _state.update { it.copy(expression = "$prefix$operator$percentageValue") }
        } else {
            val percentageValue = currentNumber / 100.0
            _state.update { it.copy(expression = "$prefix$operator$percentageValue") }
        }
    }

    private fun toggleSign() {
        val currentResult = _state.value.result
        if (currentResult.isNotBlank() && currentResult != "Error") {
            val newResult = if (currentResult.startsWith("-")) {
                currentResult.drop(1)
            } else {
                "-$currentResult"
            }
            _state.update { it.copy(result = newResult) }
        }
    }
}