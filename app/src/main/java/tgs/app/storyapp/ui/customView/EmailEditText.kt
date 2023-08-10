package tgs.app.storyapp.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import tgs.app.storyapp.R

class EmailEditText : AppCompatEditText {

    private lateinit var enabledBackground: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        background = enabledBackground
    }

    private fun init() {
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
    }
}