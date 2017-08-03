package dw.zm.com.btbproject.entry

/**
 * Created by aaron on 2017/8/3.
 */
class User {
    var info = UserInfo()
    val result : Boolean = false
}

class UserInfo {
    val funds = Funds()
}

class Funds {
    val asset = Asset()
    val free = Free()
    val freezed = Free()
}

class Asset {
    val net: String = "0"
    val total: String = "0";
}

class Free {
    val ltc: String = "0"
    val btc: String = "0"
    val eth: String = "0"
    val cny: String = "0"
}