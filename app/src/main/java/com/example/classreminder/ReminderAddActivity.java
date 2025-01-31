/*
 * Copyright 2015 Blanyal D'Souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.classreminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ReminderAddActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener {

    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mDateText, mTimeStartText, mTimeEndText, mClassAppText, mClassDescriptionText, mInstructorText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mCalendar;
    private int mHourStart, mMinuteStart;
    private int mHourEnd, mMinuteEnd;
    private int selectedTimePicker;
    private String mTitle;
    private String mTimeStart;
    private String mTimeEnd;
    private String mDate;
    private String mClassApp;
    private String mClassDescription;
    private String mInstructor;
    private String mActive;

    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME_START = "time_start_key";
    private static final String KEY_TIME_END = "time_end_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_COLOR = "color_key";
    private static final String KEY_APPLICATION_TITLE = "class_title_key";
    private static final String KEY_CLASS_DESCRIPTION = "class_description_key";
    private static final String KEY_INSTRUCTOR_NAME = "instructor_name_key";
    private static final String KEY_ACTIVE = "active_key";

    // RegEx to split string days
    private static final String DATE_DELIMITER = ", ";

    // check time picker dialog selected (time start / time end)
    private static final int TIME_START = 1;
    private static final int TIME_END = 2;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        // Initialize Views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (EditText) findViewById(R.id.reminder_title);
        mDateText = (TextView) findViewById(R.id.set_date);
        mTimeStartText = (TextView) findViewById(R.id.set_time_start);
        mTimeEndText = (TextView) findViewById(R.id.set_time_end);
        mClassAppText = (TextView) findViewById(R.id.set_class_icon);
        mClassDescriptionText = (TextView) findViewById(R.id.set_class_description);
        mInstructorText = (TextView) findViewById(R.id.set_instructor_name);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);

        // Setup Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Initialize default values
        mActive = "true";

        mCalendar = Calendar.getInstance();
        mHourStart = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinuteStart = mCalendar.get(Calendar.MINUTE);
        mHourEnd = mCalendar.get(Calendar.HOUR_OF_DAY) + 1;
        mMinuteEnd = mCalendar.get(Calendar.MINUTE);

        mDate = getResources().getStringArray(R.array.days)[0];
        mTimeStart = mHourStart + ":" + String.format("%02d", mMinuteStart);
        mTimeEnd = mHourEnd + ":" + String.format("%02d", mMinuteEnd);
        mClassApp = getResources().getStringArray(R.array.class_application)[0];
        mClassDescription = getResources().getString(R.string.class_description_placeholder);
        mInstructor = getResources().getString(R.string.instructor_name_placeholder);

        // Setup Reminder Title EditText
        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup TextViews using reminder values
        mDateText.setText(mDate);
        mTimeStartText.setText(mTimeStart);
        mTimeEndText.setText(mTimeEnd);
        mClassAppText.setText(mClassApp);
        mClassDescriptionText.setText(mClassDescription);
        mInstructorText.setText(mInstructor);

        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTimeStart = savedInstanceState.getString(KEY_TIME_START);
            mTimeStartText.setText(savedTimeStart);
            mTimeStart = savedTimeStart;

            String savedTimeEnd = savedInstanceState.getString(KEY_TIME_END);
            mTimeEndText.setText(savedTimeEnd);
            mTimeEnd = savedTimeEnd;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

//            String savedColor = savedInstanceState.getString(KEY_COLOR);
//            mColorPick.setText(savedColor);
//            mColor = savedColor;

            String savedClassName = savedInstanceState.getString(KEY_APPLICATION_TITLE);
            mClassAppText.setText(savedClassName);
            mClassApp = savedClassName;

            String savedClassDescription = savedInstanceState.getString(KEY_CLASS_DESCRIPTION);
            mClassDescriptionText.setText(savedClassDescription);
            mClassDescription = savedClassDescription;

            String savedInstructorName = savedInstanceState.getString(KEY_INSTRUCTOR_NAME);
            mInstructorText.setText(savedInstructorName);
            mInstructor = savedInstructorName;


            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }

        // Setup up active buttons
        if (mActive.equals("false")) {
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.GONE);

        } else if (mActive.equals("true")) {
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.VISIBLE);
        }
    }

    // To save state on device rotation
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME_START, mTimeStartText.getText());
        outState.putCharSequence(KEY_TIME_END, mTimeEndText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        // outState.putCharSequence(KEY_COLOR, mColorPick.getText());
        outState.putCharSequence(KEY_APPLICATION_TITLE, mClassAppText.getText());
        outState.putCharSequence(KEY_CLASS_DESCRIPTION, mClassDescriptionText.getText());
        outState.putCharSequence(KEY_INSTRUCTOR_NAME, mInstructorText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
    }

    // On clicking Time picker
    public void setTime(View v){
        int viewId = v.getId();
        if(viewId == R.id.time_start_tab) {
            selectedTimePicker = TIME_START;
        } else if(viewId == R.id.time_end_tab) {
            selectedTimePicker = TIME_END;
        }
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // Obtain time from time picker
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if(selectedTimePicker == TIME_START) {
            mHourStart = hourOfDay;
            mMinuteStart = minute;
            if (minute < 10) {
                mTimeStart = hourOfDay + ":" + "0" + minute;
            } else {
                mTimeStart = hourOfDay + ":" + minute;
            }
            mTimeStartText.setText(mTimeStart);
        } else if(selectedTimePicker == TIME_END) {
            mHourEnd = hourOfDay;
            mMinuteEnd = minute;
            if (minute < 10) {
                mTimeEnd = hourOfDay + ":" + "0" + minute;
            } else {
                mTimeEnd = hourOfDay + ":" + minute;
            }
            mTimeEndText.setText(mTimeEnd);
        }
    }

    // On clicking select day
    public void selectMultipleDay(View v) {
        final String[] items = getResources().getStringArray(R.array.days); // days : Sunday, Monday, ...
        final String[] currentSelected = mDate.split(DATE_DELIMITER);
        System.out.println("Current Selected: " + Arrays.toString(currentSelected));
        boolean[] checked = new boolean[items.length];
        for(String s: currentSelected) {
            for(int i=0; i<items.length; i++) {
                if(s.equals(items[i])) {
                    checked[i] = true;
                }
            }
        }

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_day_title);
        builder.setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                mDate = "";
                for(int j=0; j<items.length; j++) {
                    if(checked[j]) {
                        mDate += items[j] + DATE_DELIMITER;
                    }
                }
                if(mDate != null && mDate.length() > 0) { // handle if user not select any choice
                    // delete last delimiter
                    mDate = mDate.substring(0, mDate.length() - DATE_DELIMITER.length());
                }
                mDateText.setText(mDate);
                checked[i] = b;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setClassApplicationIcon(View v) {
        final String[] items = getResources().getStringArray(R.array.class_application);

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_class_application_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mClassApp = items[i];
                mClassAppText.setText(mClassApp);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setClassDescription(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        builder.setTitle(R.string.enter_your_description);

        builder.setView(edittext);

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mClassDescription = edittext.getText().toString();
                mClassDescriptionText.setText(mClassDescription);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setInstructorName(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        builder.setTitle(R.string.enter_your_instructor_name);

        builder.setView(edittext);

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mInstructor = edittext.getText().toString();
                mInstructorText.setText(mInstructor);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking the active button
    public void selectFab1(View v) {
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    // On clicking the inactive button
    public void selectFab2(View v) {
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }

    // On clicking the save button
    public void saveReminder(){
        ReminderDatabase rb = new ReminderDatabase(this);
        final String[] items = getResources().getStringArray(R.array.days); // days : Sunday, Monday, ...
        final String[] currentSelectedDay = mDate.split(DATE_DELIMITER);
        ArrayList<Integer> selectedDayInt = new ArrayList<>();
        for(String s: currentSelectedDay) {
            for(int i=0; i<items.length; i++) {
                if(s.equals(items[i])) {
                    selectedDayInt.add(i+1);
                }
            }
        }

        // if current language is THAI then change them to English before save to database
        if(items[0].equals("อาทิตย์")) {
            ThaiEngDateMap translator = new ThaiEngDateMap();
            HashMap<String, String> thaiToEn = translator.getThaiToEnglish();
            for(int i=0; i<currentSelectedDay.length; i++) {
                currentSelectedDay[i] = thaiToEn.get(currentSelectedDay[i]);
            }
        }

        // Creating Reminder
        int ID = rb.addReminder(
                new Reminder(mTitle, currentSelectedDay, mTimeStart, mTimeEnd, "#ff0000",
                        mClassApp, mClassDescription, mInstructor, mActive)
        );
        Calendar[] calendars = new Calendar[selectedDayInt.size()];

        for(int i=0; i<selectedDayInt.size(); i++) {
            calendars[i] = Calendar.getInstance();
            // Set up calender for creating the notification
            calendars[i].set(Calendar.DAY_OF_WEEK, selectedDayInt.get(i));
            calendars[i].set(Calendar.HOUR_OF_DAY, mHourStart);
            calendars[i].set(Calendar.MINUTE, mMinuteStart);
            calendars[i].set(Calendar.SECOND, 0);

            // Check we aren't setting it in the past which would trigger it to fire instantly
            if(calendars[i].getTimeInMillis() < System.currentTimeMillis()) {
                calendars[i].add(Calendar.DAY_OF_YEAR, 7);
            }
            Date date = calendars[i].getTime();
            System.out.println(new SimpleDateFormat("yyyy-M-dd hh:mm").format(date));
        }

        // Create a new notification
        if (mActive.equals("true")) {
            new AlarmReceiver().setRepeatAlarm(getApplicationContext(), calendars, ID, milWeek);
        }

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), R.string.saved,
                Toast.LENGTH_SHORT).show();

        onBackPressed();
    }

    // On pressing the back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Creating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    // On clicking menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // On clicking the back arrow
            // Discard any changes
            case android.R.id.home:
                onBackPressed();
                return true;

            // On clicking save reminder button
            // Update reminder
            case R.id.save_reminder:
                mTitleText.setText(mTitle);

                if (mTitleText.getText().toString().length() == 0)
                    mTitleText.setError(getResources().getText(R.string.title_blank_error));
                else if (mDateText.getText().toString().length() == 0)
                    mDateText.setError(getResources().getText(R.string.date_blank_error));
                else {
                    saveReminder();
                }
                return true;

            // On clicking discard reminder button
            // Discard any changes
            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.discarded),
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}