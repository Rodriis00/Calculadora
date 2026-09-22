package com.rodrigodev.calculadora.presentation

data class CalculatorState(
    val expression: String = "",
    val result: String = "",
    val memory: Double = 0.0
)