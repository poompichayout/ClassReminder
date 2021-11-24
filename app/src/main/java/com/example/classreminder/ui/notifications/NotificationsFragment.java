package com.example.classreminder.ui.notifications;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.example.classreminder.DateTimeSorter;
import com.example.classreminder.R;
import com.example.classreminder.Reminder;
import com.example.classreminder.ReminderDatabase;
import com.example.classreminder.ThaiEngDateMap;
import com.example.classreminder.ui.home.HomeFragment;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class NotificationsFragment extends Fragment {

    private TodayReminderAdapter mAdapter;
    private NotificationsViewModel notificationsViewModel;
    private ReminderDatabase rb;
    private Toolbar mToolbar;
    private TextView noReminderText;
    private RecyclerView mList;

    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private MultiSelector mMultiSelector = new MultiSelector();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize reminder database
        rb = new ReminderDatabase(getActivity().getApplicationContext());

        // initialize views
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mList = (RecyclerView) view.findViewById(R.id.today_reminder_list);

        // Setup toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);

        noReminderText = (TextView) view.findViewById(R.id.no_reminder_today);

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Calendar calendar = Calendar.getInstance();
        List<Reminder> tempList = rb.getAllRemindersOnDate(calendar);

        if(tempList.isEmpty()) {
            noReminderText.setVisibility(View.VISIBLE);
        }

        // Create recycler view
        mList.setLayoutManager(getLayoutManager());
        registerForContextMenu(mList);
        mAdapter = new TodayReminderAdapter();
        mAdapter.setItemCount(getDefaultItemCount(), calendar);
        mList.setAdapter(mAdapter);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                mAdapter.setItemCount(getDefaultItemCount(), date);
                mList.setAdapter(mAdapter);
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Layout manager for recycler view
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    protected int getDefaultItemCount() {
        return 100;
    }

    // Adapter class for recycler view
    public class TodayReminderAdapter extends RecyclerView.Adapter<TodayReminderAdapter.VerticalItemHolder> {
        private ArrayList<ReminderItem> mItems;

        public TodayReminderAdapter() {
            mItems = new ArrayList<>();
        }

        public void setItemCount(int count, Calendar calendar) {
            mItems.clear();
            mItems.addAll(generateData(count, calendar));
            notifyDataSetChanged();
        }

        public void removeItemSelected(int selected) {
            if (mItems.isEmpty()) return;
            mItems.remove(selected);
            notifyItemRemoved(selected);
        }

        // View holder for recycler view items
        @Override
        public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View root = inflater.inflate(R.layout.recycle_items, container, false);

            return new VerticalItemHolder(root, this);
        }

        @Override
        public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
            ReminderItem item = mItems.get(position);
            itemHolder.setReminderTitle(item.mTitle);
            itemHolder.setReminderDateTime(item.mDateTime);
            itemHolder.setActiveImage(item.mActive);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        // Class for recycler view items
        public  class ReminderItem {
            public String mTitle;
            public String mDateTime;
            public String mActive;

            public ReminderItem(String Title, String DateTime, String Active) {
                this.mTitle = Title;
                this.mDateTime = DateTime;
                this.mActive = Active;
            }
        }

        // Class to compare date and time so that items are sorted in ascending order
        public class DateTimeComparator implements Comparator {
            DateFormat f = new SimpleDateFormat("hh:mm");

            public int compare(Object a, Object b) {
                String o1 = ((DateTimeSorter)a).getDateTime();
                String o2 = ((DateTimeSorter)b).getDateTime();

                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        // UI and data class for recycler view items
        public class VerticalItemHolder extends SwappingHolder
                implements View.OnClickListener, View.OnLongClickListener {
            private TextView mTitleText, mDateAndTimeText;
            private ImageView mActiveImage , mThumbnailImage;
            private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
            private TextDrawable mDrawableBuilder;
            private TodayReminderAdapter mAdapter;

            public VerticalItemHolder(View itemView, TodayReminderAdapter adapter) {
                super(itemView, mMultiSelector);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);

                // Initialize adapter for the items
                mAdapter = adapter;

                // Initialize views
                mTitleText = (TextView) itemView.findViewById(R.id.recycle_title);
                mDateAndTimeText = (TextView) itemView.findViewById(R.id.recycle_date_time);
                mActiveImage = (ImageView) itemView.findViewById(R.id.active_image);
                mThumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            }

            // On clicking a reminder item
            @Override
            public void onClick(View v) {

            }

            // On long press enter action mode with context menu
            @Override
            public boolean onLongClick(View v) {
                return true;
            }

            // Set reminder title view
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

            // Set date and time views
            public void setReminderDateTime(String datetime) {
                mDateAndTimeText.setText(datetime);
            }

            // Set active image as on or off
            public void setActiveImage(String active){
                if(active.equals("true")){
                    mActiveImage.setImageResource(R.drawable.ic_notifications_on_white_24dp);
                }else if (active.equals("false")) {
                    mActiveImage.setImageResource(R.drawable.ic_notifications_off_grey600_24dp);
                }
            }
        }

        // Generate real data for each item
        public List<ReminderItem> generateData(int count, Calendar calendar) {
            ArrayList<ReminderItem> items = new ArrayList<>();
            ThaiEngDateMap translator = new ThaiEngDateMap();
            HashMap<String, String> mapDay;

            if(getResources().getString(R.string.date).equals("วัน")) {
                mapDay = translator.getThaiToAbbreviation();
            } else {
                mapDay = translator.getEnglishToAbbreviation();
            }

            // Get all reminders from the database
            List<Reminder> reminders = rb.getAllRemindersOnDate(calendar);
            if(reminders.isEmpty()) {
                noReminderText.setVisibility(View.VISIBLE);
            } else {
                noReminderText.setVisibility(View.GONE);
            }

            // Initialize lists
            List<String> Titles = new ArrayList<>();
            List<String> Actives = new ArrayList<>();
            List<String> DateAndTime = new ArrayList<>();
            List<Integer> IDList = new ArrayList<>();
            List<DateTimeSorter> DateTimeSortList = new ArrayList<>();

            // Add details of all reminders in their respective lists
            for (Reminder r : reminders) {
                Titles.add(r.getTitle());
                String[] dates = r.getDate();
                for(int i=0; i<dates.length; i++) {
                    dates[i] = mapDay.get(dates[i]);
                }
                DateAndTime.add(r.getTimeStart() + " - " + r.getTimeEnd() + " " + TextUtils.join(", ", dates));
                Actives.add(r.getActive());
                IDList.add(r.getID());
            }

            int key = 0;

            // Add date and time as DateTimeSorter objects
            for(int k = 0; k<Titles.size(); k++){
                DateTimeSortList.add(new DateTimeSorter(key, DateAndTime.get(k).split(" ")[0]));
                key++;
            }

            // Sort items according to date and time in ascending order
            Collections.sort(DateTimeSortList, new DateTimeComparator());

            int k = 0;

            // Add data to each recycler view item
            for (DateTimeSorter item:DateTimeSortList) {
                int i = item.getIndex();

                items.add(new ReminderItem(Titles.get(i), DateAndTime.get(i), Actives.get(i)));
                IDmap.put(k, IDList.get(i));
                k++;
            }
            return items;
        }
    }
}