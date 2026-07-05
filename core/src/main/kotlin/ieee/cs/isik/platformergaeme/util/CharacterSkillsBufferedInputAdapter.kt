package ieee.cs.isik.platformergaeme.util

import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Queue
import com.badlogic.gdx.utils.TimeUtils

open class CharacterSkillsBufferedInputAdapter(
    val processor: (input: BufferedInput) -> Unit
) : ActableInputAdapter() {

    enum class InputType {
        JUMP, ATTACK, CONSUME, MOVE
    }

    class BufferedInput(var type: InputType, var timestamp: Long)

    private val buffer = Queue<BufferedInput>()
    private val bufferWindow = 200L // 200ms buffer window

    override fun keyDown(keycode: Int): Boolean = when (keycode) {
            Input.Keys.SPACE -> {
                buffer.addLast(BufferedInput(InputType.JUMP, TimeUtils.millis()))
                true
            }

            Input.Keys.K -> {
                buffer.addLast(BufferedInput(InputType.ATTACK, TimeUtils.millis()))
                true
            }

            Input.Keys.J -> {
                buffer.addLast(BufferedInput(InputType.CONSUME, TimeUtils.millis()))
                true
            }

            Input.Keys.A -> {
                buffer.addLast(BufferedInput(InputType.MOVE, TimeUtils.millis()))
                false
            }

            Input.Keys.D -> {
                buffer.addLast(BufferedInput(InputType.MOVE, TimeUtils.millis()))
                false
            }

            else -> false
        }

    override fun act() {
        // Consume from the buffer
        while (buffer.size > 0) {
            val input = buffer.removeFirst()

            // Process time-decay for expired inputs (optional)
            if (TimeUtils.millis() - input.timestamp > bufferWindow) {
                continue  // Discard old input
            }

            // Handle command
            processor(input)
        }
    }
}
