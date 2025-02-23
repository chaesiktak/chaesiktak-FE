import android.app.Dialog
import android.content.Context
import android.os.Looper
import android.widget.ImageView
import com.example.chaesiktak.R
import android.os.Handler

class LoadingDialog(context: Context) : Dialog(context) {

    private val loadingImages = arrayOf(
        R.drawable.loading1,
        R.drawable.loading2,
        R.drawable.loading3,
        R.drawable.loading4,
        R.drawable.loading5,
        R.drawable.loading6,
        R.drawable.loading7
    )

    private var imageIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var imageView: ImageView

    private val imageSwitcher = object : Runnable {
        override fun run() {
            imageIndex = (imageIndex + 1) % loadingImages.size
            imageView.setImageResource(loadingImages[imageIndex])
            handler.postDelayed(this, 300) // 0.3초마다 이미지 변경
        }
    }

    init {
        setContentView(R.layout.loading_dialog)
        imageView = findViewById(R.id.loadingImageView) // ✅ 여기서 초기화
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun startAnimation() {
        handler.post(imageSwitcher)
    }

    fun stopAnimation() {
        handler.removeCallbacks(imageSwitcher)
        dismiss()
    }
}
