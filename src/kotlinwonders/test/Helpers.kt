package kotlinwonders.test

import kotlinwonders.data.Card

fun cardByName(name: String) = Card.age1.first { it.name in name }
fun cardsByName(vararg names: String) = Card.age1.filter { it.name in names }

fun <T : Collection<*>> assertTheSameSet(s1: T, s2: T) {
    val set1 = s1.toSet()
    val set2 = s2.toSet()
    assert(set1 == set2) {
        """
        Set not the same
        s1 - s2:
        ${(set1 - set2).joinToString(separator = "\n")}
        s2 - s1:
        ${(set2 - set1).joinToString(separator = "\n")}
        s1:
        ${(set1).joinToString(separator = "\n")}
        s2:
        ${(set2).joinToString(separator = "\n")}
        """.trimMargin()
    }
}

fun <T: Collection<*>> assertTheSame(f: T, s: T){
    assert(f.toSet() == s.toSet(), { "There is $f and should be $s \nDifference is ${f - s}\nor ${s - f}" } )
}

fun assertThrowsException(f: ()->Unit) = try {
    f()
    true
} catch (t: Throwable) {
    false
}

fun <T> assertContains(element: T, collection: Collection<T>) {
    assert(element in collection) {
        """
        No element in collection
        Element: $element
        Collection:
        ${collection.toSet().joinToString(separator = "\n")}
        """.trimMargin()
    }
}

fun <T> assertEquals(e1: T, e2: T) {
    assert(e1 == e2) {
        "Elements not the same. E1: $e1, e2 : $e2"
    }
}