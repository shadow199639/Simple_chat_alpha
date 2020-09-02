package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.PorterDuff.Mode
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils
import kotlin.math.min

@ColorInt
private const val DEFAULT_COLOR = Color.WHITE


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyle) {

    //set 2 dp for width
    private var borderWidth = (2 * this.resources.displayMetrics.density + 0.5f).toInt()
    private var borderColor = DEFAULT_COLOR
    private var text: String? = null
    private var bitmap: Bitmap? = null

    init {
        attrs?.let { attributeSet ->
            val a = context.obtainStyledAttributes(attributeSet, R.styleable.CircleImageView)
            borderWidth =
                a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, borderWidth)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_COLOR)
            a.recycle()
        }
    }

    @Dimension
    fun getBorderWidth(): Int = (borderWidth / this.resources.displayMetrics.density + 0.5f).toInt()

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = (dp * this.resources.displayMetrics.density + 0.5f).toInt()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        this.invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(App.applicationContext(), colorId)
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        var bitmap = getBitmapFromDrawable() ?: return
        if (width == 0 || height == 0) return

        bitmap = getScaledBitmap(bitmap, width)
        bitmap = getCenterCroppedBitmap(bitmap, width)
        bitmap = getCircleBitmap(bitmap)

        if (borderWidth > 0)
            bitmap = getStrokedBitmap(bitmap, borderWidth, borderColor)

        canvas.drawBitmap(bitmap, 0F, 0F, null)
    }

    fun generateAvatar(text: String?, sizeSp: Int, theme: Resources.Theme) {
        if (bitmap == null || text != this.text) {
            val image =
                if (text == null) {
                    getDefaultAvatar(theme)
                } else getInitials(text, sizeSp, theme)

            this.text = text
            bitmap = image
            setImageBitmap(bitmap)
            invalidate()
        }
    }

    private fun getInitials(text: String, sizeSp: Int, theme: Resources.Theme): Bitmap {
        val image = getDefaultAvatar(theme)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = sizeSp.toFloat()
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER

        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)

        val backgroundBounds = RectF()
        backgroundBounds.set(0f, 0f, layoutParams.height.toFloat(), layoutParams.height.toFloat())

        val textBottom = backgroundBounds.centerY() - textBounds.exactCenterY()
        val canvas = Canvas(image)
        canvas.drawText(text, backgroundBounds.centerX(), textBottom, paint)

        return image
    }

    private fun getDefaultAvatar(theme: Resources.Theme): Bitmap {
        val image = Bitmap.createBitmap(layoutParams.height, layoutParams.height, Config.ARGB_8888)
        val color = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, color, true)

        val canvas = Canvas(image)
        canvas.drawColor(color.data)

        return image
    }

    private fun getStrokedBitmap(squareBmp: Bitmap, strokeWidth: Int, color: Int): Bitmap {
        val inCircle = RectF()
        val strokeStart = strokeWidth / 2F
        val strokeEnd = squareBmp.width - strokeWidth / 2F

        inCircle.set(strokeStart, strokeStart, strokeEnd, strokeEnd)

        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        strokePaint.color = color
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth.toFloat()

        val canvas = Canvas(squareBmp)
        canvas.drawOval(inCircle, strokePaint)

        return squareBmp
    }

    private fun getCenterCroppedBitmap(bitmap: Bitmap, size: Int): Bitmap {
        val cropStartX = (bitmap.width - size) / 2
        val cropStartY = (bitmap.height - size) / 2

        return Bitmap.createBitmap(bitmap, cropStartX, cropStartY, size, size)
    }

    private fun getScaledBitmap(bitmap: Bitmap, minSide: Int): Bitmap {
        return if (bitmap.width != minSide || bitmap.height != minSide) {
            val smallest = min(bitmap.width, bitmap.height).toFloat()
            val factor = smallest / minSide
            Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width / factor).toInt(),
                (bitmap.height / factor).toInt(),
                false
            )
        } else bitmap
    }

    private fun getBitmapFromDrawable(): Bitmap? {
        if (bitmap != null)
            return bitmap

        if (drawable == null)
            return null

        if (drawable is BitmapDrawable)
            return (drawable as BitmapDrawable).bitmap

        val bitmap =
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val smallest = min(bitmap.width, bitmap.height)
        val outputBmp = Bitmap.createBitmap(smallest, smallest, Config.ARGB_8888)
        val canvas = Canvas(outputBmp)

        val paint = Paint()
        val rect = Rect(0, 0, smallest, smallest)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(smallest / 2F, smallest / 2F, smallest / 2F, paint)

        paint.xfermode = PorterDuffXfermode(Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return outputBmp
    }
}