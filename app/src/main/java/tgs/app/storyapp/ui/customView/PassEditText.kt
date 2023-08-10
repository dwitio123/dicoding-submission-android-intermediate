package tgs.app.storyapp.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import tgs.app.storyapp.R

class PassEditText : AppCompatEditText {

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
        hint = "Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        background = enabledBackground
    }

    private fun init() {
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Do nothing.
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if(p0.length < 8) {
                    error = context.getString(R.string.minimum_length_8_characters)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //Do nothing.
            }

        })
    }
}