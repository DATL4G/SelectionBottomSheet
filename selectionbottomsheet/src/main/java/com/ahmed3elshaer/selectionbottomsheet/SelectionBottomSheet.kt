package com.ahmed3elshaer.selectionbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmed3elshaer.selectionbottomsheet.databinding.SelectionBottomSheetBinding
import com.ahmed3elshaer.selectionbottomsheet.databinding.SingleChoiceItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectionBottomSheet<T>() : BottomSheetDialogFragment() {

    private var _binding: SelectionBottomSheetBinding? = null
    private val binding: SelectionBottomSheetBinding
        get() = _binding!!
    private lateinit var data: SelectData<T>
    private lateinit var adapter: SelectAdapter
    private var currentSelection: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<SelectData<T>>(SELECT_DATA_KEY)?.also {
            data = it
            adapter = SelectAdapter(data.itemList)
        } ?: run {
            adapter = SelectAdapter(emptyList())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        when (data.expandState) {
            ExpandState.Expanded -> dialog?.setOnShowListener { it.expand() }
            ExpandState.ExpandOnTv -> if ((context ?: activity ?: requireContext()).packageManager.isTelevision()) {
                dialog?.setOnShowListener { it.expand() }
            }
            is ExpandState.ExpandCustom -> if ((data.expandState as? ExpandState.ExpandCustom)?.predicate?.invoke() == true) {
                dialog?.setOnShowListener { it.expand() }
            }
            else -> { }
        }
        _binding = SelectionBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        title.text = data.title
        title.setTextColor(data.titleColor)
        title.gravity = data.titleGravity

        recycler.itemAnimator = null
        recycler.addItemDecoration(DefaultDividerItemDecoration(
            context ?: activity ?: requireContext(),
            DefaultDividerItemDecoration.VERTICAL_LIST,
            0
        ))
        recycler.adapter = adapter

        confirm.text = data.confirmText
        confirm.setTextColor(colorStateListOf(
            intArrayOf(android.R.attr.state_enabled) to data.confirmTextColor,
            intArrayOf(-android.R.attr.state_enabled) to data.confirmDisabledTextColor
        ))
        confirm.background.setTintList(colorStateListOf(
            intArrayOf(android.R.attr.state_enabled) to data.confirmBackgroundColor,
            intArrayOf(-android.R.attr.state_enabled) to data.confirmDisabledBackgroundColor
        ))

        confirm.setOnClickListener {
            dismiss()
            data.confirmCallback.invoke(if (isDefault(currentSelection) && !data.defaultItemConfirmable) null else currentSelection)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkConfirmable(): Unit = with(binding) {
        confirm.isEnabled = currentSelection != null
    }

    fun isDefault(item: T?): Boolean {
        return if (data.defaultItemBinder != null && item != null) {
            data.itemList.find(data.defaultItemBinder!!) == item
        } else {
            false
        }
    }

    fun show(fragmentManager: FragmentManager) = apply {
        this.show(fragmentManager, this.tag ?: TAG)
    }

    fun show(tag: String, fragmentManager: FragmentManager) = apply {
        this.show(fragmentManager, tag)
    }

    fun show(activity: FragmentActivity) = show(activity.supportFragmentManager)
    fun show(tag: String, activity: FragmentActivity) = show(tag, activity.supportFragmentManager)

    fun show(fragment: Fragment) = show(fragment.childFragmentManager)
    fun show(tag: String, fragment: Fragment) = show(tag, fragment.childFragmentManager)

    inner class SelectAdapter(private val list: List<T>) : RecyclerView.Adapter<SelectAdapter.ViewHolder>() {
        private var lastSelectionBinding: SingleChoiceItemBinding? = null

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            var _binding: SingleChoiceItemBinding? = null
            val binding: SingleChoiceItemBinding
                get() = _binding!!

            init {
                _binding = SingleChoiceItemBinding.bind(itemView)
                binding.card.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                val newSelection = list[absoluteAdapterPosition]

                if (newSelection != currentSelection) {
                    lastSelectionBinding?.let {
                        it.name.setTextColor(data.itemColor)
                        it.icon.clearColorFilter()
                        ImageViewCompat.setImageTintList(it.icon, null)
                        it.icon.hide()
                    }

                    currentSelection = list[absoluteAdapterPosition]
                    data.selectionCallback.invoke(list[absoluteAdapterPosition])
                    checkConfirmable()

                    binding.name.setTextColor(data.selectionColor)
                    binding.icon.clearTint()
                    binding.icon.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        data.selectionColor,
                        BlendModeCompat.SRC_IN
                    )

                    binding.icon.show()
                    lastSelectionBinding = binding
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_choice_item, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int): Unit = with(holder) {
            val item = list[position]

            binding.name.text = data.itemBinder(item)
            binding.name.setTextColor(data.itemColor)
            binding.icon.setImageDrawable(data.selectionDrawable)
            binding.icon.clearTint()

            if (isDefault(item)) {
                lastSelectionBinding = binding
                onClick(binding.card)
            }
        }
    }

    companion object {
        private const val SELECT_DATA_KEY = "SelectDataKey"
        const val TAG = "SelectionDialog"

        @Suppress("UNCHECKED_CAST")
        internal fun <T> newInstance(
            data: SelectData<T>
        ) = SelectionBottomSheet<T>().apply {
            arguments = Bundle().also {
                it.putParcelable(SELECT_DATA_KEY, data)
            }
        }
    }
}