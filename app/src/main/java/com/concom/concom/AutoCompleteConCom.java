package com.concom.concom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

/**
 * Created by Luis Fernando Briguelli da Silva on 07/06/2014.
 */
public class AutoCompleteConCom extends AutoCompleteTextView {
    public AutoCompleteConCom(Context context) {
        super(context);
        init();
    }

    public AutoCompleteConCom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoCompleteConCom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() throws RuntimeException{
        this.setTextColor(getResources().getColor(R.color.grey_800));
        this.setHintTextColor(getResources().getColor(R.color.grey_500));
        this.setBackgroundResource(R.drawable.bg_edit_text_border_bottom);
        this.setPadding(10,10,10,10);
        this.setTypeface(TypeFaces.getTypeFace(getContext(),"Roboto_Thin.ttf"));

    }
}
