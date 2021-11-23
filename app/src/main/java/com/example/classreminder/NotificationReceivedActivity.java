package com.example.classreminder;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class NotificationReceivedActivity extends AppCompatActivity {

    private ReminderDatabase rb;
    private Reminder mReceivedReminder;
    private int mReceivedID;

    private Toolbar mToolbar;
    private ImageView mThumbnailImage;
    private TextView mTitleText, mDateAndTimeText, mClassApp, mClassLink;

    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_notification);

        // Initialize views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (TextView) findViewById(R.id.class_title);
        mDateAndTimeText = (TextView) findViewById(R.id.class_time);
        mClassApp = (TextView) findViewById(R.id.class_app);
        mClassLink = (TextView) findViewById(R.id.class_link);
        mThumbnailImage = (ImageView) findViewById(R.id.thumbnail_image_2);

        // Setup toolbar
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Get reminder id from intent
        mReceivedID = Integer.parseInt(getIntent().getStringExtra(ReminderEditActivity.EXTRA_REMINDER_ID));

        // Get reminder using reminder id
        rb = new ReminderDatabase(this);
        mReceivedReminder = rb.getReminder(mReceivedID);

        setReminderTitle(mReceivedReminder.getTitle());

        String dateAndTime = "Every " + TextUtils.join(", ", mReceivedReminder.getDate())
        + " " +  mReceivedReminder.getTimeStart() + " - " + mReceivedReminder.getTimeEnd();
        mDateAndTimeText.setText(dateAndTime);
        mClassApp.setText(mReceivedReminder.getApplicationTitle());
        mClassLink.setText(mReceivedReminder.getClassDescription());
    }

    public void setReminderTitle(String title) {
        mTitleText.setText(title);
        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();

        // Create a circular icon consisting of  a random background colour and first letter of title
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // On clicking the back arrow
            // Discard any changes
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
