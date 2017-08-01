package dw.zm.com.btbproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import dw.zm.com.btbproject.entry.User;
import dw.zm.com.btbproject.network.NetWorkService;
import dw.zm.com.btbproject.network.UserInfoService;

import static dw.zm.com.btbproject.network.APIUrl.API_KEY;

public class MainActivity extends AppCompatActivity {

    public NetWorkService netWorkService = new NetWorkService();
    public UserInfoService userInfoService = new UserInfoService();
    public Button button1 = null;
    public Button button2 = null;
    public Button button4 = null;

    public Boolean isGettingUserInfo = false;
    public Timer getUserTimer = new Timer();
    public User userInfo = null;
    public Button userInfoButton = null;
    public TextView userCountText = null;
    public TextView cnyValue = null;
    public TextView ltcValue = null;
    public TextView btcValue = null;
    public TextView ethValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button4 = (Button) findViewById(R.id.button4);
        userInfoButton = (Button) findViewById(R.id.userInfoButton);
        userCountText = (TextView) findViewById(R.id.userCountText);
        cnyValue = (TextView) findViewById(R.id.cnyValue);
        ltcValue = (TextView) findViewById(R.id.ltcValue);
        btcValue = (TextView) findViewById(R.id.btcValue);
        ethValue = (TextView) findViewById(R.id.ethValue);

        userInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGettingUserInfo) {
                    isGettingUserInfo = true;
                    userInfoButton.setText("获取信息中...");
                    getUserTimer = new Timer();
                    getUserTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Map paramsMap = new HashMap<String, String>();
                            paramsMap.put("api_key", API_KEY);
                            userInfoService.getUserInfo(paramsMap, new UserInfoService.UserCallBack() {
                                @Override
                                public void onCallBack(final User user) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            userInfo = user;
                                            userCountText.setText(((Integer.valueOf(userCountText.getText().toString()) + 1)) + "");
                                            cnyValue.setText(userInfo.info.funds.free.cny);
                                            ltcValue.setText(userInfo.info.funds.free.ltc);
                                            btcValue.setText(userInfo.info.funds.free.btc);
                                            ethValue.setText(userInfo.info.funds.free.eth);
                                        }
                                    });
                                }
                            });
                        }
                    }, 0, 10 * 1000);
                } else {
                    getUserTimer.cancel();
                    isGettingUserInfo = false;
                    userInfoButton.setText("获取账户信息");
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netWorkService.getLtcSell();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netWorkService.getLtcList();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map paramsMap = new HashMap<String, String>();
                paramsMap.put("api_key", API_KEY);
                paramsMap.put("amount", 0.1 + "");
                paramsMap.put("symbol", "ltc_cny");
                paramsMap.put("price", "290");
                paramsMap.put("type", "buy_market");
                netWorkService.doTrade(paramsMap);
            }
        });
    }
}
