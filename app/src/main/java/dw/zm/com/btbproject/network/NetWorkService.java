package dw.zm.com.btbproject.network;

import android.util.Log;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import dw.zm.com.btbproject.utils.Codec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import static dw.zm.com.btbproject.network.APIUrl.SECRET_KEY;

/**
 * Created by aaron on 2017/7/31.
 */
public class NetWorkService {
    public static final String TAG = NetWorkService.class.getSimpleName();
    public OkHttpClient mOkHttpClient = new OkHttpClient();
    public MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public String parserParamsMap(Map<String, String> paramsMap) {
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

    public String getSignString(Map<String, String> paramsMap) {
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
