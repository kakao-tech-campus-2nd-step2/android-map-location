package campus.tech.kakao.map.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import campus.tech.kakao.map.R

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_error)

        val errorMessage = intent.getStringExtra("errorMessage")

        val errortxt: TextView = findViewById(R.id.error_text)
        errortxt.text = errorMessage ?: "Unknown error occurred"
    }
}