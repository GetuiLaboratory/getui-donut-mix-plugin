package com.donut.wxed34e654870cba42

import android.app.Activity
import android.os.Bundle
import android.util.Log

abstract class IdoOnForegroundChangedListener {

    companion object {
        private var resumedCounter = 0
        private var isForeground = false
    }

    fun OnForegroundChangedListener() {}

    protected abstract fun onForegroundChanged(var1: Boolean)

    private fun checkForegroundChanged() {
        try {
           // Log.w("IdoProvider","isForeground ${isForeground} after resumedCounter ${resumedCounter}" )
            if (isForeground && resumedCounter == 0) {
                isForeground = false
                onForegroundChanged(false)
            }
            if (!isForeground && resumedCounter > 0) {
                isForeground = true
                onForegroundChanged(true)
            }
        } catch (var1: Throwable) {
            var1.printStackTrace()
        }
    }

    fun onActivityResumed(var1: Activity?) {
        ++resumedCounter
        checkForegroundChanged()
    }

    fun onActivityPaused(var1: Activity?) {
        if (resumedCounter > 0) {
            --resumedCounter
        }
    }

    fun onActivityStopped(var1: Activity?) {
        checkForegroundChanged()
    }

    fun onActivityCreated(var1: Activity?, var2: Bundle?) {}

    fun onActivityStarted(var1: Activity?) {}

    fun onActivitySaveInstanceState(var1: Activity?, var2: Bundle?) {}

    fun onActivityDestroyed(var1: Activity?) {}

}
