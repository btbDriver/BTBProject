package dw.zm.com.btbproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import dw.zm.com.btbproject.entry.Ticker;
import dw.zm.com.btbproject.network.HttpRequest;
import dw.zm.com.btbproject.network.YBUrl;
import dw.zm.com.btbproject.utils.TimerUtil;

public class MainActivity extends AppCompatActivity {

    /**
     * 交易价格列表
     */
    List<Ticker> ticketList = new ArrayList<Ticker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doWork();
    }

    public void doWork() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String result = HttpRequest.sendGet(YBUrl.MARKET_FORMET, "market=btccny");
                if (!TextUtils.isEmpty(result)) {
                    Ticker ticker = new Gson().fromJson(result, Ticker.class);
                    Log.i("EOSCNY: ", "No: " + " 时间: " + TimerUtil.getTime(ticker.date) + "  交易价格： " + ticker.last
                            + "  卖出价：" + ticker.sell + "  买入价：" + ticker.buy);
                    doOptimize(ticker);
                }
            }
        }, 1000, 1000);
    }

    private void doOptimize(Ticker ticker) {
        if (ticketList.size() >= 100) {
            ticketList.remove(0);
        }
        ticketList.add(ticker);

        if (ticketList.size() >= 100) {
            int addCount = 0;
            int delCount = 0;
            for (int i = 0; i < ticketList.size() - 1; i++) {
                if (Double.valueOf(ticketList.get(i + 1).last) > Double.valueOf(ticketList.get(i).last)) {
                    addCount ++;
                } else if (Double.valueOf(ticketList.get(i + 1).last) < Double.valueOf(ticketList.get(i).last)) {
                    delCount ++;
                }
            }

            Log.i("EOSCNY: ", " addCount: " + addCount + "  delCount: " + delCount);
        }
    }
}
