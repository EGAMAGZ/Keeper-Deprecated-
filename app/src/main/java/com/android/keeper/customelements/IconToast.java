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

/**
 * Custom Toast that contains an icon and a message
 * @author Gamaliel Garcia
 * */

public final class IconToast {

    private static  IconToast sIconToast;
    private static Toast mToast;

    private final Context context;
    private final String message;
    private final int imageResource;
    private final int length;

    /**
     * <p>Private constructor for IconToast class to create a toast</p>
     * @param context Application context when the class is called
     * @param message Message that will be shown in the toast
     * @param length Time that will be shown the toast
     * @param imageResource Icon that will be used
     * */
    private IconToast(@NonNull final Context context,final String message, final int length,final int imageResource) {
        this.context=context;
        this.message=message;
        this.imageResource=imageResource;
        this.length=length;
    }
    /**
     * <p> Function that will create an object from IconToast class</p>
     * @param context Application context when the class is called
     * @param message Message that will be shown in the toast
     * @param length Time that will be shown the toast
     * @param imageResource Icon that will be used
     * @return IconToast class for the display of the toast
     * */
    public static IconToast makeContent(@NonNull final Context context,String message,int length, int imageResource){
        if(sIconToast==null){
            // Checks if class was already created
            sIconToast=new IconToast(context,message,length,imageResource);
        }
        return sIconToast;
    }

    /**
     * <p>Function that displays the custom toast</p>
     * */
    public void show(){
        // Checks the android version to create custom toast or not
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            // If the android version is lower to Pie, it will generate a custom toast
            mToast=generateCustomToast();
        }else{
            mToast=Toast.makeText(context,message,length);
        }
        mToast.show();
    }
    /**
     * Function to generate CustomToast with parameters passed in the constructor
     * @return Custom toast generated
     * */
    private Toast generateCustomToast(){

        Toast toast= new Toast(context); // Creates Toast object
        LayoutInflater inflater=LayoutInflater.from(context);

        // Creates View form existing layout
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) ((Activity) context).findViewById(R.id.toast_root));

        // Gets views from the layout
        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastText.setText(message);
        toastImage.setImageResource(imageResource);

        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout); // Sets layout to the toast

        return toast;
    }
}
