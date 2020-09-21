package com.android.keeper.customelements

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.keeper.R
import org.junit.runner.RunWith

/**
 * Custom Toast that contains an icon and a message
 * @author Gamaliel Garcia
 */
class IconToast private constructor(context: Context, message: String?, length: Int, imageResource: Int) {
    private val context: Context?
    private val message: String?
    private val imageResource: Int
    private val length: Int

    /**
     *
     * Function that displays the custom toast
     */
    fun show() {
        // Checks the android version to create custom toast or not
        mToast = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            // If the android version is lower to Pie, it will generate a custom toast
            generateCustomToast()
        } else {
            Toast.makeText(context, message, length)
        }
        mToast.show()
    }

    /**
     * Function to generate CustomToast with parameters passed in the constructor
     * @return Custom toast generated
     */
    private fun generateCustomToast(): Toast? {
        val toast = Toast(context) // Creates Toast object
        val inflater = LayoutInflater.from(context)

        // Creates View form existing layout
        val layout = inflater.inflate(R.layout.toast_layout, (context as Activity?).findViewById<View?>(R.id.toast_root) as ViewGroup?)

        // Gets views from the layout
        val toastText = layout.findViewById<TextView?>(R.id.toast_text)
        val toastImage = layout.findViewById<ImageView?>(R.id.toast_image)
        toastText.setText(message)
        toastImage.setImageResource(imageResource)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout // Sets layout to the toast
        return toast
    }

    companion object {
        private var sIconToast: IconToast? = null
        private var mToast: Toast? = null

        /**
         *
         *  Function that will create an object from IconToast class
         * @param context Application context when the class is called
         * @param message Message that will be shown in the toast
         * @param length Time that will be shown the toast
         * @param imageResource Icon that will be used
         * @return IconToast class for the display of the toast
         */
        fun makeContent(context: Context, message: String?, length: Int, imageResource: Int): IconToast? {
            if (sIconToast == null) {
                // Checks if class was already created
                sIconToast = IconToast(context, message, length, imageResource)
            }
            return sIconToast
        }
    }

    /**
     *
     * Private constructor for IconToast class to create a toast
     * @param context Application context when the class is called
     * @param message Message that will be shown in the toast
     * @param length Time that will be shown the toast
     * @param imageResource Icon that will be used
     */
    init {
        this.context = context
        this.message = message
        this.imageResource = imageResource
        this.length = length
    }
}