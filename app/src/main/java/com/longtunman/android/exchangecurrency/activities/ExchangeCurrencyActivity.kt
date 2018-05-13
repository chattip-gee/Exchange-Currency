package com.longtunman.android.exchangecurrency.activities

import android.os.Bundle
import com.longtunman.android.exchangecurrency.R
import com.longtunman.android.exchangecurrency.bases.BaseActivity
import com.longtunman.android.exchangecurrency.fragments.ExchangeCurrencyFragment

class ExchangeCurrencyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(R.id.fragment_container, ExchangeCurrencyFragment())
    }

    override fun getResourceLayout(): Int {
        return R.layout.activity_main
    }
}
