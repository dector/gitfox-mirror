package gitfox.entity

data class Time(
    val isoString: String
) {
    override fun toString() = isoString
}

data class Date(
    val isoString: String
) {
    override fun toString() = isoString
}