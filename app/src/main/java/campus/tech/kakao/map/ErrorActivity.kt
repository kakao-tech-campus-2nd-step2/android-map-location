package campus.tech.kakao.map

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ErrorActivity : AppCompatActivity() {

    private val errorMsgTextView by lazy { findViewById<TextView>(R.id.english_error_msg) }
    private lateinit var msg: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        msg = intent.getStringExtra("ERROR").toString()
        errorMsgTextView.text = msg
    }
}