package campus.tech.kakao.map.Presenter.View

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import campus.tech.kakao.map.Base.ErrorEnum
import campus.tech.kakao.map.R
import java.io.Serializable

class ErrorActivity : AppCompatActivity() {
    private lateinit var errorText : TextView
    private lateinit var errorMsg : TextView
    private lateinit var retry : Button
    private lateinit var type : ErrorEnum
    private lateinit var msg : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        errorText = findViewById<TextView>(R.id.errorText)
        errorMsg = findViewById<TextView>(R.id.errorMsg)
        retry = findViewById<Button>(R.id.retryBtn)

        getExtras()
        settingText()
        setRetryListener()
    }

    private fun getExtras(){
        type = intent.intentSerializable(ERROR_TYPE,ErrorEnum :: class.java) ?: ErrorEnum.ELSE
        msg = intent.extras?.getString(ERROR_MSG) ?: ERROR_UNKNOWN
    }

    private fun settingText(){
        errorText.text = when(type){
            ErrorEnum.MAP_LOAD_ERROR -> getString(R.string.map_error)
            else -> getString(R.string.else_error)
        }

        errorMsg.text = msg
    }

    private fun setRetryListener(){
        retry.setOnClickListener{
            val intent = when(type){
                ErrorEnum.MAP_LOAD_ERROR -> Intent(this,MapActivity::class.java)
                else -> null
            }

            intent?.let{
                startActivity(it)
            }
        }
    }

    private fun <T: Serializable> Intent.intentSerializable(key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as T?
        }
    }

    companion object{
        const val ERROR_TYPE = "type"
        const val ERROR_MSG = "msg"
        const val ERROR_UNKNOWN = "unknown"
    }
}

