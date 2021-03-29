![Konstellation logo](https://user-images.githubusercontent.com/22205373/112414032-b8319500-8cf7-11eb-97d0-720f2f202cde.png)

A plotting library based on Jetpack Compose.

**This library is still a work in progress and is being actively worked on. Here's what it can draw so far:**

![device-2021-03-28-191546_framed](https://user-images.githubusercontent.com/22205373/112771599-93d8ff80-8ffa-11eb-8375-ebc116ad85bc.png)

## Usage example

```Kotlin
@Composable
fun FunctionChartContent() {
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
        FunctionPlotter(
            chartName = "f(x) = sin(x)",
            pointSpacing = 5,
            lineStyle = LineDrawStyle(color = MaterialTheme.colors.primary),
            textStyle = textStyle.copy(color = MaterialTheme.colors.primary),
            dataXRange = -PI.toFloat() + m..PI.toFloat() + m,
            dataYRange = -2f..2f
        ) {
            sin(it)
        }
    }
}
```
Result:

https://user-images.githubusercontent.com/22205373/112778948-aa3f8500-9013-11eb-81aa-3f4bdb9739a5.mp4

