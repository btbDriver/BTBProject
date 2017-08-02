package dw.zm.com.btbproject.network;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import dw.zm.com.btbproject.entry.SellInfo;
import dw.zm.com.btbproject.utils.TimerUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by aaron on 2017/8/2.
 */
public class LtcService extends NetWorkService {

    public Activity context = null;
    public LtcService(Activity mContext) {
        this.context = mContext;
    }

    public void getLtcSell() {
        Request request = new Request.Builder().url(APIUrl.LTC_SELL_FORMET).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("result", response.body().string());
            }
        });
    }

    public void getLtcList() {
        final Request request = new Request.Builder().url(APIUrl.LTC_INFOS_FORMET).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Type type = new TypeToken<ArrayList<SellInfo>>() {}.getType();
                ArrayList<SellInfo> mList = new Gson().fromJson(response.body().string(), type);
                Log.i(TAG, "交易记录个数： " + mList.size());

                List<SellInfo> resultList = mList.subList(mList.size() - 100, mList.size() - 1);
                List<SellInfo> tempList = new ArrayList<SellInfo>();
                for (int i = 0; i < resultList.size(); i ++) {
                    if (tempList.size() > 0) {
                        if (!resultList.get(i).price.equals(tempList.get(tempList.size() - 1).price)) {
                            tempList.add(resultList.get(i));
                        }
                    } else {
                        tempList.add(resultList.get(i));
                    }
                }

                Log.i(TAG, "交易记录个数： " + tempList.size());
                for (int i = 0; i < tempList.size(); i++) {
                    Log.i(TAG, "交易时间：" + TimerUtil.getTime(tempList.get(i).date) +
                            "   交易价格：" + tempList.get(i).price +
                            "   交易数量：" + tempList.get(i).amount +
                            "   交易类型：" + tempList.get(i).type);
                }
            }
        });
    }
}
