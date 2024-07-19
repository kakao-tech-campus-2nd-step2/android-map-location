package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val errorTextView: TextView = findViewById(R.id.error_message)
        val errorTextView1: TextView = findViewById(R.id.error_message1)

        Log.d("ErrorActivity", "Error TextView: $errorTextView")
        Log.d("ErrorActivity", "Error TextView 1: $errorTextView1")

        val errorMessage = intent.getStringExtra("error_message") ?: "Unknown Error"

        errorTextView.text = errorMessage
        errorTextView1.text = "다시 시도해주세요."

        val retryButton: Button = findViewById(R.id.retry_button)
        retryButton.setOnClickListener {
            val intent = Intent(this, Map_Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}