package dw.zm.com.btbproject;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by aaron on 2017/8/2.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * 显示Toast弹窗
     * @param show
     */
    public void showToast(String show) {
        Toast.makeText(this, show, Toast.LENGTH_SHORT).show();
    }
}
