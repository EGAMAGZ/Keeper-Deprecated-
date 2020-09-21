package com.android.keeper.fragments

import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.keeper.R
import org.junit.runner.RunWith

class ScheduleFragment : Fragment() {
    private var fragmentView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false)
        return fragmentView
    }
}