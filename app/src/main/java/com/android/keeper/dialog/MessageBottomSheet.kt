package com.android.keeper.dialog

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.android.keeper.R
import org.junit.runner.RunWith

class MessageBottomSheet : BottomSheetDialogFragment() {
    private var bottomSheetView: View? = null
    private var messageTitleTextView: TextView? = null
    private var messageSubtitleTextView: TextView? = null
    private var messageImageView: ImageView? = null
    private var messageCloseImageButton: ImageButton? = null
    private var messageTitle: String? = null
    private var messageSubtitle: String? = null
    private var messageTitleColor: String? = null
    private var resource = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_message, container, false)
        messageImageView = bottomSheetView.findViewById<ImageView?>(R.id.message_image)
        messageTitleTextView = bottomSheetView.findViewById<TextView?>(R.id.message_title)
        messageSubtitleTextView = bottomSheetView.findViewById<TextView?>(R.id.message_subtitle)
        messageCloseImageButton = bottomSheetView.findViewById<ImageButton?>(R.id.message_close_btn)
        messageCloseImageButton.setOnClickListener(View.OnClickListener { dismiss() })
        setContent()
        return bottomSheetView
    }

    /**
     * Sets the content when the Bottom Sheet View is created.
     */
    private fun setContent() {
        messageTitleTextView.setText(messageTitle)
        messageTitleTextView.setTextColor(Color.parseColor(messageTitleColor))
        messageSubtitleTextView.setText(messageSubtitle)
    }

    /**
     * Sets title and color to the title
     * @param text Text to be display in the title
     * @param hexColor Color in hexadecimal that will have the title (Sample: "#FF0000")
     */
    fun setTitle(text: String?, hexColor: String?) {
        messageTitle = text
        messageTitleColor = hexColor
    }

    /**
     * Sets the text of the subtitle
     * @param text Text that will be display in the subtitle
     */
    fun setSubtitle(text: String?) {
        messageSubtitle = text
    }

    /**
     * Sets custom image to the bottom sheet
     * @param resource Image resource to be used
     */
    fun setImage(resource: Int) {
        this.resource = resource
    }
}