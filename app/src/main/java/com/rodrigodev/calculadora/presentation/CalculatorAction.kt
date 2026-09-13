package com.rodrigodev.calculadora.presentation

sealed interface CalculatorAction {
    data class Number(val number: Int) : CalculatorAction
    data class Operator(val operator: String) : CalculatorAction
    object Clear : CalculatorAction
    object Delete : CalculatorAction
    object Decimal : CalculatorAction
    object Calculate : CalculatorAction
    object Percentage : CalculatorAction
    object  ToggleSign : CalculatorAction
}