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

    }
}