package campus.tech.kakao.map
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        // Edge-to-Edge 화면 지원 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val retryButton: ImageButton = findViewById(R.id.robot)

        retryButton.setOnClickListener {
            // 다시 MapActivity로 이동
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }

    }
}