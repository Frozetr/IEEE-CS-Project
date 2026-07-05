package ieee.cs.isik.platformergaeme.util

import kotlin.math.abs
import kotlin.math.exp

fun accelerate(
    currentSpeed: Float,
    targetSpeed: Float,
    sign: Byte,
    delta: Float,
    sharpness: Float = 15f
): Float {
    val targetVelocity = targetSpeed * sign.toFloat()

    val decay = exp((-sharpness * delta).toDouble()).toFloat()

    var newSpeed = targetVelocity + (currentSpeed - targetVelocity) * decay

    if (sign.toInt() == 0 && abs(newSpeed) < (targetSpeed * 0.05f)) {
        newSpeed = 0f
    }

    return newSpeed
}
