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


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import java.util.Calendar;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {
        String extra = intent.getStringExtra(ReminderEditActivity.EXTRA_REMINDER_ID);
        int mReceivedID = Integer.parseInt(extra.substring(0, extra.length() - 4));

        // Get notification title from Reminder Database
        ReminderDatabase rb = new ReminderDatabase(context);
        Reminder reminder = rb.getReminder(mReceivedID);
        String mTitle = reminder.getTitle();
        String mTimeStart = reminder.getTimeStart();
        String mTimeEnd = reminder.getTimeEnd();
        String mClassApp = reminder.getApplicationTitle();
        String mClassDescription = reminder.getClassDescription();

        createNotificationChannel(context, mTitle);

        // Create intent to open ReminderEditActivity on notification click
        Intent editIntent = new Intent(context, NotificationReceivedActivity.class);
        editIntent.putExtra(ReminderEditActivity.EXTRA_REMINDER_ID, Integer.toString(mReceivedID));
        PendingIntent mClick = PendingIntent.getActivity(context, Integer.parseInt(extra), editIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
                .setContentTitle(context.getResources().getString(R.string.app_name) + ": " + mTitle)
                .setContentText(mTimeStart + " - " + mTimeEnd + ": " + mClassApp + " - " + mClassDescription)
                .setTicker(mTitle)
                .setShowWhen(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(mClick)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setChannelId(CHANNEL_ID);
        nManager.notify(mReceivedID, mBuilder.build());
    }

    public void setRepeatAlarm(Context context, Calendar[] calendars, int ID, long RepeatTime) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Calculate notification time in
        Calendar c = Calendar.getInstance();
        for(int i=0; i<calendars.length; i++) {
            long currentTime = c.getTimeInMillis();
            long diffTime = calendars[i].getTimeInMillis() - currentTime;

            // Put Reminder ID in Intent Extra
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(ReminderEditActivity.EXTRA_REMINDER_ID, ID + "000" + i);
            mPendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(ID + "000" + i), intent, PendingIntent.FLAG_CANCEL_CURRENT);

            // Start alarm using initial notification time and repeat interval time
            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + diffTime,
                    RepeatTime , mPendingIntent);
        }

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context, int ID, int dayLength) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel All Alarm for multiple days
        for(int i=0; i<dayLength; i++) {
            // Cancel Alarm using Reminder ID
            mPendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(ID + "000" + i), new Intent(context, AlarmReceiver.class), 0);
            mAlarmManager.cancel(mPendingIntent);
        }


        // Disable alarm
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void createNotificationChannel(Context context, String description) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}