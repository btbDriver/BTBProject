package dw.zm.com.btbproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import dw.zm.com.btbproject.entry.Order;
import dw.zm.com.btbproject.entry.OrderCancel;
import dw.zm.com.btbproject.entry.User;
import dw.zm.com.btbproject.network.LtcService;
import dw.zm.com.btbproject.network.TradeService;
import dw.zm.com.btbproject.network.UserInfoService;
import static dw.zm.com.btbproject.network.APIUrl.API_KEY;

public class MainActivity extends BaseActivity {

    public UserInfoService userInfoService = new UserInfoService(this);
    public LtcService ltcService = new LtcService(this);
    public TradeService tradeService = new TradeService(this);

    public Button ltcSellButton = null;
    public Button ltcInfoButton = null;
    public Button tradeButton = null;
    public Button tradeInfoButton = null;
    public Button cancelTradeButton = null;

    public Boolean isGettingUserInfo = false;
    public Timer userTimer = new Timer();
    public User userInfo = null;
    public Button userInfoButton = null;
    public TextView userCountText = null;
    public TextView cnyValue = null;
    public TextView cny2Value = null;
    public TextView ltcValue = null;
    public TextView ltc2Value = null;
    public TextView btcValue = null;
    public TextView btc2Value = null;
    public TextView ethValue = null;
    public TextView eth2Value = null;
    public String order_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ltcSellButton = (Button) findViewById(R.id.ltcSellButton);
        ltcInfoButton = (Button) findViewById(R.id.ltcInfoButton);
        tradeButton = (Button) findViewById(R.id.tradeButton);
        cancelTradeButton = (Button) findViewById(R.id.cancelTradeButton);
        tradeInfoButton = (Button) findViewById(R.id.tradeInfoButton);
        userInfoButton = (Button) findViewById(R.id.userInfoButton);
        userCountText = (TextView) findViewById(R.id.userCountText);
        cnyValue = (TextView) findViewById(R.id.cnyValue);
        cny2Value = (TextView) findViewById(R.id.cny2Value);
        ltcValue = (TextView) findViewById(R.id.ltcValue);
        ltc2Value = (TextView) findViewById(R.id.cny2Value);
        btcValue = (TextView) findViewById(R.id.btcValue);
        btc2Value = (TextView) findViewById(R.id.btc2Value);
        ethValue = (TextView) findViewById(R.id.ethValue);
        eth2Value = (TextView) findViewById(R.id.eth2Value);

        userInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGettingUserInfo) {
                    isGettingUserInfo = true;
                    userInfoButton.setText("获取信息中...");
                    userTimer = new Timer();
                    userTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Map paramsMap = new HashMap<String, String>();
                            paramsMap.put("api_key", API_KEY);
                            userInfoService.getUserInfo(paramsMap, new UserInfoService.UserCallBack() {
                                @Override
                                public void onCallBack(final User user) {
                                    userInfo = user;
                                    userCountText.setText(((Integer.valueOf(userCountText.getText().toString()) + 1)) + "");
                                    cnyValue.setText(userInfo.getInfo().getFunds().getFree().getCny());
                                    ltcValue.setText(userInfo.getInfo().getFunds().getFree().getLtc());
                                    btcValue.setText(userInfo.getInfo().getFunds().getFree().getBtc());
                                    ethValue.setText(userInfo.getInfo().getFunds().getFree().getEth());
                                    cny2Value.setText(userInfo.getInfo().getFunds().getFreezed().getCny());
                                    ltc2Value.setText(userInfo.getInfo().getFunds().getFreezed().getLtc());
                                    btc2Value.setText(userInfo.getInfo().getFunds().getFreezed().getBtc());
                                    eth2Value.setText(userInfo.getInfo().getFunds().getFreezed().getEth());
                                }
                            });
                        }
                    }, 0, 10 * 1000);
                } else {
                    userTimer.cancel();
                    isGettingUserInfo = false;
                    userInfoButton.setText("获取账户信息");
                }
            }
        });

        ltcSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ltcService.getLtcSell();
            }
        });

        ltcInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ltcService.getLtcList();
            }
        });

        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map paramsMap = new HashMap<String, String>();
                paramsMap.put("api_key", API_KEY);
                paramsMap.put("amount", "0.1");
                paramsMap.put("symbol", "ltc_cny");
                paramsMap.put("price", "1");
                paramsMap.put("type", "buy");
                tradeService.doTrade(paramsMap, new TradeService.TradeCallBack() {
                    @Override
                    public void tradeCallBack(Order order) {
                        if (order.getResult()) {
                            showToast("下单交易成功！");
                            order_id = order.getOrder_id();
                        } else {
                            showToast("下单交易失败！");
                        }
                    }
                });
            }
        });

        tradeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map paramsMap = new HashMap<String, String>();
                paramsMap.put("api_key", API_KEY);
                paramsMap.put("symbol", "ltc_cny");
                paramsMap.put("order_id", "-1");
                tradeService.tradeInfo(paramsMap);
            }
        });

        cancelTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(order_id)) {
                    showToast("您当前没有可撤销订单！");
                    return;
                }
                Map paramsMap = new HashMap<String, String>();
                paramsMap.put("api_key", API_KEY);
                paramsMap.put("symbol", "ltc_cny");
                paramsMap.put("order_id", order_id);
                tradeService.doCancelTrade(paramsMap, new TradeService.TradeCancelCallBack() {
                    @Override
                    public void tradeCancelCallBack(OrderCancel orderCancel) {
                        if (orderCancel.getResult()) {
                            showToast("撤销订单成功!");
                        } else {
                            showToast("撤销订单失败!");
                        }
                    }
                });
            }
        });
    }
}
