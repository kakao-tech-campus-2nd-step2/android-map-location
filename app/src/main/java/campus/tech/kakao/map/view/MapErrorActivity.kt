package campus.tech.kakao.map.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.databinding.ActivityMapErrorBinding

class MapErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.errorDescriptionTextView.text =
            intent.getStringExtra("errorDescription") ?: "Unknown error"

        binding.errorCodeTextView.text =
            intent.getStringExtra("errorCode") ?: "Unknown error code"

        binding.refreshBackgroundView.setOnClickListener {
            finish()
        }
    }
}