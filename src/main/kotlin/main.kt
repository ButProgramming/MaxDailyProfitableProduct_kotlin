import java.io.File
import java.lang.NumberFormatException
import kotlin.io.*


fun orders(date: String):List<String> {
    val bf = File("orders.csv").bufferedReader()
    val fileRead = mutableListOf<String>()
    val dateIDs = mutableListOf<String>()
    bf.useLines { lines -> lines.forEach { fileRead.add(it) } }
    fileRead.forEach {
        val idFromFile = it.substring(0, it.indexOf(','))
        val dateFromFile = it.substring(it.indexOf(',') + 1, it.indexOf('T'))
        if (date == dateFromFile)
            dateIDs.add(idFromFile)
    }
    return dateIDs
}

fun order_items(dateIDs: List<String>):Map<String, Int>{
    val bf = File("order_items.csv").bufferedReader()
    val ordersCount = mutableMapOf<String, Int>()
    val fileRead = mutableListOf<String>()
    bf.useLines { lines -> lines.forEach { fileRead.add(it) } }
    for(ID in dateIDs){
        for(line in fileRead){
            if(ID==line.substring(0, line.indexOf(','))){
                val productID = line.substring(line.indexOf(',')+1, line.lastIndexOf(','))
                val countOfProductID = line.substring(line.lastIndexOf(',')+1).toInt()
                if(ordersCount[productID]==null)
                    ordersCount[productID] = countOfProductID

                else{

                    ordersCount[productID] = countOfProductID +
                            ordersCount[productID]!!
                }
            }
        }
    }
    return ordersCount
}

fun products(ordersCount: Map<String, Int>):Map<String, Int>{
    val bf = File("products.csv").bufferedReader()
    val fileRead = mutableListOf<String>()
    bf.useLines{ lines -> lines.forEach { fileRead.add(it) } }
    val productProfit = mutableMapOf<String, Int>()

    for(line in fileRead){
        for((k,v) in ordersCount){
            val productID = line.substring(0, line.indexOf(','))
            val productName = line.substring(line.indexOf(',')+1, line.lastIndexOf(','))
            var productPrice:Int
            try {
                productPrice = line.substring(line.lastIndexOf(',') + 1).toInt()
            }catch(e:NumberFormatException){
                productPrice = -1
            }
            if(k==productID){
                if(productProfit[productName]==null){
                    productProfit[productName]=v*productPrice
                }
                else
                    productProfit[productName]=productProfit[productName]!! + v*productPrice
            }
        }
    }
    return productProfit
}

fun mostProfitableProducts(date: String){
    var max=0
    for((k,v) in products(order_items(orders(date)))){
        if(v>max) max=v
    }
    for((k,v) in products(order_items(orders(date)))){
        if(v==max) println("Most profitable product on $date: $k. Profit: $v")
    }
}

fun mostProfitableProducts(){
    for(i in 1..31){
        val date = "2021-01-"+i/10+i%10
        mostProfitableProducts(date)
    }
}


fun main() {
    mostProfitableProducts()
}
