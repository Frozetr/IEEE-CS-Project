package ieee.cs.isik.platformergaeme.util

import com.badlogic.gdx.Input

class CharacterMovementInputAdapter(
    val processor: (Byte, Boolean) -> Unit
) : ActableInputAdapter() {

    private var moveSign: Byte = 0;
    private var lookingLeft: Boolean = false

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A -> {
                moveSign--
            }

            Input.Keys.D -> {
                moveSign++
            }

            else -> return false
        }


        if(moveSign != 0.toByte())
            lookingLeft = moveSign != 1.toByte()

        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A -> {
                moveSign++
            }

            Input.Keys.D -> {
                moveSign--
            }

            else -> return false
        }
        if(moveSign != 0.toByte())
            lookingLeft = moveSign != 1.toByte()

        return true
    }

    override fun act() {
        processor(moveSign, lookingLeft)
    }
}
