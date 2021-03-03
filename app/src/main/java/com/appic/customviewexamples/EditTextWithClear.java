package com.appic.customviewexamples;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

public class EditTextWithClear extends AppCompatEditText {

    private Drawable mClearButtonImage;

    public EditTextWithClear(@NonNull Context context) {
        super(context);
        init();
    }

    public EditTextWithClear(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public EditTextWithClear(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mClearButtonImage = ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_clear_opaque_24dp, null);
        // TODO: If the X (clear) button is tapped, clear the text.
        // TODO: If the text changes, show or hide the X (clear) button.
        setCompoundDrawablesRelativeWithIntrinsicBounds
                (null,                      // Start of text.
                        null,               // Above text.
                        mClearButtonImage,  // End of text.
                        null);              // Below text.
    }

}
