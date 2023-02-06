package com.giganti.gabriele.studiostudy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercent: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.editTextNumber)
        seekBarTip = findViewById(R.id.seekBar)
        tvTipPercent = findViewById(R.id.tvTipPerc)
        tvTipAmount = findViewById(R.id.textView5)
        tvTotalAmount = findViewById(R.id.textView6)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)


        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercent.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "aftertextChanged")
                computeTipAndTotal()
            }

        })


    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..19 -> "Acceptable"
            in 20..24 -> "Good"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        //interpolate colors
        var color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this,R.color.color_worst_percent),
            ContextCompat.getColor(this,R.color.color_best_percent)
        ) as Int
        tvTipDescription.setTextColor(color)

    }

    private fun computeTipAndTotal() {
        // get base tip and percent
        if (etBaseAmount.text.isEmpty()) {
            tvTotalAmount.text = ""
            tvTipAmount.text = ""
            return
        }

        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPerc = seekBarTip.progress
        // compute tip and total
        val tip = baseAmount * tipPerc / 100
        val total = baseAmount + tip
        // update ui
        tvTipAmount.text = "%.2f".format(tip)
        tvTotalAmount.text = "%.2f".format(total)

    }

}


