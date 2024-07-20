package campus.tech.kakao.map

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import java.util.concurrent.atomic.AtomicBoolean

class RecyclerViewIdlingResource(
    private val recyclerView: RecyclerView
) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    private val isIdleNow = AtomicBoolean(false)

    init {
        recyclerView.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                val itemCount = recyclerView.adapter?.itemCount ?: 0
                Log.d("RecyclerViewIdlingResource", "onChanged: itemCount = $itemCount")
                if (itemCount > 0) {
                    isIdleNow.set(true)
                    callback?.onTransitionToIdle()
                }
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onChanged()
            }
        })
    }

    override fun getName(): String {
        return this::class.java.name
    }

    override fun isIdleNow(): Boolean {
        val idle = isIdleNow.get()
        Log.d("RecyclerViewIdlingResource", "isIdleNow: $idle")
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
        if (isIdleNow.get()) {
            callback?.onTransitionToIdle()
        }
    }
}

class RecyclerViewCountingIdlingResource(
    private val recyclerView: RecyclerView,
    private val resource: CountingIdlingResource
) {

    init {
        resource.increment()
        recyclerView.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (recyclerView.adapter?.itemCount ?: 0 > 0) {
                    resource.decrement()
                }
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (recyclerView.adapter?.itemCount ?: 0 > 0) {
                    resource.decrement()
                }
            }
        })
    }
}

