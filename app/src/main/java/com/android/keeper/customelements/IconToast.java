package com.android.keeper.customelements;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keeper.R;

public final class IconToast {

    private static  IconToast sIconToast;
    private static Toast mToast;

    private final Context context;
    private final String message;
    private final int imageResource;
    private final int length;

    private IconToast(@NonNull final Context context,final String message, final int length,final int imageResource) {
        this.context=context;
        this.message=message;
        this.imageResource=imageResource;
        this.length=length;
    }

    public static IconToast makeContent(@NonNull final Context context,String message,int length, int imageResource){
        if(sIconToast==null){
            sIconToast=new IconToast(context,message,length,imageResource);
        }
        return sIconToast;
    }

    public void show(){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            mToast=generateCustomToast();
        }else{
            mToast=Toast.makeText(context,message,length);
        }
        mToast.show();
    }
    private Toast generateCustomToast(){

        Toast toast= new Toast(context);
        LayoutInflater inflater=LayoutInflater.from(context);

        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) ((Activity) context).findViewById(R.id.toast_root));

        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastText.setText(message);
        toastImage.setImageResource(imageResource);

        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        return toast;
    }
}
