package com.lazyracoon.grem.ui.overlay

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.unit.IntSize
import grem.shared.generated.resources.Res
import kotlinx.coroutines.delay
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AnimatedWebp(
    resourcePath: String,
    modifier: Modifier = Modifier,
    onDurationLoaded: (Long) -> Unit,
) {
    var frameVersion by remember { mutableStateOf(0) }
    val bitmap = remember { Bitmap() }

    LaunchedEffect(resourcePath) {
        try {
            val bytes = Res.readBytes(resourcePath)
            val skiaCodec = Codec.makeFromData(Data.makeFromBytes(bytes))
            
            val total = skiaCodec.framesInfo.sumOf { it.duration.toLong() }
            onDurationLoaded(total)

            val frameCount = skiaCodec.frameCount
            if (frameCount > 0) {
                bitmap.allocPixels(skiaCodec.imageInfo)
                
                while (true) {
                    for (i in 0 until frameCount) {
                        skiaCodec.readPixels(bitmap, i)
                        frameVersion++
                        
                        val duration = skiaCodec.framesInfo[i].duration
                        delay(duration.toLong().coerceAtLeast(10L).milliseconds)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    key(frameVersion) {
        if (frameVersion > 0) {
            Canvas(modifier = modifier) {
                drawImage(
                    image = bitmap.asComposeImageBitmap(),
                    dstSize = IntSize(size.width.toInt(), size.height.toInt())
                )
            }
        }
    }
}
