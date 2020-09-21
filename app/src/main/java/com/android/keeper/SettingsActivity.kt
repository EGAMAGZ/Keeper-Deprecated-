package com.android.keeper

import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.android.keeper.util.PreferenceUtil
import org.junit.runner.RunWith

class SettingsActivity : AppCompatActivity() {
    private var clockFormatSpinner: Spinner? = null
    private var lastFragmentSwitch: Switch? = null
    private var screenOnSwitch: Switch? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        clockFormatSpinner = findViewById<View?>(R.id.settings_clock_format_spinner) as Spinner?
        lastFragmentSwitch = findViewById<View?>(R.id.settings_last_fragment_switch) as Switch?
        screenOnSwitch = findViewById<View?>(R.id.settings_keep_on_switch) as Switch?
        val toolbar = findViewById<Toolbar?>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar.setTitle("Settings")
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.thirdColor)
        setSettingsOptions()
        clockFormatSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val option = parent.getItemAtPosition(position).toString()
                var format: String? = null
                when (option) {
                    "Automatic" -> format = "auto"
                    "24 Hours" -> format = "24hr"
                    "12 Hours" -> format = "12hr"
                }
                PreferenceUtil.Companion.getInstance(applicationContext).setClockFormat(format)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
        lastFragmentSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked -> PreferenceUtil.Companion.getInstance(applicationContext).setChangeLastFragment(isChecked) })
        screenOnSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            PreferenceUtil.Companion.getInstance(applicationContext).setKeepScreenOn(isChecked)
            setKeepScreenOn()
        })
        setKeepScreenOn()
    }

    private fun setSettingsOptions() {
        val adapter = ArrayAdapter.createFromResource(applicationContext, R.array.clock_format_options, R.layout.item_spinner)
        clockFormatSpinner.setAdapter(adapter)
        when (PreferenceUtil.Companion.getInstance(applicationContext).getClockFormat()) {
            "auto" -> clockFormatSpinner.setSelection(0)
            "24hr" -> clockFormatSpinner.setSelection(1)
            "12hr" -> clockFormatSpinner.setSelection(2)
        }
        lastFragmentSwitch.setChecked(PreferenceUtil.Companion.getInstance(applicationContext).getChangeLastFragment())
        screenOnSwitch.setChecked(PreferenceUtil.Companion.getInstance(applicationContext).getKeepScreenOn())
    }

    private fun setKeepScreenOn() {
        if (PreferenceUtil.Companion.getInstance(applicationContext).getKeepScreenOn()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}