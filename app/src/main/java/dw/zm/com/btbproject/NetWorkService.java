package dw.zm.com.btbproject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dw.zm.com.btbproject.entry.Funds;
import dw.zm.com.btbproject.entry.SellInfo;
import dw.zm.com.btbproject.entry.User;
import dw.zm.com.btbproject.network.APIUrl;
import dw.zm.com.btbproject.utils.Codec;
import dw.zm.com.btbproject.utils.TimerUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static dw.zm.com.btbproject.network.APIUrl.SECRET_KEY;

/**
 * Created by aaron on 2017/7/31.
 */

public class NetWorkService {
    public static final String TAG = NetWorkService.class.getSimpleName();
    public OkHttpClient mOkHttpClient = new OkHttpClient();
    public MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

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

    public void getUserInfo(Map<String, String> paramsMap) {
        final Request request = new Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.UESR_INFO_FORMET).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("result", result);
                User user = new Gson().fromJson(result, User.class);
                Log.i("result", user.info.funds.asset.net + "    " + user.info.funds.asset.total);
            }
        });
    }

    public void doTrade(Map<String, String> paramsMap) {
        Request request = new Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.TRADE_INFO_FORMET).build();
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




    private String parserParamsMap(Map<String, String> paramsMap) {
        StringBuffer sb = new StringBuffer("");
        Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();
        String[] params = new String[paramsMap.size()];
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            params[i] = entry.getKey();
            i++;
        }

        Arrays.sort(params);
        for (int a = 0; a < params.length; a++) {
            sb.append(params[a]).append("=").append(paramsMap.get(params[a]).toString()).append("&");
        }
        sb.append("sign=").append(getSignString(paramsMap));
        Log.i(TAG, "params: " + sb.toString());
        return sb.toString();
    }

    private String getSignString(Map<String, String> paramsMap) {
        StringBuffer sb = new StringBuffer("");
        Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();
        String[] params = new String[paramsMap.size()];
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            params[i] = entry.getKey();
            i++;
        }

        Arrays.sort(params);
        for (int a = 0; a < params.length; a++) {
            sb.append(params[a]).append("=").append(paramsMap.get(params[a])).append("&");
        }
        sb.append("secret_key=").append(SECRET_KEY);
        String md5 = Codec.MD5(sb.toString());
        return md5;
    }
}
