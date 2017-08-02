package dw.zm.com.btbproject.network;

import android.app.Activity;
import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import dw.zm.com.btbproject.entry.Order;
import dw.zm.com.btbproject.entry.OrderCancel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by aaron on 2017/8/2.
 */
public class TradeService extends NetWorkService {

    public Activity context = null;
    public TradeService(Activity mContext) {
        this.context = mContext;
    }

    public void doTrade(Map<String, String> paramsMap, final TradeCallBack tradeCallBack) {
        Request request = new Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.DO_TRADE_FORMET).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "doTrade.result: " + result);
                final Order order = new Gson().fromJson(result, Order.class);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tradeCallBack.tradeCallBack(order);
                    }
                });
            }
        });
    }

    public void tradeInfo(Map<String, String> paramsMap) {
        Request request = new Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.TRADE_INFO_FORMET).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "tradeInfo.result: " + result);
            }
        });
    }

    public void doCancelTrade(Map<String, String> paramsMap, final TradeCancelCallBack tradeCancelCallBack) {
        Request request = new Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.TRADE_CANCEL_FORMET).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "doCancelTrade.result: " + result);
                final OrderCancel orderCancel = new Gson().fromJson(result, OrderCancel.class);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tradeCancelCallBack.tradeCancelCallBack(orderCancel);
                    }
                });
            }
        });
    }


    public interface TradeCallBack {
        public void tradeCallBack(Order order);
    }

    public interface TradeCancelCallBack {
        public void tradeCancelCallBack(OrderCancel orderCancel);
    }
}
