package com.longtunman.android.exchangecurrency.fragments

import android.os.Bundle
import android.view.View
import com.longtunman.android.exchangecurrency.R
import com.longtunman.android.exchangecurrency.bases.BaseFragment
import kotlinx.android.synthetic.main.fragment_exchange_currency.*
import java.math.BigDecimal


class ExchangeCurrencyFragment : BaseFragment(), View.OnClickListener {
    var currency: Boolean = true
    private val CURRENCY_YEN: String = "YEN"
    private val CURRENCY_THB: String = "THB"
    private val CURRENCY_CUSTOM: String = "CURRENCY_CUSTOM"

    var typeEditText: String = CURRENCY_YEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val buttons = listOf(buttonOne, buttonTwo, buttonThree,
                buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight,
                buttonNine, buttonZero, buttonDot, buttonClear, buttonConvert, buttonTHB, buttonYEN)

        buttons.forEach {
            it.setOnClickListener(this)
        }

        val editTexts = listOf(editTextCustomYen, editTextAmountYEN, editTextAmountTHB)

        editTexts.forEach {
            it.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonOne -> {
                presentNumbers("1")
            }
            R.id.buttonTwo -> {
                presentNumbers("2")
            }
            R.id.buttonThree -> {
                presentNumbers("3")
            }
            R.id.buttonFour -> {
                presentNumbers("4")
            }
            R.id.buttonFive -> {
                presentNumbers("5")
            }
            R.id.buttonSix -> {
                presentNumbers("6")
            }
            R.id.buttonSeven -> {
                presentNumbers("7")
            }
            R.id.buttonEight -> {
                presentNumbers("8")
            }
            R.id.buttonNine -> {
                presentNumbers("9")
            }
            R.id.buttonZero -> {
                presentNumbers("0")
            }
            R.id.buttonDot -> {
                presentNumbers(".")
            }
            R.id.buttonClear -> {
                resetNumbers()
            }
            R.id.buttonConvert -> {
                presentNumbers(null)
            }
            R.id.buttonYEN -> {
                buttonYEN.setBackgroundColor(resources.getColor(R.color.md_green_400))
                buttonTHB.setBackgroundColor(resources.getColor(R.color.md_grey_600))

                currency = true
                typeEditText = CURRENCY_YEN
            }
            R.id.buttonTHB -> {
                buttonTHB.setBackgroundColor(resources.getColor(R.color.md_green_400))
                buttonYEN.setBackgroundColor(resources.getColor(R.color.md_grey_600))

                currency = false
                typeEditText = CURRENCY_THB
            }
            R.id.editTextCustomYen -> {
                typeEditText = CURRENCY_CUSTOM
            }
            R.id.editTextAmountYEN -> {
                buttonYEN.setBackgroundColor(resources.getColor(R.color.md_green_400))
                buttonTHB.setBackgroundColor(resources.getColor(R.color.md_grey_600))

                currency = true
                typeEditText = CURRENCY_YEN
            }
            R.id.editTextAmountTHB -> {
                buttonTHB.setBackgroundColor(resources.getColor(R.color.md_green_400))
                buttonYEN.setBackgroundColor(resources.getColor(R.color.md_grey_600))

                currency = false
                typeEditText = CURRENCY_THB
            }
            else -> {
                resetNumbers()
            }
        }
    }

    private fun presentNumbers(number: String?) {
        number?.let {
            when (typeEditText) {
                CURRENCY_YEN -> editTextAmountYEN.setText(validateNumbers(number))
                CURRENCY_THB -> editTextAmountTHB.setText(validateNumbers(number))
                CURRENCY_CUSTOM -> editTextCustomYen.setText(validateNumbers(number))
                else -> {
                    editTextAmountYEN.setText(validateNumbers(number))
                }
            }
            return
        }

        if (editTextCustomYen.text.toString() == "") {
            val amountTHB: Double = editTextAmountYEN.text.toString().toDouble() / 3.43
            val amountYEN: Double = editTextAmountTHB.text.toString().toDouble() * 3.43

            if (currency) {
                editTextAmountTHB.setTextColor(resources.getColor(R.color.md_green_400))
                editTextAmountYEN.setTextColor(resources.getColor(R.color.md_grey_600))

                editTextAmountTHB.setText((BigDecimal(amountTHB).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()).toString())
            } else {
                editTextAmountYEN.setTextColor(resources.getColor(R.color.md_green_400))
                editTextAmountTHB.setTextColor(resources.getColor(R.color.md_grey_600))

                editTextAmountYEN.setText((BigDecimal(amountYEN).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()).toString())
            }

            return

        } else {
            val customYEN: Double = editTextCustomYen.text.toString().toDouble()
            val amountYEN = editTextAmountYEN.text.toString().toDouble()
            var amountTHB: Double = editTextAmountTHB.text.toString().toDouble()

            if (amountYEN > 0 || amountTHB > 0) {
                if (currency) {
                    editTextAmountTHB.setText((BigDecimal((amountYEN / customYEN).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()).toString())
                } else {
                    editTextAmountYEN.setText((BigDecimal((amountTHB * customYEN)).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()).toString())
                }
            } else {
                amountTHB = customYEN / customYEN

                editTextAmountYEN.setText((BigDecimal(customYEN).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()).toString())
                editTextAmountTHB.setText((BigDecimal(amountTHB).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()).toString())
            }

            return
        }
    }

    private fun validateNumbers(number: String?): String {
        var amount: String
        amount = when (typeEditText) {
            CURRENCY_YEN -> editTextAmountYEN.text.toString()
            CURRENCY_THB -> editTextAmountTHB.text.toString()
            CURRENCY_CUSTOM -> editTextCustomYen.text.toString()
            else -> {
                editTextAmountYEN.text.toString()
            }
        }

        when (number) {
            "0" -> {
                if (amount == "0") resetNumbers() else amount += number
            }
            "." -> {
                if (amount == "0") {
                    amount = "0."
                    buttonDot.isEnabled = false
                } else {
                    val hasDot = amount.contains(".")
                    if (hasDot) buttonDot.isEnabled = false else amount += "."
                }
            }
            else -> {
                val firstIndexAmount = amount.substring(0)
                if (firstIndexAmount == "0") amount = number!! else amount += number
            }
        }

        return amount
    }

    fun resetNumbers() {
        editTextAmountYEN.setText("0")
        editTextAmountTHB.setText("0")
        editTextCustomYen.setText("")
        editTextAmountYEN.setTextColor(resources.getColor(R.color.md_black_1000))
        editTextAmountTHB.setTextColor(resources.getColor(R.color.md_black_1000))

        buttonDot.isEnabled = true
    }

    override fun getResourceLayout(): Int {
        return R.layout.fragment_exchange_currency
    }
}