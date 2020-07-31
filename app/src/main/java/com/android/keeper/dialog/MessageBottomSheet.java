package com.android.keeper.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.keeper.R;

public class MessageBottomSheet extends BottomSheetDialogFragment {

    private View bottomSheetView;
    private TextView messageTitleTextView,messageSubtitleTextView;
    private ImageView messageImageView;
    private ImageButton messageCloseImageButton;

    private String messageTitle,messageSubtitle,messageTitleColor;
    private int resource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetView=inflater.inflate(R.layout.bottom_sheet_message,container,false);

        messageImageView=bottomSheetView.findViewById(R.id.message_image);

        messageTitleTextView=bottomSheetView.findViewById(R.id.message_title);
        messageSubtitleTextView=bottomSheetView.findViewById(R.id.message_subtitle);

        messageCloseImageButton=bottomSheetView.findViewById(R.id.message_close_btn);

        messageCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setContent();

        return bottomSheetView;
    }

    /**
     * Sets the content when the Bottom Sheet View is created.
     * */
    private void setContent(){
        messageTitleTextView.setText(messageTitle);
        messageTitleTextView.setTextColor(Color.parseColor(messageTitleColor));
        messageSubtitleTextView.setText(messageSubtitle);
    }

    /**
     * Sets title and color to the title
     * @param text Text to be display in the title
     * @param hexColor Color in hexadecimal that will have the title (Sample: "#FF0000")
     * */
    public void setTitle(String text, String hexColor){
        messageTitle=text;
        messageTitleColor=hexColor;
    }

    /**
     * Sets the text of the subtitle
     * @param text Text that will be display in the subtitle
     * */
    public void setSubtitle(String text){
        messageSubtitle=text;
    }

    /**
     * Sets custom image to the bottom sheet
     * @param resource Image resource to be used
     * */
    public void setImage(int resource){
        this.resource=resource;
    }

}
