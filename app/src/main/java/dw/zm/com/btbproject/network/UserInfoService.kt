package dw.zm.com.btbproject.network

import android.util.Log
import com.google.gson.Gson
import java.io.IOException
import dw.zm.com.btbproject.entry.User
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import java.util.HashMap

/**
 * Created by aaron on 2017/8/1.
 */
class UserInfoService : NetWorkService() {

    fun observeUserUpdate() : Observable<User> {
        val subject = BehaviorSubject<User>()
        val paramsMap = HashMap<String, String>()
        paramsMap.put("api_key", APIUrl.API_KEY)
        val request = Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.UESR_INFO_FORMET).build()
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body().string()
                Log.i(NetWorkService.TAG, "获取账户信息成功，INFO：" + result)
                val user = Gson().fromJson(result, User::class.java)
                subject.onNext(user)
                subject.onCompleted()
            }
        })

        return subject
    }
}
