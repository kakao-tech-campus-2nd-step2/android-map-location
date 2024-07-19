package campus.tech.kakao.map

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class AuthenticationErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_error)

        val retryIcon: ImageView = findViewById(R.id.retry_icon)
        val loadingIndicator: ProgressBar = findViewById(R.id.loading_indicator)

        retryIcon.setOnClickListener {
            loadingIndicator.visibility = ProgressBar.VISIBLE
        }
    }
}
