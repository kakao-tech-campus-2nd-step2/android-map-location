package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import campus.tech.kakao.map.databinding.ActivityErrorBinding
import campus.tech.kakao.map.databinding.ActivityMainBinding

class ErrorActivity : AppCompatActivity() {
    private lateinit var errorViewBinding: ActivityErrorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        errorViewBinding = ActivityErrorBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(errorViewBinding.root)

        errorViewBinding.errorText.text = intent.getStringExtra("Error")

        errorViewBinding.refreshButton.setOnClickListener {
            val intent = Intent(this@ErrorActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}