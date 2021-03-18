import java.io.File

fun orders(date: String):List<String> {
    val bf = File("orders.csv").bufferedReader()
    val fileRead = mutableListOf<String>()
    val dateIDs = mutableListOf<String>()
    bf.useLines { lines -> lines.forEach { fileRead.add(it) } }
    fileRead.forEach {
        val v = it.substring(0, it.indexOf(','))
        val v2 = it.substring(it.indexOf(',') + 1, it.indexOf('T'))
        if (date == it.substring(it.indexOf(',') + 1, it.indexOf('T')))
            dateIDs.add(it.substring(0, it.indexOf(',')))
    }
    return dateIDs
}



fun main() {
    orders("2021-01-18")
}
