![Konstellation logo](https://user-images.githubusercontent.com/22205373/112414032-b8319500-8cf7-11eb-97d0-720f2f202cde.png)

A plotting library based on Jetpack Compose.

**This library is still a work in progress and is being actively worked on. Here's what it can draw so far:**

![device-2021-07-29-231041_framed](https://user-images.githubusercontent.com/22205373/127594170-d67b0d1b-11a4-49d0-b610-fdd3258c03b8.png)

https://user-images.githubusercontent.com/22205373/127596495-450aac67-c897-4bc7-8403-09c35fa427e8.mp4

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
