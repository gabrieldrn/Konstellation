![Header](https://user-images.githubusercontent.com/22205373/159395017-93cd77cd-e19c-4ec3-a79d-40f1913b6f7b.png#gh-light-mode-only)
![Header](https://user-images.githubusercontent.com/22205373/159395023-0eccf032-aed6-4d71-9500-02760793651b.png#gh-dark-mode-only)

[![License](https://img.shields.io/badge/License-GNU%20v3.0-white.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.5.31-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.0.5-blue.svg?logo=android)](https://developer.android.com/jetpack/compose)

[![](https://github.com/gabrieldrn/Konstellation/workflows/CI/badge.svg?branch=develop)]()

A plotting library based on Jetpack Compose.

**This library is still a work in progress and is being actively worked on. Here's what it can draw so far:**

![device-2021-11-14-231208_framed](https://user-images.githubusercontent.com/22205373/141721568-02888a5b-60bc-4fb5-a880-cf89f5ef8f28.png)

## Usage example of a function chart

```Kotlin
@Composable
fun SinChart() {
    val infiniteTransition = rememberInfiniteTransition()
    val m by infiniteTransition.animateFloat(
        initialValue = -PI.toFloat(),
        targetValue = PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Surface(color = MaterialTheme.colors.background) {
        FunctionPlotter( //<- Composable provided by Konstellation
            chartName = "f(x) = sin(x)",
            pointSpacing = 5, //The lower the spacing, the higher the accuracy of the drawing.
            lineStyle = LineDrawStyle(color = MaterialTheme.colors.primary),
            textStyle = TextDrawStyle(color = MaterialTheme.colors.primary),
            dataXRange = -PI.toFloat() + m..PI.toFloat() + m,
            dataYRange = -2f..2f
        ) {
            sin(it) //<- Your function f(x) where [it] is x.
        }
    }
}
```
Result:

![result](https://user-images.githubusercontent.com/22205373/112779385-a19b7e80-9014-11eb-854e-ad86e6d93d52.gif)

https://user-images.githubusercontent.com/22205373/112778948-aa3f8500-9013-11eb-81aa-3f4bdb9739a5.mp4
