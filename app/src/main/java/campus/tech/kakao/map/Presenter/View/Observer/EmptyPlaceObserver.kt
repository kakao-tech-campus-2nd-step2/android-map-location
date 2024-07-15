package campus.tech.kakao.map.Presenter.View.Observer

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmptyPlaceObserver(recyclerView: RecyclerView, textView: TextView) :
    RecyclerView.AdapterDataObserver() {
    private var emptyView: TextView? = null
    private var recyclerView: RecyclerView? = null

    init {
        this.recyclerView = recyclerView
        emptyView = textView
        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        if (emptyView != null && recyclerView!!.adapter != null) {
            val isRecyclerHasData = recyclerView!!.adapter!!.itemCount != 0

            if (isRecyclerHasData) {
                recyclerView?.let{
                    it.visibility = VISIBLE
                }
                emptyView?.let{
                    it.visibility = GONE
                }
            } else {
                recyclerView?.let{
                    it.visibility = GONE
                }
                emptyView?.let{
                    it.visibility = VISIBLE
                }
            }
        }
    }

    override fun onChanged() {
        super.onChanged()
        checkIfEmpty()
    }
}