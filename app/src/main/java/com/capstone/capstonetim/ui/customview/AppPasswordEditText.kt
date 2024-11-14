package com.capstone.capstonetim.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.capstonetim.R

class AppPasswordEditText : AppCompatEditText {

    private lateinit var passwordIconDrawable: Drawable
    private lateinit var toggleVisibilityDrawable: Drawable
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        passwordIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_eye_on) as Drawable
        toggleVisibilityDrawable = ContextCompat.getDrawable(context, R.drawable.ic_eye) as Drawable

        compoundDrawablePadding = 16
        updatePasswordIcon()

        backgroundTintList = ContextCompat.getColorStateList(context, R.color.white)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                when {
                    password.isBlank() -> error = context.getString(R.string.error_empty_password)
                    password.length < 8 -> error = context.getString(R.string.error_password_more_6)
                    else -> error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.x <= totalPaddingLeft && event.x >= paddingLeft) {
                    isPasswordVisible = !isPasswordVisible
                    updatePasswordIcon()
                    updatePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun updatePasswordIcon() {
        val iconRes = if (isPasswordVisible) R.drawable.ic_eye_on else R.drawable.ic_eye
        toggleVisibilityDrawable = ContextCompat.getDrawable(context, iconRes) as Drawable
        setCompoundDrawablesWithIntrinsicBounds(toggleVisibilityDrawable, null, null, null)
    }

    private fun updatePasswordVisibility() {
        val inputType = if (isPasswordVisible) {
            android.text.InputType.TYPE_CLASS_TEXT
        } else {
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setInputType(inputType)
        setSelection(text?.length ?: 0)
    }
}
