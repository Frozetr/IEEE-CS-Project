package ieee.cs.isik.platformergaeme.game

data class FixtureData(
    val type: Type
) {
    enum class Type  {
        CHARACTER,
        SOLID,
        SPIKE,
    }
}

