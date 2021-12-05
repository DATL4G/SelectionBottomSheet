package com.ahmed3elshaer.sample

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ahmed3elshaer.sample.databinding.ActivityMainBinding
import com.ahmed3elshaer.selectionbottomsheet.SelectionBuilder
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
            dragIndicatorColor(ContextCompat.getColor(this@MainActivity, R.color.colorWhite))
            title("Title")
            itemBinder { it.title }
            confirmText("Apply")
            itemColor(ContextCompat.getColor(this@MainActivity, R.color.colorWhite))
            selectionColor(ContextCompat.getColor(this@MainActivity, R.color.colorGreen))
            selectionDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_check_circle_24))
            selectionListener {
                Log.e("Selected: ", it.toString())
            }
            confirmListener {
                Log.e("Confirmed: ", it.toString())
            }
        }
    }

    data class SelectionModel(val title: String, val id: String)


}
