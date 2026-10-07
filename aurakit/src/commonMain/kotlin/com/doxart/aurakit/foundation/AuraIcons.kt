package com.doxart.aurakit.foundation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AuraIcons {
    val Search: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraSearch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(11f, 19f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = true, isPositiveArc = true, 11f, 3f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11f, 19f)
                close()
                moveTo(21f, 21f)
                lineTo(16.65f, 16.65f)
            }
        }.build()
    }

    val Close: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraClose",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18f, 6f)
                lineTo(6f, 18f)
                moveTo(6f, 6f)
                lineTo(18f, 18f)
            }
        }.build()
    }

    val Swap: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraSwap",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(16f, 3f)
                lineTo(20f, 7f)
                lineTo(16f, 11f)
                moveTo(4f, 7f)
                lineTo(20f, 7f)
                moveTo(8f, 21f)
                lineTo(4f, 17f)
                lineTo(8f, 13f)
                moveTo(20f, 17f)
                lineTo(4f, 17f)
            }
        }.build()
    }

    val Home: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraHome",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 9.5f)
                lineTo(12f, 2.5f)
                lineTo(21f, 9.5f)
                lineTo(21f, 20f)
                arcTo(1.5f, 1.5f, 0f, false, true, 19.5f, 21.5f)
                lineTo(4.5f, 21.5f)
                arcTo(1.5f, 1.5f, 0f, false, true, 3f, 20f)
                close()
            }
        }.build()
    }

    val Wallet: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraWallet",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 7f)
                arcTo(2f, 2f, 0f, false, true, 5f, 5f)
                lineTo(19f, 5f)
                arcTo(2f, 2f, 0f, false, true, 21f, 7f)
                lineTo(21f, 17f)
                arcTo(2f, 2f, 0f, false, true, 19f, 19f)
                lineTo(5f, 19f)
                arcTo(2f, 2f, 0f, false, true, 3f, 17f)
                close()
                moveTo(16f, 12f)
                arcTo(1f, 1f, 0f, true, true, 16f, 12.01f)
            }
        }.build()
    }

    val ArrowBack: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraArrowBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(19f, 12f)
                lineTo(5f, 12f)
                moveTo(12f, 19f)
                lineTo(5f, 12f)
                lineTo(12f, 5f)
            }
        }.build()
    }

    val Edit: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraEdit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(17f, 3f)
                arcTo(2.828f, 2.828f, 0f, isMoreThanHalf = true, isPositiveArc = true, 21f, 7f)
                lineTo(7.5f, 20.5f)
                lineTo(2f, 22f)
                lineTo(3.5f, 16.5f)
                lineTo(17f, 3f)
                close()
                moveTo(15f, 5f)
                lineTo(19f, 9f)
            }
        }.build()
    }

    val Trash: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraTrash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 6f)
                lineTo(21f, 6f)
                moveTo(19f, 6f)
                lineTo(18.2f, 19.2f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16.2f, 21f)
                lineTo(7.8f, 21f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.8f, 19.2f)
                lineTo(5f, 6f)
                moveTo(8f, 6f)
                lineTo(8f, 4f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 3f)
                lineTo(15f, 3f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 4f)
                lineTo(16f, 6f)
                moveTo(10f, 11f)
                lineTo(10f, 17f)
                moveTo(14f, 11f)
                lineTo(14f, 17f)
            }
        }.build()
    }

    val Menu: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraMenu",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 6f)
                lineTo(20f, 6f)
                moveTo(4f, 12f)
                lineTo(16f, 12f)
                moveTo(4f, 18f)
                lineTo(20f, 18f)
            }
        }.build()
    }

    val Backspace: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraBackspace",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 4f)
                lineTo(8f, 4f)
                lineTo(1f, 12f)
                lineTo(8f, 20f)
                lineTo(21f, 20f)
                arcTo(2f, 2f, 0f, false, false, 23f, 18f)
                lineTo(23f, 6f)
                arcTo(2f, 2f, 0f, false, false, 21f, 4f)
                close()
                moveTo(18f, 9f)
                lineTo(12f, 15f)
                moveTo(12f, 9f)
                lineTo(18f, 15f)
            }
        }.build()
    }
}
