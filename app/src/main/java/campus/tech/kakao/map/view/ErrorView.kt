package campus.tech.kakao.map.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import campus.tech.kakao.map.R

class ErrorView : LinearLayout {
    lateinit var errorDetail: TextView
    lateinit var retryButton: Button

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_error, this, true)
        errorDetail = findViewById(R.id.errorDetail)
        retryButton = findViewById(R.id.retryButton)
    }

    fun showError(error: String) {
        errorDetail.text = error
        visibility = View.VISIBLE
    }
}
