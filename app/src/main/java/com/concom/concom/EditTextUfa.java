package com.concom.concom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Luis Fernando Briguelli da Silva on 07/06/2014.
 */
public class EditTextUfa extends EditText {
    public EditTextUfa(Context context) {
        super(context);
        init();
    }

    public EditTextUfa(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextUfa(Context context, AttributeSet attrs, int defStyle) {
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
