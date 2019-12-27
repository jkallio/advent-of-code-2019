package fi.jkallio

class Reaction(val result:Element, val inputs:List<Element>) {

    override fun toString(): String {
        var out = "$result = "
        var inputCount = 0
        inputs.forEach {
            if (inputCount > 0) {
                out += " + "
            }
            out += it
            inputCount += 1
        }
        return out
    }
}