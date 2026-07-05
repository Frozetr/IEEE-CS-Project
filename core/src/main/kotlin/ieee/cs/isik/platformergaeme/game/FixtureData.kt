package ieee.cs.isik.platformergaeme.game

import ieee.cs.isik.platformergaeme.game.entity.Entity

open class FixtureData(
    val type: Type
) {
    enum class Type  {
        SOLID,
        SPIKE,
    }
}
