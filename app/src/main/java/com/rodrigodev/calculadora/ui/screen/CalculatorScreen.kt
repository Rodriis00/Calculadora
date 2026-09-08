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
import com.rodrigodev.calculadora.ui.theme.BackgroundWhite
import com.rodrigodev.calculadora.ui.theme.ButtonBlue
import com.rodrigodev.calculadora.ui.theme.ButtonGray
import com.rodrigodev.calculadora.ui.theme.TextPrimary

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
            .background(BackgroundWhite)
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
                color = TextPrimary.copy(alpha = 0.6f),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.result,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val buttons = listOf(
            listOf("C", "⌫", "%", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                row.forEach { symbol ->
                    val isAction = symbol in listOf("C", "⌫", "%", "/", "*", "-", "+", "=")
                    val bgColor = if (isAction) ButtonBlue else ButtonGray
                    val textColor = if (isAction) BackgroundWhite else TextPrimary

                    val buttonModifier =
                        if (symbol == "0") Modifier.weight(2f) else Modifier.weight(1f)

                    CalculatorButton(
                        symbol = symbol,
                        backgroundColor = bgColor,
                        textColor = textColor,
                        modifier = buttonModifier
                    ) {
                        when (symbol) {
                            "C" -> viewModel.onAction(CalculatorAction.Clear)
                            "⌫" -> viewModel.onAction(CalculatorAction.Delete)
                            "%" -> viewModel.onAction(CalculatorAction.Percentage)
                            "/" -> viewModel.onAction(CalculatorAction.Operator("/"))
                            "*" -> viewModel.onAction(CalculatorAction.Operator("*"))
                            "-" -> viewModel.onAction(CalculatorAction.Operator("-"))
                            "+" -> viewModel.onAction(CalculatorAction.Operator("+"))
                            "=" -> viewModel.onAction(CalculatorAction.Calculate)
                            "." -> viewModel.onAction(CalculatorAction.Decimal)
                            else -> viewModel.onAction(CalculatorAction.Number(symbol.toInt()))
                        }
                    }
                }
            }
        }
    }
}