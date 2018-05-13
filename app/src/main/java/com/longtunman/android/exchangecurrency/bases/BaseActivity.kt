package com.longtunman.android.exchangecurrency.bases

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeLoadContentView(savedInstanceState)
        setContentView(getResourceLayout())
        afterLoadContentView(savedInstanceState)
    }

    abstract fun getResourceLayout(): Int

    protected fun beforeLoadContentView(savedInstanceState: Bundle?) {

    }

    protected fun afterLoadContentView(savedInstanceState: Bundle?) {

    }

    //********************************* START COMPACT ACTIVITY ***************************************************************************************************

    fun startCompactActivity(clazz: Class<*>) {
        startCompactActivity(clazz, null)
    }

    fun startCompactActivity(clazz: Class<*>, extras: Bundle?) {
        val intent = Intent()
        intent.setClass(this, clazz)
        startCompactActivity(intent, extras, null)
    }

    fun startCompactActivity(intent: Intent, extras: Bundle?, launchOptions: Bundle?) {
        if (extras != null) {
            intent.putExtras(extras)
        }

        ActivityCompat.startActivity(this, intent, launchOptions)
    }

    //********************************* START NEW STACK ACTIVITY *************************************************************************************************

    fun startNewStackActivity(clazz: Class<*>) {
        startNewStackActivity(clazz, null)
    }

    fun startNewStackActivity(clazz: Class<*>, extras: Bundle?) {
        val intent = Intent()
        intent.setClass(this, clazz)
        startNewStackActivity(intent, extras, null)
    }

    fun startNewStackActivity(intent: Intent?, extras: Bundle?, launchOptions: Bundle?) {
        if (intent == null) {
            return
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        if (extras != null) {
            intent.putExtras(extras)
        }

        ActivityCompat.startActivity(this, intent, launchOptions)
    }

    //********************************* REPLACE FRAGMENT **********************************************************************************************************

    protected fun replaceFragment(containerId: Int, fragment: Fragment) {
        replaceFragment(containerId, fragment, null, false, 0, 0, false)
    }

    protected fun replaceFragment(containerId: Int, fragment: Fragment, replaceAnyway: Boolean) {
        replaceFragment(containerId, fragment, null, false, 0, 0, replaceAnyway)
    }

    protected fun replaceFragment(containerId: Int, fragment: Fragment, tag: String?, addToBackStack: Boolean, enterAnim: Int, exitAnim: Int, replaceAnyway: Boolean) {
        val currentFragment = supportFragmentManager.findFragmentById(containerId)

        if (!replaceAnyway) {
            if (currentFragment != null && currentFragment.javaClass == fragment.javaClass) {
                return
            }
        }

        val ft = supportFragmentManager.beginTransaction()

        if (enterAnim != 0 && exitAnim != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim)
        }

        if (addToBackStack) {
            ft.addToBackStack(null)
        }

        ft.replace(containerId, fragment, tag)
        ft.commit()
    }

    //********************************* ADD FRAGMENT ***************************************************************************************************************

    protected fun addFragment(containerId: Int, fragment: Fragment) {
        addFragment(containerId, fragment, null, false, 0, 0)
    }

    protected fun addFragment(containerId: Int, fragment: Fragment, tag: String?, addToBackStack: Boolean, enterAnim: Int, exitAnim: Int) {
        val ft = supportFragmentManager.beginTransaction()

        if (enterAnim != 0 && exitAnim != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim)
        }

        if (addToBackStack) {
            ft.addToBackStack(null)
        }

        ft.add(containerId, fragment, tag)
        ft.commit()
    }

    //********************************* REMOVE FRAGMENT ************************************************************************************************************

    protected fun removeFragment(containerId: Int) {
        val fragment = supportFragmentManager.findFragmentById(containerId)
        removeFragment(fragment)
    }

    protected fun removeFragment(fragment: Fragment?) {
        if (fragment == null) {
            return
        }

        supportFragmentManager.beginTransaction().remove(fragment).commit()
    }

    //********************************* Detect Network **************************************************************************************************************

    fun isConnect(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    //********************************* KEYBOARD *********************************************************************************************************************

    fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}