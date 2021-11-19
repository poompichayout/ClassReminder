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

import java.util.Calendar;


public class ReminderAddActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mDateText, mTimeStartText, mTimeEndText, mClassAppText, mClassDescriptionText, mInstructorText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mCalendar;
    private int mYear, mMonth, mHourStart, mMinuteStart, mDay;
    private int mHourEnd, mMinuteEnd;
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
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTimeStart = mHourStart + ":" + mMinuteStart;
        mTimeEnd = mHourEnd + ":" + mMinuteEnd;

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

    // On clicking select day
    public void selectMultipleDay(View v) {
        final String[] items = getResources().getStringArray(R.array.days);

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_day_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDate = items[i];
                mDateText.setText(mDate);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking Date picker
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    // Obtain time from time picker
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        view.getTag().toString();
        mHourStart = hourOfDay;
        mMinuteStart = minute;
        if (minute < 10) {
            mTimeStart = hourOfDay + ":" + "0" + minute;
        } else {
            mTimeStart = hourOfDay + ":" + minute;
        }
        mTimeStartText.setText(mTimeStart);
    }

    // Obtain date from date picker
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mDateText.setText(mDate);
    }

    public void setClassApplicationIcon(View v) {

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

        // Creating Reminder
        int ID = rb.addReminder(
                new Reminder(mTitle, new String[] { mDate }, mTimeStart, "#ff0000",
                        mClassApp, mClassDescription, mInstructor, mActive)
        );

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHourStart);
        mCalendar.set(Calendar.MINUTE, mMinuteStart);
        mCalendar.set(Calendar.SECOND, 0);

        // Create a new notification
        if (mActive.equals("true")) {
            // new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);
        }

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
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