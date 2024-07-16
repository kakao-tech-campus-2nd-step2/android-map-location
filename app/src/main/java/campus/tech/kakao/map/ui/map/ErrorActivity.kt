package campus.tech.kakao.map.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.databinding.ActivityErrorBinding
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_MAP_ERROR_MESSAGE

class ErrorActivity : AppCompatActivity() {
    lateinit var binding: ActivityErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setErrorMessage()
    }

    private fun setErrorMessage() {
        val errorMessage = intent.getStringExtra(EXTRA_MAP_ERROR_MESSAGE)
        errorMessage?.let {
            binding.errorMessageTextView.text = it
        }
    }
}
