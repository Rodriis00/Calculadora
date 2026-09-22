# 📱 Calculadora Android — Evolución y Arquitectura

Aplicación móvil nativa para Android desarrollada en **Kotlin** bajo los principios de **Clean Architecture** y el patrón **MVVM (Model-View-ViewModel)** con **Jetpack Compose**

---

## 👥 Equipo de Desarrollo

* **Medel Cortés Logan Yarom**
* **Ordoñez Gómez Leonardo**
* **Sandoval Jiménez Rodrigo**

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose
* **Arquitectura:** Clean Architecture + MVVM
* **Manejo de Estado y Concurrencia:** StateFlow, Coroutines, ViewModel
* **Motor Matemático:** Parser descendente recursivo con soporte para jerarquía PEMDAS

---

## 🚀 Historial de Versiones y Evolución del Proyecto

### 📌 Versión 1.0 — Fundamentos y Arquitectura Base
* **Cálculo de Operaciones Básicas:** Implementación de suma (`+`), resta (`-`), multiplicación (`*`) y división (`/`)
* **Jerarquía de Operaciones (PEMDAS):** Procesamiento de expresiones complejas con precedencia aritmética estándar.
* **Validación Decimal:** Control estricto para evitar la duplicación del punto decimal (`.`) en un mismo operando.
* **Funciones de Edición:** Incorporación de borrado carácter por carácter (`⌫`) y limpieza de pantalla (`C`).
* **Diseño Inicial:** Botones de geometría circular y tema claro funcional.

---

### 📌 Versión 2.0 — Robustez Matemática, Control de Errores y UI Refinada
* **Manejo Seguro de División por Cero:** Intercepción de valores indeterminados (`Infinity` y `NaN`), transformándolos en un estado controlado de `"Error"` sin provocar bloqueos (*crashes*).
* **Cambio de Signo Dinámico (`+/-`):** Posibilidad de invertir el signo del operando o del resultado actual.
* **Formateo de Precisión Decimal:** Redondeo y visualización a un máximo de dos decimales con `RoundingMode.HALF_UP`.
* **Documentación Formal:** Generación de la especificación técnica de requerimientos funcionales y no funcionales en PDF.

---

### 📌 Versión 3.0 — Acumuladores de Memoria, Modo Oscuro y Aseguramiento de Calidad
* **Botones de Acumulación de Memoria:**
  * `MC` *(Memory Clear)*: Restablece el valor de la memoria a `0.0`.
  * `MR` *(Memory Recall)*: Recupera e inyecta el valor acumulado directamente en la expresión de cálculo activa.
  * `M+` *(Memory Add)*: Suma el valor o resultado en pantalla a la memoria persistente.
  * `M-` *(Memory Subtract)*: Resta el valor o resultado en pantalla de la memoria persistente.
* **Porcentaje Comercial Contextual (`%`):** Lógica matemática avanzada capaz de resolver aumentos y descuentos automáticos (ejemplo: `1000000 - 20%` evaluado dinámicamente como `1000000 - 200000`).
* **Rediseño UI Completo:**
  * Botones con factor de forma cuadrado y esquinas redondeadas (`RoundedCornerShape`).
  * Paleta en **Modo Oscuro** (`DarkTheme`): fondo `#121212`, botones `#2C2C2C` / `#3A7BD5` y contraste de texto claro `#E0E0E0`.
* **Pruebas y QA:** Elaboración y ejecución del Plan e Informe de Pruebas cubriendo pruebas de **Unidad**, **Integración** y **Sistema** con 100% de casos aprobados.

---

## 📂 Estructura del Proyecto

```text
app/src/main/java/com/rodrigodev/calculadora/
├── domain/
│   └── usecase/
│       └── EvaluateExpressionUseCase.kt   # Motor de análisis sintáctico y evaluación
├── presentation/
│   ├── CalculatorAction.kt                # Eventos y acciones de usuario
│   ├── CalculatorState.kt                 # Estado inmutable de la UI y memoria
│   └── CalculatorViewModel.kt             # Lógica de presentación y negocio
└── ui/
    ├── components/
    │   └── CalculatorButton.kt            # Componente reutilizable de botón
    ├── screen/
    │   └── CalculatorScreen.kt            # Maquetación principal de la interfaz
    └── theme/
        ├── Color.kt                       # Paleta cromática Dark Mode
        └── Theme.kt                       # Configuración global del tema Compose
