package dw.zm.com.btbproject

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import java.util.HashMap
import java.util.Timer
import java.util.TimerTask
import dw.zm.com.btbproject.network.LtcService
import dw.zm.com.btbproject.network.TradeService
import dw.zm.com.btbproject.network.UserInfoService
import dw.zm.com.btbproject.network.APIUrl.API_KEY
import org.jetbrains.anko.onClick
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : BaseActivity() {

    val ltcSellButton by lazy {
        findViewById(R.id.ltcSellButton) as Button
    }
    val ltcInfoButton by lazy {
        findViewById(R.id.ltcInfoButton) as Button
    }
    val tradeButton by lazy {
        findViewById(R.id.tradeButton) as Button
    }
    val tradeInfoButton by lazy {
        findViewById(R.id.tradeInfoButton) as Button
    }
    val cancelTradeButton by lazy {
        findViewById(R.id.cancelTradeButton) as Button
    }
    val userInfoButton by lazy {
        findViewById(R.id.userInfoButton) as Button
    }
    val userCountText by lazy {
        findViewById(R.id.userCountText) as TextView
    }
    val cnyValue by lazy {
        findViewById(R.id.cnyValue) as TextView
    }
    val cny2Value by lazy {
        findViewById(R.id.cny2Value) as TextView
    }
    val ltcValue by lazy {
        findViewById(R.id.ltcValue) as TextView
    }
    val ltc2Value by lazy {
        findViewById(R.id.ltc2Value) as TextView
    }
    val btcValue by lazy {
        findViewById(R.id.btcValue) as TextView
    }
    val btc2Value by lazy {
        findViewById(R.id.btc2Value) as TextView
    }
    val ethValue by lazy {
        findViewById(R.id.ethValue) as TextView
    }
    val eth2Value by lazy {
        findViewById(R.id.eth2Value) as TextView
    }
    val recyclerView by lazy {
        findViewById(R.id.recyclerView) as RecyclerView
    }

    var userInfoService = UserInfoService()
    var ltcService = LtcService()
    var tradeService = TradeService()
    lateinit var recyclerAdapter: RecyclerAdapter
    var isGettingUserInfo: Boolean = false
    var userTimer = Timer()
    var order_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        recyclerAdapter = RecyclerAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter
        userInfoButton.onClick {
            if (!isGettingUserInfo) {
                isGettingUserInfo = true
                userInfoButton.text = "获取信息中..."
                userTimer = Timer()
                userTimer.schedule(object : TimerTask() {
                    override fun run() {
                        userInfoService.observeUserUpdate()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ user ->
                                    userCountText.text = ((Integer.valueOf(userCountText.text.toString()) + 1)).toString()
                                    cnyValue.text = user.info.funds.free.cny
                                    ltcValue.text = user.info.funds.free.ltc
                                    btcValue.text = user.info.funds.free.btc
                                    ethValue.text = user.info.funds.free.eth
                                    cny2Value.text = user.info.funds.freezed.cny
                                    ltc2Value.text = user.info.funds.freezed.ltc
                                    btc2Value.text = user.info.funds.freezed.btc
                                    eth2Value.text = user.info.funds.freezed.eth
                                }, {})

                        ltcService.observeLtcList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ ltcSellList ->
                                    recyclerAdapter.dataList = ltcSellList
                                    recyclerAdapter.notifyDataSetChanged()
                                }, {})
                    }
                }, 0, (3 * 1000).toLong())
            } else {
                userTimer.cancel()
                isGettingUserInfo = false
                userInfoButton.text = "获取账户信息"
            }
        }

        ltcSellButton.onClick {
            ltcService.observeLtcSell()
        }

        ltcInfoButton.onClick {
            ltcService.observeLtcList()
        }

        tradeButton.onClick {
            val paramsMap = HashMap<String, String>()
            paramsMap.put("api_key", API_KEY)
            paramsMap.put("amount", "0.1")
            paramsMap.put("symbol", "ltc_cny")
            paramsMap.put("price", "1")
            paramsMap.put("type", "buy")
            tradeService.doTrade(paramsMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ order ->
                        if (order.result) {
                            showToast("下单交易成功！")
                            order_id = order.order_id
                        } else {
                            showToast("下单交易失败！")
                        }
                    }, {})
        }

        tradeInfoButton.onClick {
            val paramsMap = HashMap<String, String>()
            paramsMap.put("api_key", API_KEY)
            paramsMap.put("symbol", "ltc_cny")
            paramsMap.put("order_id", "-1")
            tradeService.observeTradeInfo(paramsMap)
        }

        cancelTradeButton.onClick {
            if (TextUtils.isEmpty(order_id)) {
                showToast("您当前没有可撤销订单！")
                return@onClick
            }
            val paramsMap = HashMap<String, String>()
            paramsMap.put("api_key", API_KEY)
            paramsMap.put("symbol", "ltc_cny")
            paramsMap.put("order_id", order_id)
            tradeService.doCancelTrade(paramsMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ orderCancel ->
                        if (orderCancel.result) {
                            showToast("撤销订单成功!")
                        } else {
                            showToast("撤销订单失败!")
                        }
                    }, {})
        }
    }
}
