package com.longtunman.android.exchangecurrency.bases


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        beforeLoadContentView(savedInstanceState)
        val view = inflater.inflate(getResourceLayout(), container, false)
        afterLoadContentView(view, savedInstanceState)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        this.initialize()
    }

    abstract fun getResourceLayout(): Int

    open protected fun beforeLoadContentView(savedInstanceState: Bundle?) {

    }

    open protected fun afterLoadContentView(view: View, savedInstanceState: Bundle?) {

    }

    open protected fun initialize() {

    }

    //********************************* START COMPACT ACTIVITY ***************************************************************************************************

    fun startCompactActivity(clazz: Class<*>) {
        startCompactActivity(clazz, null)
    }

    fun startCompactActivity(clazz: Class<*>, extras: Bundle?) {
        val intent = Intent()
        intent.setClass(activity!!, clazz)
        startCompactActivity(intent, extras, null)
    }

    fun startCompactActivity(intent: Intent, extras: Bundle?, launchOptions: Bundle?) {
        if (extras != null) {
            intent.putExtras(extras)
        }

        ActivityCompat.startActivity(activity!!, intent, launchOptions)
    }

    //********************************* START NEW STACK ACTIVITY *************************************************************************************************

    fun startNewStackActivity(clazz: Class<*>) {
        startNewStackActivity(clazz, null)
    }

    fun startNewStackActivity(clazz: Class<*>, extras: Bundle?) {
        val intent = Intent()
        intent.setClass(activity!!, clazz)
        startNewStackActivity(intent, extras, null)
    }

    fun startNewStackActivity(intent: Intent?, extras: Bundle?, launchOptions: Bundle?) {
        if (activity == null || intent == null) {
            return
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        if (extras != null) {
            intent.putExtras(extras)
        }

        ActivityCompat.startActivity(activity!!, intent, launchOptions)
    }

    //********************************* REPLACE FRAGMENT **********************************************************************************************************

    protected fun replaceFragment(containerId: Int, fragment: Fragment) {
        replaceFragment(containerId, fragment, null, false, 0, 0)
    }

    protected fun replaceFragment(containerId: Int, fragment: Fragment, tag: String?,
                                  addToBackStack: Boolean, enterAnim: Int, exitAnim: Int) {
        val currentFragment = childFragmentManager.findFragmentById(containerId)

        if (currentFragment != null && currentFragment.javaClass == fragment.javaClass) {
            return
        }

        val ft = childFragmentManager.beginTransaction()

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

    protected fun addFragment(containerId: Int, fragment: Fragment, tag: String?, addToBackStack: Boolean,
                              enterAnim: Int, exitAnim: Int) {
        val ft = childFragmentManager.beginTransaction()

        if (enterAnim != 0 && exitAnim != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim)
        }

        if (addToBackStack) {
            ft.addToBackStack(null)
        }

        ft.add(containerId, fragment, tag)
        ft.commit()
    }

    //********************************* Detect Network **************************************************************************************************************

    fun isConnect(): Boolean {
        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    //********************************* BASE ACTIVITY ****************************************************************************************************************

    protected fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }
}