package com.android.keeper.dialogimport

import android.app.Dialog
import android.support.v4.app.DialogFragment
import java.util.*

org.junit.runner.RunWithimport android.support.test.runner.AndroidJUnit4import android.support.test.InstrumentationRegistryimport android.os.Buildimport android.app.Activityimport android.database.sqlite.SQLiteDatabaseimport com.android.keeper.util.PreferenceUtilimport android.content.SharedPreferencesimport android.content.SharedPreferences.Editorimport android.support.design.widget.BottomSheetDialogFragmentimport android.widget.TextViewimport android.widget.ImageButtonimport android.view.LayoutInflaterimport android.view.ViewGroupimport android.os.Bundleimport com.android.keeper.Rimport com.android.keeper.dialog.EditTaskBottomSheet.EditTaskBottomSheetListenerimport android.widget.EditTextimport com.android.keeper.localdb.SQLiteConnectionimport android.app.DatePickerDialog.OnDateSetListenerimport android.widget.DatePickerimport com.android.keeper.dialog.TimePickerDialogFragmentimport android.app.TimePickerDialog.OnTimeSetListenerimport android.widget.TimePickerimport android.content.DialogInterfaceimport com.android.keeper.dialog.DatePickerDialogFragmentimport com.android.keeper.localdb.utilities.TasksUtilitiesimport com.android.keeper.util.CursorUtilimport com.android.keeper.util.CalendarUtilimport android.content.ContentValuesimport android.app.AlarmManagerimport android.content.Intentimport com.android.keeper.notifications.AlertReceiverimport android.app.PendingIntentimport com.android.keeper.dialog.AddNewTaskBottomSheet.AddNewTaskBottomSheetListenerimport com.android.keeper.notifications.NotificationHelperimport android.widget.RelativeLayoutimport android.app.DatePickerDialogimport android.app.TimePickerDialogimport com.android.keeper.dialog.AddNewReminderBottomSheet.AddNewReminderBottomSheetListenerimport android.widget.LinearLayoutimport com.android.keeper.localdb.utilities.RemindersUtilitiesimport com.android.keeper.localdb.utilities.NotesUtilitiesimport android.database.sqlite.SQLiteDatabase.CursorFactoryimport android.database.sqlite.SQLiteOpenHelperimport com.android.keeper.recycle_items.NoteItemimport com.android.keeper.recycle_items.TaskItemimport android.support.v7.widget.RecyclerViewimport com.android.keeper.viewholders.TasksViewHolderimport android.widget.Filter.FilterResultsimport com.android.keeper.recycle_items.ReminderItemimport com.android.keeper.viewholders.RemindersViewHolderimport android.widget.ScrollViewimport android.support.design.bottomappbar.BottomAppBarimport android.widget.Toastimport android.support.design.widget.CoordinatorLayoutimport android.support.design.widget.FloatingActionButtonimport android.widget.ProgressBarimport com.android.keeper.adapters.TasksAdapterimport android.view.animation.Animationimport com.android.keeper.dialog.EditTaskBottomSheetimport com.android.keeper.dialog.AddNewTaskBottomSheetimport android.support.v7.widget.LinearLayoutManagerimport android.annotation .SuppressLintimport android.view.animation.Animation.AnimationListenerimport com.android.keeper.customelements.IconToastimport com.android.keeper.dialog.MessageBottomSheetimport com.android.keeper.adapters.RemindersAdapterimport com.android.keeper.dialog.AddNewReminderBottomSheetimport android.view.View.OnLongClickListenerimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.support.annotation .RequiresApiimport android.app.NotificationChannelimport android.content.ContextWrapperimport com.android.keeper.MainActivityimport android.support.v4.app.NotificationCompatimport android.support.v7.app.AppCompatActivityimport android.support.design.widget.NavigationViewimport com.android.keeper.fragments.TasksFragmentimport com.android.keeper.fragments.RemindersFragmentimport android.support.v4.widget.DrawerLayoutimport com.android.keeper.fragments.NotesFragmentimport android.support.v4.content.ContextCompatimport android.support.v4.view.GravityCompatimport android.support.design.internal .NavigationMenuItemViewimport com.android.keeper.fragments.ScheduleFragmentimport com.android.keeper.SettingsActivityimport android.view.inputmethod.EditorInfoimport android.view.WindowManagerimport android.widget.Spinnerimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport android.widget.CompoundButtonimport android.widget.ArrayAdapter
/**
 * Represents date picker dialog fragment
 *
 * @author Gamaliel Garcia
 */
class DatePickerDialogFragment : DialogFragment() {
    var onDateSetListener: OnDateSetListener? = null
    var onDismissListener: DialogInterface.OnDismissListener? = null

    /**
     * Sets callback when the date is selected by the user
     */
    fun setCallBack(ondate: OnDateSetListener?) {
        onDateSetListener = ondate
    }

    /**
     * Sets callback when the user dismiss date picker dialog
     */
    fun setOnDismissListener(ondismiss: DialogInterface.OnDismissListener?) {
        //TODO: Check if this is executed
        onDismissListener = ondismiss
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(activity, onDateSetListener, year, month, day)
    }
}