package com.davidread.starwarsdatabase.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A subclass of [MutableLiveData] meant for sending single events to observers. It differs from
 * [MutableLiveData] in that it will not trigger observers again upon configuration changes.
 *
 * @author Abhishek Srivastava
 * @see <a href="https://abhiappmobiledeveloper.medium.com/android-singleliveevent-of-livedata-for-ui-event-35d0c58512da">Android SingleLiveEvent (of LiveData) for UI Event</a>
 */
class MutableSingleEventLiveData<T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        // Observe the internal MutableLiveData
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}