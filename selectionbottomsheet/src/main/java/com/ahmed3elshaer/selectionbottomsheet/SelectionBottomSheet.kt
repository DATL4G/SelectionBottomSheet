package com.ahmed3elshaer.selectionbottomsheet

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ahmed3elshaer.selectionbottomsheet.databinding.SelectionBottomSheetBinding
import com.ahmed3elshaer.selectionbottomsheet.databinding.SingleChoiceItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectionBottomSheet<T> internal constructor(
    private val itemList: List<T>,
    private val title: String,
    private val selectionItemBinder: (item: T) -> String,
    private val selectionCallback: (item: T) -> Unit,
    private val confirmCallback: (item: T) -> Unit,
    private val confirmText: String?,
    private val dragIndicatorColor: Int?,
    private val titleColor: Int,
    private val titleGravity: Int,
    private val itemColor: Int,
    private val selectionColor: Int,
    private val selectionDrawable: Drawable?,
    private val confirmTextColor: Int,
    private val confirmBackgroundColor: Int,
    private val confirmDisabledTextColor: Int,
    private val confirmDisabledBackgroundColor: Int
) : BottomSheetDialogFragment() {

    private val binding: SelectionBottomSheetBinding by viewBinding()
    private var currentSelection: T? = null
    private var confirmButtonListener = {
        binding.bConfirm.isEnabled = currentSelection != null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.selection_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(): Unit = with(binding) {
        rvSingleChoice.layoutManager = LinearLayoutManager(context)
        rvSingleChoice.itemAnimator = DefaultItemAnimator()
        rvSingleChoice.addItemDecoration(
            DefaultDividerItemDecoration(
                requireContext(),
                DefaultDividerItemDecoration.VERTICAL_LIST,
                0
            )
        )
        val adapter = object : SelectionAdapter() {
            override fun onBindData(item: T, itemBinding: SingleChoiceItemBinding) {
                itemBinding.tvName.text = selectionItemBinder(item)
                itemBinding.tvName.setTextColor(itemColor)
                itemBinding.ivSelected.setImageDrawable(selectionDrawable)
                itemBinding.ivSelected.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    itemColor,
                    BlendModeCompat.SRC_IN
                )
            }
        }
        dragIndicatorColor?.let {
            draggedIndicator.setCardBackgroundColor(it)
            draggedIndicator.show()
        }

        tvTitle.text = title
        tvTitle.setTextColor(titleColor)
        tvTitle.gravity = titleGravity

        rvSingleChoice.adapter = adapter

        confirmText?.let { bConfirm.text = it }
        bConfirm.isEnabled = currentSelection != null
        bConfirm.setTextColor(colorStateListOf(
            intArrayOf(android.R.attr.state_enabled) to confirmTextColor,
            intArrayOf(-android.R.attr.state_enabled) to confirmDisabledTextColor
        ))
        bConfirm.background.setTintList(colorStateListOf(
            intArrayOf(android.R.attr.state_enabled) to confirmBackgroundColor,
            intArrayOf(-android.R.attr.state_enabled) to confirmDisabledBackgroundColor
        ))

        bConfirm.setOnClickListener {
            dismiss()
            confirmCallback.invoke(currentSelection!!)
        }
    }

    abstract inner class SelectionAdapter : RecyclerView.Adapter<SelectionAdapter.VisaTypesViewHolder>() {
        private var lastSelectionBinding: SingleChoiceItemBinding? = null

        inner class VisaTypesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            private val binding: SingleChoiceItemBinding by viewBinding()

            fun bindData(item: T) {
                onBindData(item, binding)
            }

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                val newSelection = itemList[absoluteAdapterPosition]

                if (newSelection != currentSelection) {
                    lastSelectionBinding?.let {
                        it.tvName.setTextColor(itemColor)
                        it.ivSelected.hide()
                    }

                    currentSelection = itemList[absoluteAdapterPosition]
                    selectionCallback(itemList[absoluteAdapterPosition])
                    confirmButtonListener.invoke()

                    binding.tvName.setTextColor(selectionColor)
                    binding.ivSelected.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        selectionColor,
                        BlendModeCompat.SRC_IN
                    )
                    binding.ivSelected.show()
                    lastSelectionBinding = binding
                }
            }
        }

        abstract fun onBindData(item: T, itemBinding: SingleChoiceItemBinding)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisaTypesViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_choice_item, parent, false)

            return VisaTypesViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: VisaTypesViewHolder, position: Int) {
            holder.bindData(itemList[position])
        }

        override fun getItemCount(): Int {
            return itemList.size
        }
    }
}