package ieee.cs.isik.platformergaeme.util

import com.badlogic.gdx.Input

class CharacterMovementInputAdapter(
    val processor: (Byte) -> Unit
) : ActableInputAdapter() {

    private var moveSign: Byte = 0;

    override fun keyDown(keycode: Int): Boolean = when (keycode) {
        Input.Keys.A -> {
            moveSign--
            true
        }

        Input.Keys.D -> {
            moveSign++
            true
        }


        else -> false
    }

    override fun keyUp(keycode: Int): Boolean = when (keycode) {
        Input.Keys.A -> {
            moveSign++
            true
        }

        Input.Keys.D -> {
            moveSign--
            true
        }

        else -> false
    }

    override fun act() {
        processor(moveSign)
    }
}
