package campus.tech.kakao.map

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MapErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_error)

        val errorMessage: String? = intent.extras?.getString("error")
        val errorText = findViewById<TextView>(R.id.errorText)

        errorText.text = errorMessage
    }
}