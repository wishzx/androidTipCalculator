package com.giganti.gabriele.studiostudy

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
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
    private lateinit var switchSplit : Switch
    private lateinit var splitNumber : EditText
    private lateinit var switchButtonRound : Switch
    private lateinit var tvTotalEach : TextView
    private lateinit var tvTotalEachValue : TextView



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

        switchSplit = findViewById(R.id.switch4)
        splitNumber = findViewById(R.id.editTextNumber4)
        switchButtonRound = findViewById(R.id.switch5)
        tvTotalEach = findViewById(R.id.tvTotalEach)
        tvTotalEachValue = findViewById(R.id.tvTotalEachValue)

        // hide stuff
        splitNumber.visibility = View.INVISIBLE;
        tvTotalEach.visibility = View.INVISIBLE;
        tvTotalEachValue.visibility = View.INVISIBLE;
        switchButtonRound.visibility = View.INVISIBLE;

        // end initialization

        // switch on/off activate/deactivate splitNUmber and tvTotalEach and tvTotalEachValue
        switchSplit.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                computeSplit()
                splitNumber.visibility = View.VISIBLE;
                tvTotalEach.visibility = View.VISIBLE;
                tvTotalEachValue.visibility = View.VISIBLE;
            }
            else {
                splitNumber.visibility = View.INVISIBLE;
                tvTotalEach.visibility = View.INVISIBLE;
                tvTotalEachValue.visibility = View.INVISIBLE;
            }

        }


        // on editing split number recalculate tvTotalEach and tvTotalEachValue
        splitNumber.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) { computeSplit() }

        })


        // on switchButtonRound activated activate the rounding of the correct textviews
        switchButtonRound.setOnCheckedChangeListener{ _, isChecked ->  }


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

        if (switchSplit.isChecked) {
            computeSplit()
        }

    }

    private fun computeSplit() {
        //check if they inputed an empty or 0 split
        if (splitNumber.text.isEmpty() || splitNumber.text.toString().toInt() == 0){
            splitNumber.setText("1");
        }
        //check if base amount is set
        if (tvTotalAmount.text.isEmpty()){
            return
        }

        // get totalAmount and split
        val totalAmount = tvTotalAmount.text.toString().toDouble()
        val split = splitNumber.text.toString().toDouble()

        // compute split
        val totalEach = totalAmount / split ;

        // set totalEach
        tvTotalEachValue.text = "%.2f".format(totalEach)
    }

}


