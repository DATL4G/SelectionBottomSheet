package com.ahmed3elshaer.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ahmed3elshaer.sample.databinding.ActivityMainBinding
import com.ahmed3elshaer.selectionbottomsheet.selectionBottomSheet

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            renderSelectionSheet()
        }
    }

    private fun renderSelectionSheet() {
        //prepare list of your models
        val selectionList =
            mutableListOf(
                SelectionModel("Selection 1", "1"),
                SelectionModel("Selection 2", "2"),
                SelectionModel("Selection 3", "3"),
                SelectionModel("Selection 4", "4")
            )
        //start using the Builder for the BottomSheet
        //You should pass your type of model to the Builder Class first
        selectionBottomSheet<SelectionModel> {
            list(selectionList)
            dragIndicatorColor(ContextCompat.getColor(this@MainActivity, R.color.colorBlack))
            title("Title")
            titleColor(ContextCompat.getColor(this@MainActivity, R.color.colorBlack))
            itemBinder { it.title }
            confirmText("Apply")
            defaultItemConfirmable(false)
            itemColor(ContextCompat.getColor(this@MainActivity, R.color.colorBlack))
            selectionColor(ContextCompat.getColor(this@MainActivity, R.color.colorGreen))
            selectionDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_check_circle_24))
            defaultItem {
                it.id == "3"
            }
            selectionListener {
                Log.e("Selected", it.toString())
            }
            confirmListener {
                if (it == null) {
                    Log.e("Confirmed", "same as default")
                } else {
                    Log.e("Confirmed ", it.toString())
                }
            }
        }.show(this)
    }

    data class SelectionModel(val title: String, val id: String)


}
