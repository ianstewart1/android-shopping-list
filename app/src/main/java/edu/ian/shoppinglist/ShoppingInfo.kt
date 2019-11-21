package edu.ian.shoppinglist

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import edu.ian.shoppinglist.MainActivity.Companion.KEY_INFO
import edu.ian.shoppinglist.data.Shopping
import edu.ian.shoppinglist.data.ShoppingType
import kotlinx.android.synthetic.main.shopping_info.view.*

class ShoppingInfo : DialogFragment() {

    private lateinit var ivInfo: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvPrice: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.info))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.shopping_info, null
        )
        ivInfo = rootView.ivIcon
        tvName = rootView.tvInfoName
        tvDesc = rootView.tvInfoDesc
        tvPrice = rootView.tvInfoPrice

        builder.setView(rootView)

        var shopping: Shopping = (arguments?.getSerializable(KEY_INFO) as Shopping)
        if (shopping.type == ShoppingType.FOOD.ordinal) {
            ivInfo.setImageResource(R.drawable.ic_local_dining)
        } else if (shopping.type == ShoppingType.CLOTHING.ordinal) {
            ivInfo.setImageResource(R.drawable.ic_action_tshirt)
        } else {
            ivInfo.setImageResource(R.drawable.ic_action_headphones)
        }
        tvName.setText(shopping.name)
        tvDesc.setText(shopping.description)
        tvPrice.setText(getString(R.string.currency_sign) + shopping.price)

        builder.setPositiveButton(getString(R.string.ok)) {
                _, _ -> // nothing
        }

        return builder.create()
    }
}