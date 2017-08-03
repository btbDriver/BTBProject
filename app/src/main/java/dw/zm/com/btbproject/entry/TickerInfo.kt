package dw.zm.com.btbproject.entry

/**
 * Created by aaron on 2017/8/3.
 */
class TickerInfo {
    val date: String = "0"
    val ticker = Ticker()
}

class Ticker {
    var buy = "0.0"
    var high = "0.0"
    var last = "0.0"
    var low = "0.0"
    var sell = "0.0"
    var vol = "0.0"
}