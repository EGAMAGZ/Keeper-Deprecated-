package com.android.keeper.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.keeper.R;

public class MessageBottomSheet extends BottomSheetDialogFragment {

    private View bottomSheetView;
    private TextView messageTitleTextView,messageSubtitleTextView;
    private ImageView messageImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetView=inflater.inflate(R.layout.bottom_sheet_message,container,false);

        messageImageView=bottomSheetView.findViewById(R.id.message_image);

        messageTitleTextView=bottomSheetView.findViewById(R.id.message_title);
        messageSubtitleTextView=bottomSheetView.findViewById(R.id.message_subtitle);

        return bottomSheetView;
    }

    public void setTitle(String text, String hexColor){
        messageTitleTextView.setText(text);
        messageTitleTextView.setTextColor(Color.parseColor(hexColor));
    }

    public void setSubtitle(String text, String hexColor){
        messageSubtitleTextView.setText(text);
        messageSubtitleTextView.setTextColor(Color.parseColor(hexColor));
    }

    public void setImage(int resource){
        messageImageView.setImageResource(resource);
    }

}
