package edu.ian.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import edu.ian.shoppinglist.adapter.ShoppingAdapter
import edu.ian.shoppinglist.data.AppDatabase
import edu.ian.shoppinglist.data.Shopping
import edu.ian.shoppinglist.data.ShoppingType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ShoppingDialog.ShoppingHandler {

    companion object {
        // Got a context error when I tried to extract these strings
        val KEY_SHOPPING = "KEY_SHOPPING"
        val TAG_SHOPPING_DIALOG = "TAG_SHOPPING_DIALOG"
        val TAG_SHOPPING_EDIT = "TAG_SHOPPING_DIALOG"
        val KEY_INFO = "KEY_INFO"
    }

    lateinit var shoppingAdapter: ShoppingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        initRecyclerView()

        btnAdd.setOnClickListener {
            showAddShoppingDialog()
        }

        btnDeleteAll.setOnClickListener {
            shoppingAdapter.deleteAllShopping()
        }

    }

    private fun initRecyclerView() {
        Thread {
            var shoppingList =
                AppDatabase.getInstance(this@MainActivity).shoppingDao().getAllShopping()

            runOnUiThread {
                shoppingAdapter = ShoppingAdapter(this, shoppingList)
                recyclerShopping.adapter = shoppingAdapter

                var itemDecoration = DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )

                recyclerShopping.addItemDecoration(itemDecoration)
            }
        }.start()
    }

    fun showAddShoppingDialog() {
        ShoppingDialog().show(supportFragmentManager, TAG_SHOPPING_DIALOG)
    }

    var editIndex: Int = -1

    fun showEditShoppingDialog(shoppingToEdit: Shopping, idx: Int) {
        editIndex = idx
        val editDialog = ShoppingDialog()
        var bundle = Bundle()
        bundle.putSerializable(KEY_SHOPPING, shoppingToEdit)
        editDialog.arguments = bundle
        editDialog.show(supportFragmentManager, TAG_SHOPPING_EDIT)
    }

    fun showInfoDialog(shopping: Shopping, idx: Int) {
        var bundle = Bundle()
        bundle.putSerializable(KEY_INFO, shopping)
        val infoDialog = ShoppingInfo()
        infoDialog.arguments = bundle
        infoDialog.show(supportFragmentManager, TAG_SHOPPING_DIALOG)
    }

    private fun saveShopping(shopping: Shopping) {
        Thread {
            var newId = AppDatabase.getInstance(this).shoppingDao().addShopping(shopping)

            shopping.shoppingId = newId

            runOnUiThread {
                shoppingAdapter.addShopping(shopping)
            }
        }.start()
    }

    override fun shoppingCreated(item: Shopping) {
        saveShopping(item)
    }

    override fun shoppingUpdated(item: Shopping) {
        Thread {
            AppDatabase.getInstance(this@MainActivity).shoppingDao().updateShopping(item)
            runOnUiThread {
                shoppingAdapter.updateShoppingOnPosition(item, editIndex)
            }
        }.start()
    }
}

