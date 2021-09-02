![Konstellation logo - 3](https://user-images.githubusercontent.com/22205373/131760981-76444b9a-97d1-481f-8fa4-16379a841f9c.png)

[![License](https://img.shields.io/badge/License-GNU%20v3.0-white.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.5.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.0.1-blue.svg?logo=android)](https://developer.android.com/jetpack/compose)

A plotting library based on Jetpack Compose.

**This library is still a work in progress and is being actively worked on. Here's what it can draw so far:**

![device-2021-08-04-211915_framed](https://user-images.githubusercontent.com/22205373/128276471-6e20e929-10b7-4f6d-b9af-603da8d0dee2.png)

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
