package dw.zm.com.btbproject.network;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import dw.zm.com.btbproject.entry.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

/**
 * Created by aaron on 2017/8/1.
 */

public class UserInfoService extends NetWorkService {

    public void getUserInfo(Map<String, String> paramsMap, final UserCallBack userCallBack) {
        final Request request = new Request.Builder().post(RequestBody.create(mediaType, parserParamsMap(paramsMap))).url(APIUrl.UESR_INFO_FORMET).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "获取账户信息成功，INFO：" + result);
                User user = new Gson().fromJson(result, User.class);
                userCallBack.onCallBack(user);
            }
        });
    }


    public interface UserCallBack {
        public void onCallBack(User user);
    }
}
