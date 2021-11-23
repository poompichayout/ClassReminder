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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BootReceiver extends BroadcastReceiver {

    private String mTime;
    private String mActive;
    private String[] mTimeSplit;
    private int mHourStart, mMinuteStart, mReceivedID;

    private AlarmReceiver mAlarmReceiver;

    // Constant values in milliseconds
    private static final long milWeek = 604800000L;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            final String[] items = new String[] {
                    "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
            };


            ReminderDatabase rb = new ReminderDatabase(context);
            mAlarmReceiver = new AlarmReceiver();

            List<Reminder> reminders = rb.getAllReminders();

            for (Reminder rm : reminders) {
                mReceivedID = rm.getID();
                mActive = rm.getActive();
                mTime = rm.getTimeStart();

                mTimeSplit = mTime.split(":");

                final String[] currentSelectedDay = rm.getDate();
                ArrayList<Integer> selectedDayInt = new ArrayList<>();
                for(String s: currentSelectedDay) {
                    for(int i=0; i<items.length; i++) {
                        if(s.equals(items[i])) {
                            selectedDayInt.add(i+1);
                        }
                    }
                }

                mHourStart = Integer.parseInt(mTimeSplit[0]);
                mMinuteStart = Integer.parseInt(mTimeSplit[1]);

                // Cancel existing notification of the reminder by using its ID
                mAlarmReceiver.cancelAlarm(context, mReceivedID);

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
                }

                // Create a new notification
                if (mActive.equals("true")) {
                    new AlarmReceiver().setRepeatAlarm(context, calendars, mReceivedID, milWeek);
                }
            }
        }
    }
}