![Header](https://user-images.githubusercontent.com/22205373/198927737-e8d7aadf-713f-41c2-a90c-c62b9c24bae8.png#gh-light-mode-only)
![Header](https://user-images.githubusercontent.com/22205373/198927777-d3c048e6-ab3a-43dd-b69d-bd593e1db058.png#gh-dark-mode-only)

[![License](https://img.shields.io/badge/License-GNU%20v3.0-white.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.7.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.2.1-blue.svg?logo=android)](https://developer.android.com/jetpack/compose)

[![](https://github.com/gabrieldrn/Konstellation/workflows/CI/badge.svg?branch=develop)]()

A plotting library based on Jetpack Compose.

**This library is still a work in progress and is being actively worked on.**

<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://user-images.githubusercontent.com/22205373/200699663-3e5a6a26-2cb5-4afb-8724-fb9a3b2bc9b5.png">
    <source media="(prefers-color-scheme: light)" srcset="https://user-images.githubusercontent.com/22205373/200699641-26583508-51ed-4e9e-b3de-1ab992df5c33.png">
    <img alt="Demo preview" src="https://user-images.githubusercontent.com/22205373/200699663-3e5a6a26-2cb5-4afb-8724-fb9a3b2bc9b5.png" width="50%">
  </picture>
</p>

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
