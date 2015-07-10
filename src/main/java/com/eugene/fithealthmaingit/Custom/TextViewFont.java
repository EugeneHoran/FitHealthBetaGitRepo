package com.eugene.fithealthmaingit.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

public class TextViewFont extends TextView {

    public TextViewFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public TextViewFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextViewFont(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextViewFont);
            String fontName = a.getString(R.styleable.TextViewFont_fontName);
            if (fontName != null) {
                setTypeface(getTypeFace(fontName));
            }
            a.recycle();
        }
    }

    Typeface typeface;
    public Typeface getTypeFace(String fontName) {
        switch (fontName) {
            case "Roboto-Regular.ttf":
                typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
                break;
            case "Roboto-Bold.ttf":
                typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
                break;
            case "Roboto-Medium.ttf":
                typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
                break;
            case "Roboto-Light.ttf":
                typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
                break;
            default:
                break;
        }
        return typeface;
    }
}
