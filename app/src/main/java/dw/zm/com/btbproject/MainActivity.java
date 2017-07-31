package dw.zm.com.btbproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import static dw.zm.com.btbproject.network.APIUrl.API_KEY;

public class MainActivity extends AppCompatActivity {

    public NetWorkService netWorkService = new NetWorkService();
    public Button button1 = null;
    public Button button2 = null;
    public Button button3 = null;
    public Button button4 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

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

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map paramsMap = new HashMap<String, String>();
                paramsMap.put("api_key", API_KEY);
                netWorkService.getUserInfo(paramsMap);
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
