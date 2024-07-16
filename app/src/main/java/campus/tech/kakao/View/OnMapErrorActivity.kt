package campus.tech.kakao.View

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import campus.tech.kakao.map.R

class OnMapErrorActivity : AppCompatActivity() {
    private lateinit var textError: TextView
    private lateinit var refresh: ImageButton
    private lateinit var contentLoading: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onmaperror_activity)
        initializeView()
        showError()
        refresh.setOnClickListener { whenClickRefreshButton() }
    }

    private fun initializeView() {
        textError = findViewById(R.id.errortype)
        refresh = findViewById(R.id.refresh_button)
        contentLoading = findViewById(R.id.contentLoading)
    }

    private fun showError() {
        val typeError: String? = intent.getStringExtra("ErrorType")
        textError.text = typeError ?: "Unknown error"
    }

    private fun whenClickRefreshButton() {
        refresh.visibility = GONE
        contentLoading.visibility = VISIBLE
    }
}