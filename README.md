![Header](https://user-images.githubusercontent.com/22205373/198927737-e8d7aadf-713f-41c2-a90c-c62b9c24bae8.png#gh-light-mode-only)
![Header](https://user-images.githubusercontent.com/22205373/198927777-d3c048e6-ab3a-43dd-b69d-bd593e1db058.png#gh-dark-mode-only)

[![License](https://img.shields.io/badge/License-GNU%20v3.0-white.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.7.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.2.1-blue.svg?logo=android)](https://developer.android.com/jetpack/compose)

[![](https://github.com/gabrieldrn/Konstellation/workflows/CI/badge.svg?branch=develop)]()

A plotting library based on Jetpack Compose.

**This library is still a work in progress and is being actively worked on.**

![Screenshot_20220927_003134_framed](https://user-images.githubusercontent.com/22205373/192433407-087166f9-9e96-4f66-bbc9-daef829dab9f.png#gh-light-mode-only)
![Screenshot_20220927_003116_framed](https://user-images.githubusercontent.com/22205373/192433474-6384bd39-751f-4120-b206-e839bba60e28.png#gh-dark-mode-only)


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
