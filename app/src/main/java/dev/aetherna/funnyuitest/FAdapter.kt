package dev.aetherna.funnyuitest

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



class FAdapter() : RecyclerView.Adapter<FViewHolder>() {

    override fun onBindViewHolder(viewHolder: FViewHolder, position: Int) {

    }

    var items: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FViewHolder {
        return FViewHolder(parent)
    }


    override fun getItemCount() = items.size

}

class FViewHolder(
    val parent: ViewGroup
) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_f))

fun ViewGroup.inflate(@LayoutRes layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}