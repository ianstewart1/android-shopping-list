package edu.ian.shoppinglist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.ian.shoppinglist.MainActivity
import edu.ian.shoppinglist.R
import edu.ian.shoppinglist.data.AppDatabase
import edu.ian.shoppinglist.data.Shopping
import edu.ian.shoppinglist.data.ShoppingType
import kotlinx.android.synthetic.main.shopping_row.view.*

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    var shoppingList = mutableListOf<Shopping>()

    val context: Context

    constructor(context: Context, listShopping: List<Shopping>) {
        this.context = context

        shoppingList.addAll(listShopping)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val shoppingRow = LayoutInflater.from(context).inflate(
            R.layout.shopping_row, parent, false
        )

        return ViewHolder(shoppingRow)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var shopping = shoppingList.get(holder.adapterPosition)

        if (shopping.type == ShoppingType.FOOD.ordinal) {
            holder.ivShopping.setImageResource(R.drawable.ic_local_dining)
        } else if (shopping.type == ShoppingType.CLOTHING.ordinal) {
            holder.ivShopping.setImageResource(R.drawable.ic_action_tshirt)
        } else {
            holder.ivShopping.setImageResource(R.drawable.ic_action_headphones)
        }

        holder.cbShopping.text = context.getString(R.string.cbText)
        holder.cbShopping.isChecked = shopping.isBought
        holder.tvName.text = shopping.name

        holder.btnDelete.setOnClickListener {
            deleteShopping(holder.adapterPosition)
        }

        holder.cbShopping.setOnClickListener {
            shopping.isBought = holder.cbShopping.isChecked
            updateShopping(shopping)
        }

        holder.btnEdit.setOnClickListener {
            (context as MainActivity).showEditShoppingDialog(
                shopping, holder.adapterPosition
            )
        }

        holder.ibtnInfo.setOnClickListener {
            (context as MainActivity).showInfoDialog(
                shopping, holder.adapterPosition
            )
        }
    }

    fun updateShopping(shopping: Shopping) {
        Thread {
            AppDatabase.getInstance(context).shoppingDao().updateShopping(shopping)
        }.start()
    }

    fun updateShoppingOnPosition(shopping: Shopping, index: Int) {
        shoppingList[index] = shopping
        notifyItemChanged(index)
    }

    fun deleteShopping(index: Int) {
        Thread{
            AppDatabase.getInstance(context).shoppingDao().deleteShopping(shoppingList[index])

            (context as MainActivity).runOnUiThread {
                shoppingList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAllShopping() {
        Thread {
            AppDatabase.getInstance(context).shoppingDao().deleteAllShopping()

            (context as MainActivity).runOnUiThread {
                shoppingList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addShopping(shopping: Shopping) {
        shoppingList.add(shopping)
        notifyItemInserted(shoppingList.lastIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivShopping = itemView.ivShopping
        val cbShopping = itemView.cbShopping
        val tvName = itemView.tvName
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val ibtnInfo = itemView.ibtnInfo
    }

}