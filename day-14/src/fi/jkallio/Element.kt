package fi.jkallio

class Element(val name: String, var amount: Long = 0) {

    override fun toString(): String {
        return "$amount($name)"
    }
}