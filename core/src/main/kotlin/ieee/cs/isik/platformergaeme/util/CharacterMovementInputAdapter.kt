package ieee.cs.isik.platformergaeme.util

import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.TimeUtils
import ieee.cs.isik.platformergaeme.game.CharacterEntity
import ieee.cs.isik.platformergaeme.game.Entity
import ieee.cs.isik.platformergaeme.game.IEntity
import ieee.cs.isik.platformergaeme.game.StateMaterial
import ieee.cs.isik.platformergaeme.util.CharacterSkillsBufferedInputAdapter.BufferedInput
import ieee.cs.isik.platformergaeme.util.CharacterSkillsBufferedInputAdapter.InputType

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
