package dw.zm.com.btbproject.network;

/**
 * Created by aaron on 2017/7/4.
 */

public class APIUrl {

    public static final String API_KEY = "6c55f3b3-41a4-47f4-a0e6-e09317964416";
    public static final String SECRET_KEY = "BE4A7468D74A76D9E493541D8B532FBF";

    /**
     * LTC的最新价格
     */
    public static final String LTC_SELL_FORMET = "https://www.okcoin.cn/api/v1/ticker.do?symbol=ltc_cny";
    /**
     * LTC交易信息
     */
    public static final String LTC_INFOS_FORMET = "https://www.okcoin.cn/api/v1/trades.do?since=5000&symbol=ltc_cny";

    /**
     * 获取个人信息
     */
    public static final String UESR_INFO_FORMET = "https://www.okcoin.cn/api/v1/userinfo.do";
    /**
     * 下单交易
     */
    public static final String DO_TRADE_FORMET = "https://www.okcoin.cn/api/v1/trade.do";
    /**
     * 获取用户的交易信息（未成交）
     */
    public static final String TRADE_INFO_FORMET = "https://www.okcoin.cn/api/v1/order_info.do";
    /**
     * 撤销订单
     */
    public static final String TRADE_CANCEL_FORMET = "https://www.okcoin.cn/api/v1/cancel_order.do";
}
