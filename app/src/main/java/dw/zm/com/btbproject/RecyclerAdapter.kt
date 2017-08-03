package dw.zm.com.btbproject

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import dw.zm.com.btbproject.entry.SellInfo
import dw.zm.com.btbproject.utils.TimerUtil

/**
 * Created by aaron on 2017/8/3.
 */
class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataList: List<SellInfo> = ArrayList<SellInfo>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val rootView = LayoutInflater.from(parent!!.context).inflate(R.layout.recycler_item, null, false) as ViewGroup
        rootView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return RecyclerViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as RecyclerViewHolder).setItem(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}

class RecyclerViewHolder(view: ViewGroup): RecyclerView.ViewHolder(view) {
    val timeText by lazy {
        view.findViewById(R.id.timeText) as TextView
    }
    val priceText by lazy {
        view.findViewById(R.id.priceText) as TextView
    }
    val amountText by lazy {
        view.findViewById(R.id.amountText) as TextView
    }
    val typeText by lazy {
        view.findViewById(R.id.typeText) as TextView
    }

    fun setItem(sellInfo: SellInfo) {
        timeText.text = TimerUtil.getDayTime(sellInfo.date).toString()
        priceText.text = sellInfo.price
        amountText.text = sellInfo.amount
        typeText.text = sellInfo.type
    }
}