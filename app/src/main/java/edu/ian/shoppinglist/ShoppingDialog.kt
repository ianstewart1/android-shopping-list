package edu.ian.shoppinglist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import edu.ian.shoppinglist.MainActivity.Companion.KEY_SHOPPING
import edu.ian.shoppinglist.data.Shopping
import edu.ian.shoppinglist.data.ShoppingType
import kotlinx.android.synthetic.main.new_shopping_dialog.view.*

class ShoppingDialog : DialogFragment(), AdapterView.OnItemSelectedListener {

    interface ShoppingHandler {
        fun shoppingCreated(item: Shopping)
        fun shoppingUpdated(item: Shopping)
    }

    private lateinit var shoppingHandler: ShoppingHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ShoppingHandler) {
            shoppingHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.exception_warning))
        }
    }

    private lateinit var spinnerCategory: Spinner
    private lateinit var etShoppingName: EditText
    private lateinit var etShoppingDesc: EditText
    private lateinit var etShoppingPrice: EditText

    var isEditMode = false


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_item))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_shopping_dialog, null
        )
        spinnerCategory = rootView.spinner
        etShoppingName = rootView.etShopping
        etShoppingDesc = rootView.etDescription
        etShoppingPrice = rootView.etPrice

        var typeAdapter = ArrayAdapter((context as MainActivity), android.R.layout.simple_spinner_item, ShoppingType.values())
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = typeAdapter
        builder.setView(rootView)


        isEditMode = ((arguments != null) && arguments!!.containsKey(KEY_SHOPPING))

        if (isEditMode) {
            builder.setTitle(getString(R.string.edit_item))
            var shopping: Shopping = (arguments?.getSerializable(KEY_SHOPPING) as Shopping)
            spinnerCategory.setSelection(shopping.type)
            etShoppingName.setText(shopping.name)
            etShoppingDesc.setText(shopping.description)
            etShoppingPrice.setText(shopping.price)
        }

        builder.setPositiveButton(R.string.ok) {
                _, _ -> // nothing
        }

        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etShoppingName.text.isNotEmpty() && etShoppingDesc.text.isNotEmpty()) {
                if (isEditMode) {
                    handleShoppingEdit()
                } else {
                    handleShoppingCreate()
                }
                (dialog as AlertDialog).dismiss()
            } else {
                etShoppingName.error = getString(R.string.edit_text_error)
                etShoppingDesc.error = getString(R.string.edit_text_error)
            }
        }
    }

    private fun handleShoppingCreate() {
        shoppingHandler.shoppingCreated(
            Shopping(
                null,
                spinnerCategory.selectedItemPosition,
                etShoppingName.text.toString(),
                etShoppingDesc.text.toString(),
                etShoppingPrice.text.toString(),
                false
            )
        )
    }

    private fun handleShoppingEdit() {
        val shoppingToEdit = arguments?.getSerializable(
            KEY_SHOPPING
        ) as Shopping
        shoppingToEdit.type = spinnerCategory.selectedItemPosition
        shoppingToEdit.name = etShoppingName.text.toString()
        shoppingToEdit.description = etShoppingDesc.text.toString()
        shoppingToEdit.price = etShoppingPrice.text.toString()

        shoppingHandler.shoppingUpdated(shoppingToEdit)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }
}