package pl.marcinmoskala.kotlindownders.utills

fun <T> List<T>.shuffle(): List<T> {
    val a = toMutableList()
    var n = a.size
    while (n > 1) {
        val k = Random.gen.nextInt(n--)
        val t = a[n]
        a[n] = a[k]
        a[k] = t
    }
    return a
}