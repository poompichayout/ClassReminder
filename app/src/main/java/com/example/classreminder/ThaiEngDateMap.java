package com.example.classreminder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ThaiEngDateMap {
    private HashMap<String, String> englishToThai;
    private HashMap<String, String> englishToAbbreviation;
    private HashMap<String, String> thaiToEnglish;
    private HashMap<String, String> thaiToAbbreviation;
    private List<String> englishDays = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    private List<String> thaiDays = Arrays.asList("อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์");

    public ThaiEngDateMap() {
        initEnglishToThaiHashMap();
        initThaiToEnglishHashMap();
        initEnglishToAbbreviation();
        initThaiToAbbreviationHashMap();
    }

    private void initEnglishToThaiHashMap() {
        englishToThai = new HashMap<>();
        englishToThai.put("Sunday", "อาทิตย์");
        englishToThai.put("Monday", "จันทร์");
        englishToThai.put("Tuesday", "อังคาร");
        englishToThai.put("Wednesday", "พุธ");
        englishToThai.put("Thursday", "พฤหัสบดี");
        englishToThai.put("Friday", "ศุกร์");
        englishToThai.put("Saturday", "เสาร์");
    }

    private void initThaiToEnglishHashMap() {
        thaiToEnglish = new HashMap<>();
        thaiToEnglish.put("อาทิตย์", "Sunday");
        thaiToEnglish.put("จันทร์", "Monday");
        thaiToEnglish.put("อังคาร", "Tuesday");
        thaiToEnglish.put("พุธ", "Wednesday");
        thaiToEnglish.put("พฤหัสบดี", "Thursday");
        thaiToEnglish.put("ศุกร์", "Friday");
        thaiToEnglish.put("เสาร์", "Saturday");
    }

    private void initEnglishToAbbreviation() {
        englishToAbbreviation = new HashMap<>();
        englishToAbbreviation.put("Sunday", "Sun");
        englishToAbbreviation.put("Monday", "Mon");
        englishToAbbreviation.put("Tuesday", "Tue");
        englishToAbbreviation.put("Wednesday", "Wed");
        englishToAbbreviation.put("Thursday", "Thu");
        englishToAbbreviation.put("Friday", "Fri");
        englishToAbbreviation.put("Saturday", "Sat");
        englishToAbbreviation.put("อาทิตย์", "Sun");
        englishToAbbreviation.put("จันทร์", "Mon");
        englishToAbbreviation.put("อังคาร", "Tue");
        englishToAbbreviation.put("พุธ", "Wed");
        englishToAbbreviation.put("พฤหัสบดี", "Thu");
        englishToAbbreviation.put("ศุกร์", "Fri");
        englishToAbbreviation.put("เสาร์", "Sat");
    }

    private void initThaiToAbbreviationHashMap() {
        thaiToAbbreviation = new HashMap<>();
        thaiToAbbreviation.put("อาทิตย์", "อา");
        thaiToAbbreviation.put("จันทร์", "จ");
        thaiToAbbreviation.put("อังคาร", "อ");
        thaiToAbbreviation.put("พุธ", "พ");
        thaiToAbbreviation.put("พฤหัสบดี", "พฤ");
        thaiToAbbreviation.put("ศุกร์", "ศ");
        thaiToAbbreviation.put("เสาร์", "ส");
        thaiToAbbreviation.put("Sunday", "อา");
        thaiToAbbreviation.put("Monday", "จ");
        thaiToAbbreviation.put("Tuesday", "อ");
        thaiToAbbreviation.put("Wednesday", "พ");
        thaiToAbbreviation.put("Thursday", "พฤ");
        thaiToAbbreviation.put("Friday", "ศ");
        thaiToAbbreviation.put("Saturday", "ส");
    }

    public HashMap<String, String> getEnglishToThai() {
        return englishToThai;
    }

    public HashMap<String, String> getEnglishToAbbreviation() {
        return englishToAbbreviation;
    }

    public HashMap<String, String> getThaiToAbbreviation() {
        return thaiToAbbreviation;
    }

    public HashMap<String, String> getThaiToEnglish() {
        return thaiToEnglish;
    }

    public List<String> getEnglishDays() {
        return englishDays;
    }

    public List<String> getThaiDays() {
        return thaiDays;
    }
}
