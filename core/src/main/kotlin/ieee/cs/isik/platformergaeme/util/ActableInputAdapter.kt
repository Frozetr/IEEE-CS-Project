package ieee.cs.isik.platformergaeme.util

import com.badlogic.gdx.InputAdapter

abstract class ActableInputAdapter : InputAdapter() {
    abstract fun act()
}
