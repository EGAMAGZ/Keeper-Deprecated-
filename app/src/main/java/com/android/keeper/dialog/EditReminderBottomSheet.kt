package com.android.keeper.dialogimport

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.keeper.R

class EditReminderBottomSheet : BottomSheetDialogFragment() {
    private var bottomSheetView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_edit_reminder, container, false)
        return bottomSheetView
    }
}