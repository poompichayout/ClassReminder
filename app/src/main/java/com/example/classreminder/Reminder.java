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

// Reminder class
public class Reminder {
    private int mID;
    private String mClassTitle;
    private String[] mDate;
    private String mTime;
    private String mColor;
    private String mApplicationTitle;
    private String mClassDescription;
    private String mInstructorName;
    private String mActive;


    public Reminder(int ID, String Title, String[] Date, String Time, String color, String applicationTitle, String classDescription, String instructorName, String Active){
        mID = ID;
        mClassTitle = Title;
        mDate = Date;
        mTime = Time;
        mInstructorName = instructorName;
        mColor = color;
        mApplicationTitle = applicationTitle;
        mClassDescription = classDescription;
        mActive = Active;
    }

    public Reminder(String Title, String[] Date, String Time, String color, String applicationTitle, String classDescription, String instructorName, String Active){
        mClassTitle = Title;
        mDate = Date;
        mTime = Time;
        mInstructorName = instructorName;
        mColor = color;
        mApplicationTitle = applicationTitle;
        mClassDescription = classDescription;
        mActive = Active;
    }

    public Reminder(){}

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mClassTitle;
    }

    public void setTitle(String title) {
        mClassTitle = title;
    }

    public String[] getDate() {
        return mDate;
    }

    public void setDate(String[] date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getApplicationTitle() {
        return mApplicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        mApplicationTitle = applicationTitle;
    }

    public String getInstructorName() {
        return mInstructorName;
    }

    public void setInstructorName(String instructorName) {
        mInstructorName = instructorName;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getClassDescription() {
        return mClassDescription;
    }

    public void setClassDescription(String classDescription) {
        mClassDescription = classDescription;
    }

    public String getActive() {
        return mActive;
    }

    public void setActive(String active) {
        mActive = active;
    }
}
