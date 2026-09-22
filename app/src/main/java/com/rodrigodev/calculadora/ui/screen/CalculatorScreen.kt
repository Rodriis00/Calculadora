package com.rodrigodev.calculadora.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodrigodev.calculadora.presentation.CalculatorAction
import com.rodrigodev.calculadora.presentation.CalculatorViewModel
import com.rodrigodev.calculadora.ui.components.CalculatorButton
import com.rodrigodev.calculadora.ui.theme.DarkBackground
import com.rodrigodev.calculadora.ui.theme.DarkButtonBlue
import com.rodrigodev.calculadora.ui.theme.DarkButtonGray
import com.rodrigodev.calculadora.ui.theme.TextLight

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    modifier: Modifier
) {
    val state by viewModel.state.collectAsState()
    val buttonSpacing = 8.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = state.expression,
                fontSize = 32.sp,
                color = TextLight.copy(alpha = 0.6f),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.result,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val buttons = listOf(
            listOf("MC", "MR", "M+", "M-"),
            listOf("C", "⌫", "%", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("+/-", "0", ".", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                row.forEach { symbol ->
                    val isAction = symbol in listOf("C", "⌫", "%", "/", "*", "-", "+", "=", "MC", "MR", "M+", "M-")
                    val bgColor = if (isAction) DarkButtonBlue else DarkButtonGray
                    val textColor = TextLight

                    val buttonModifier =
                        if (symbol == "0") Modifier.weight(2f) else Modifier.weight(1f)

                    CalculatorButton(
                        symbol = symbol,
                        backgroundColor = bgColor,
                        textColor = textColor,
                        modifier = Modifier.weight(1f)
                    ) {
                        when (symbol) {
                            "MC" -> viewModel.onAction(CalculatorAction.MemoryClear)
                            "MR" -> viewModel.onAction(CalculatorAction.MemoryRecall)
                            "M+" -> viewModel.onAction(CalculatorAction.MemoryAdd)
                            "M-" -> viewModel.onAction(CalculatorAction.MemorySubtract)
                            "C" -> viewModel.onAction(CalculatorAction.Clear)
                            "⌫" -> viewModel.onAction(CalculatorAction.Delete)
                            "%" -> viewModel.onAction(CalculatorAction.Percentage)
                            "/" -> viewModel.onAction(CalculatorAction.Operator("/"))
                            "*" -> viewModel.onAction(CalculatorAction.Operator("*"))
                            "-" -> viewModel.onAction(CalculatorAction.Operator("-"))
                            "+" -> viewModel.onAction(CalculatorAction.Operator("+"))
                            "=" -> viewModel.onAction(CalculatorAction.Calculate)
                            "." -> viewModel.onAction(CalculatorAction.Decimal)
                            "+/-" -> viewModel.onAction(CalculatorAction.ToggleSign)
                            else -> viewModel.onAction(CalculatorAction.Number(symbol.toInt()))
                        }
                    }
                }
            }
        }
    }
}