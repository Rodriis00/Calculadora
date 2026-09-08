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
            is CalculatorAction.Clear -> _state.update { CalculatorState() }
            is CalculatorAction.Delete -> deleteLast()
            is CalculatorAction.Calculate -> calculateResult()
            is CalculatorAction.Percentage -> enterPercentage()
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
        val lastNumber = currentExpression.split('+', '-', 'x', '/').lastOrNull() ?: ""

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

        val numberPart = if (lastOperatorIndex != -1) {
            currentExpression.substring(lastOperatorIndex + 1)
        } else {
            currentExpression
        }

        val prefix = if (lastOperatorIndex != -1) {
            currentExpression.substring(0, lastOperatorIndex + 1)
        } else {
            ""
        }

        val parsedNumber = numberPart.toDoubleOrNull()
        if (parsedNumber != null) {
            val percentageValue = parsedNumber / 100.0
            _state.update { it.copy(expression = prefix + percentageValue) }
        }
    }
}