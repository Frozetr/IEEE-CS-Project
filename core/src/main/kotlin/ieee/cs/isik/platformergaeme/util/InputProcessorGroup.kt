package ieee.cs.isik.platformergaeme.util

import com.badlogic.gdx.InputProcessor
import java.util.LinkedList

class InputProcessorGroup(
    vararg processors: InputProcessor
) : InputProcessor, LinkedList<InputProcessor>(
    processors.toList()
) {

    override fun keyDown(keycode: Int): Boolean {
        for (processor in this)
            if(processor.keyDown(keycode))
                return true

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        for (processor in this)
            if(processor.keyUp(keycode))
                return true

        return false
    }

    override fun keyTyped(character: Char): Boolean {
        for (processor in this)
            if(processor.keyTyped(character))
                return true

        return false
    }

    override fun touchDown(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int
    ): Boolean {
        for (processor in this)
            if(processor.touchDown(screenX, screenY, pointer, button))
                return true

        return false
    }

    override fun touchUp(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int
    ): Boolean {
        for (processor in this)
            if(processor.touchUp(screenX, screenY, pointer, button))
                return true
        return false
    }

    override fun touchCancelled(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int
    ): Boolean {
        for (processor in this)
            if(processor.touchCancelled(screenX, screenY, pointer, button))
                return true
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        for (processor in this)
            if(processor.touchDragged(screenX, screenY, pointer))
                return true
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        for (processor in this)
            if(processor.mouseMoved(screenX, screenY))
                return true
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        for (processor in this)
            if(processor.scrolled(amountX, amountY))
                return true
        return false
    }
}
