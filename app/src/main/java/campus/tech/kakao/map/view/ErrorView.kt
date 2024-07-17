package campus.tech.kakao.map.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import campus.tech.kakao.map.databinding.ViewErrorBinding

class ErrorView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    errorMessage: String = "",
    retry: (() -> Unit)? = null,
) : FrameLayout(context, attrs, defStyleAttr) {
    private var binding: ViewErrorBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewErrorBinding.inflate(inflater, this, true)
        binding.message.text = errorMessage
        binding.refresh.visibility = if (retry != null) VISIBLE else GONE
        binding.refresh.setOnClickListener {
            retry?.invoke()
            binding.progressCircular.visibility = VISIBLE
        }
    }
}
