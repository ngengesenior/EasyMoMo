package com.ngengeapps.easymomo.ui.shapes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Green

enum class CornerType {
ROUNDED,
    CUT,
    NONE

}

data class ShapeCorner(
    val type: CornerType = CornerType.NONE,
    val size:Dp = 0.dp
)

data class ShapeDecoration(
    val topStart:ShapeCorner = ShapeCorner(),
    val topEnd:ShapeCorner = ShapeCorner(),
    val bottomEnd:ShapeCorner = ShapeCorner(),
    val bottomStart:ShapeCorner = ShapeCorner()
)

class Shapeable(private val cornerSize:Dp):Shape{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        //get values in pixels. Actual size multiplied by density
        val topEndSize = cornerSize.value * density.density

        val path = Path().apply {
            moveTo(0f,0f)
            lineTo(size.width,0f)
            lineTo(size.width,size.height)
            lineTo(size.width,topEndSize)
            lineTo(topEndSize,size.height)

        }

        return Outline.Generic(path)

    }
}

@Preview
@Composable
fun ShapeViewTest() {
    /*Box(
        modifier = Modifier
            .size(150.dp)
            .clip(
                Shapeable(
                    shapeDecoration = ShapeDecoration(
                        topStart = ShapeCorner(
                            type = CornerType.CUT,
                            size = 50.dp
                        ),
                    ),
                )
            )
            .background(Color.Gray)
    ) {
    }*/
}

@Preview
@Composable
fun SmileyFace() {

    /*Canvas(modifier = Modifier.fillMaxSize()) {
        val smileyPadding = size.width * 0.15f
        drawCircle(Brush.linearGradient(colors = listOf(Color.Red,Color.Yellow)),
            center = center,
            radius = size.width /2,
            style = Stroke(width = size.width * 0.075f)
        )
        drawArc(Color.Blue,useCenter = true,
        startAngle = 0f,
        sweepAngle = 180f,
            topLeft = Offset(smileyPadding,smileyPadding),
            size = Size(size.width - (smileyPadding * 2f),size.height * (smileyPadding * 2f))
        )*/

    Canvas(modifier = Modifier.fillMaxSize()) {
        translate(size.width * 0.15f,-size.height * 0.15f){
            drawArc(Brush.linearGradient(listOf(Color.Red,Color.Green,Color.Magenta)),
                startAngle = 225f,sweepAngle = 45f,useCenter = true)

        }

        drawCircle(radius = size.width/2f,color = Color.Blue,center = Offset(size.width/2f,size.height/2f))


    }
}

@Preview
@Composable
fun FocusableSample() {
    var color by remember { mutableStateOf(Black) }
    Box(
        Modifier
            .border(2.dp, color)
            // The onFocusChanged should be added BEFORE the focusable that is being observed.
            .onFocusChanged { color = if (it.isFocused) Green else Black }
            .focusTarget()
    )
}

@Preview
@Composable
fun StampedPathEffectSample() {
    val size = 20f
    val square = Path().apply {
        lineTo(size, 0f)
        lineTo(size, size)
        lineTo(0f, size)
        close()
    }
    Column(modifier = Modifier.fillMaxHeight().wrapContentSize(Alignment.Center)) {
        val canvasModifier = Modifier.requiredSize(80.dp).align(Alignment.CenterHorizontally)

        // StampedPathEffectStyle.Morph will modify the lines of the square to be curved to fit
        // the curvature of the circle itself. Each stamped square will be rendered as an arc
        // that is fully contained by the bounds of the circle itself
        Canvas(modifier = canvasModifier) {
            drawCircle(color = Color.Blue)
            drawCircle(
                color = Color.Red,
                style = Stroke(
                    pathEffect = PathEffect.stampedPathEffect(
                        shape = square,
                        style = StampedPathEffectStyle.Morph,
                        phase = 0f,
                        advance = 30f
                    )
                )
            )
        }

        Spacer(modifier = Modifier.requiredSize(10.dp))

        // StampedPathEffectStyle.Rotate will draw the square repeatedly around the circle
        // such that each stamped square is centered on the circumference of the circle and is
        // rotated along the curvature of the circle itself
        Canvas(modifier = canvasModifier) {
            drawCircle(color = Color.Blue)
            drawCircle(
                color = Color.Red,
                style = Stroke(
                    pathEffect = PathEffect.stampedPathEffect(
                        shape = square,
                        style = StampedPathEffectStyle.Rotate,
                        phase = 0f,
                        advance = 30f
                    )
                )
            )
        }

        Spacer(modifier = Modifier.requiredSize(10.dp))

        // StampedPathEffectStyle.Translate will draw the square repeatedly around the circle
        // with the top left of each stamped square on the circumference of the circle
        Canvas(modifier = canvasModifier) {
            drawCircle(color = Color.Blue)
            drawCircle(
                color = Color.Red,
                style = Stroke(
                    pathEffect = PathEffect.stampedPathEffect(
                        shape = square,
                        style = StampedPathEffectStyle.Translate,
                        phase = 0f,
                        advance = 30f
                    )
                )
            )
        }
    }
}

