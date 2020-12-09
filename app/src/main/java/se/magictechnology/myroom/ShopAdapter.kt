package se.magictechnology.myroom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShopAdapter(ctx : Context) : RecyclerView.Adapter<ShopViewHolder>() {

    var shopdb = ShopDB(ctx)

    var shoppingitems : List<ShopDB.ShoppingItem>? = null

    fun loadShopping()
    {
        GlobalScope.launch(Dispatchers.IO) {
            shoppingitems = shopdb.shopdb.ShoppingDao().loadAll()

            GlobalScope.launch(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {

        val vh = ShopViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.shopitem, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        shoppingitems?.let {
            return it.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {

        var currentItem = shoppingitems!![position]

        var amountText = "VET EJ"
        currentItem.shopAmount?.let {
            amountText = it.toString()
        }

        holder.shoptext.text = currentItem.uid.toString()+" "+currentItem.shopName+" "+amountText

        holder.itemView.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                shopdb.shopdb.ShoppingDao().delete(currentItem)
                loadShopping()
            }

        }

    }

}

class ShopViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    var shoptext = view.findViewById<TextView>(R.id.shopitemTV)

}