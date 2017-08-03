package dw.zm.com.btbproject.network

import android.util.Log
import com.google.gson.Gson
import java.io.IOException
import dw.zm.com.btbproject.entry.Order
import dw.zm.com.btbproject.entry.OrderCancel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import rx.Observable
import rx.lang.kotlin.BehaviorSubject

/**
 * Created by aaron on 2017/8/2.
 */
class TradeService : NetWorkService() {

    fun doTrade(paramsMap: Map<String, String>): Observable<Order> {
        val subject = BehaviorSubject<Order>()
        val request = Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.DO_TRADE_FORMET).build()
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body().string()
                Log.i(NetWorkService.TAG, "doTrade.result: " + result)
                val order = Gson().fromJson(result, Order::class.java)
                subject.onNext(order)
                subject.onCompleted()
            }
        })

        return subject
    }

    fun observeTradeInfo(paramsMap: Map<String, String>) {
        val request = Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.TRADE_INFO_FORMET).build()
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body().string()
                Log.i(NetWorkService.TAG, "tradeInfo.result: " + result)
            }
        })
    }

    fun doCancelTrade(paramsMap: Map<String, String>): Observable<OrderCancel> {
        val subject = BehaviorSubject<OrderCancel>()
        val request = Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.TRADE_CANCEL_FORMET).build()
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body().string()
                Log.i(NetWorkService.TAG, "doCancelTrade.result: " + result)
                val orderCancel = Gson().fromJson(result, OrderCancel::class.java)
                subject.onNext(orderCancel)
                subject.onCompleted()
            }
        })

        return subject
    }
}
