package dw.zm.com.btbproject.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import dw.zm.com.btbproject.entry.SellInfo
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import java.util.*

/**
 * Created by aaron on 2017/8/2.
 */
class LtcService : NetWorkService() {

    fun observeLtcSell() {
        val request = Request.Builder().url(APIUrl.LTC_SELL_FORMET).build()
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.i("result", response.body().string())
            }
        })
    }

    fun observeLtcList() : Observable<List<SellInfo>> {
        val subject = BehaviorSubject<List<SellInfo>>()
        val request = Request.Builder().url(APIUrl.LTC_INFOS_FORMET).build()
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val type = object : TypeToken<ArrayList<SellInfo>>() {}.type
                val mList = Gson().fromJson<ArrayList<SellInfo>>(response.body().string(), type)
                Log.i(NetWorkService.TAG, "交易记录个数： " + mList.size)

                val resultList = mList.subList(mList.size - 100, mList.size - 1)
                val tempList = ArrayList<SellInfo>()
                for (i in resultList.indices) {
                    if (tempList.size > 0) {
                        if (resultList[i].price != tempList[tempList.size - 1].price) {
                            tempList.add(resultList[i])
                        }
                    } else {
                        tempList.add(resultList[i])
                    }
                }

                Collections.reverse(tempList)
                subject.onNext(tempList)
                subject.onCompleted()

                /*Log.i(NetWorkService.TAG, "交易记录个数： " + tempList.size)
                for (i in tempList.indices) {
                    Log.i(NetWorkService.TAG, "交易时间：" + TimerUtil.getTime(tempList[i].date) +
                            "   交易价格：" + tempList[i].price +
                            "   交易数量：" + tempList[i].amount +
                            "   交易类型：" + tempList[i].type)
                }*/
            }
        })

        return subject
    }
}
