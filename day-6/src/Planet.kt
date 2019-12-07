class Planet(val id: String) {
    var parent : Planet? = null
    var orbitingPlanets = mutableListOf<Planet>()

    override fun toString(): String {
        parent?.let {
            return "${it.id})${id}"
        }
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        other as Planet
        return other.id == id
    }
}