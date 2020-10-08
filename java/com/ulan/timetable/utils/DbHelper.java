package com.ulan.timetable.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ulan.timetable.model.Contact;
import com.ulan.timetable.model.Events;
import com.ulan.timetable.model.Note;
import com.ulan.timetable.model.Task;
import com.ulan.timetable.model.Week;

import java.util.ArrayList;

/**
 * Created by Ulan on 07.09.2018.
 */
public class DbHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 6;
    private static final String DB_NAME = "timetabledb";
    private static final String TIMETABLE = "timetable";
    private static final String WEEK_ID = "id";
    private static final String WEEK_SUBJECT = "subject";
    private static final String WEEK_FRAGMENT = "fragment";
    private static final String WEEK_CONTACT = "contact";
    private static final String WEEK_ROOM = "room";
    private static final String WEEK_FROM_TIME = "fromtime";
    private static final String WEEK_TO_TIME = "totime";
    private static final String WEEK_COLOR = "color";

    private static final String TASKS = "tasks";
    private static final String TASKS_ID = "id";
    private static final String TASKS_SUBJECT = "subject";
    private static final String TASKS_DESCRIPTION = "description";
    private static final String TASKS_DATE = "date";
    private static final String TASKS_COLOR = "color";

    private static final String NOTES = "notes";
    private static final String NOTES_ID = "id";
    private static final String NOTES_TITLE = "title";
    private static final String NOTES_TEXT = "text";
    private static final String NOTES_COLOR = "color";

    private static final String CONTACTS = "contacts";
    private static final String CONTACTS_ID = "id";
    private static final String CONTACTS_NAME = "name";
    private static final String CONTACTS_POST = "post";
    private static final String CONTACTS_PHONE_NUMBER = "phonenumber";
    private static final String CONTACTS_EMAIL = "email";
    private static final String CONTACTS_COLOR = "color";

    private static final String EVENTS = "events";
    private static final String EVENTS_ID = "id";
    private static final String EVENTS_SUBJECT = "subject";
    private static final String EVENTS_CONTACT = "contact";
    private static final String EVENTS_ROOM = "room";
    private static final String EVENTS_DATE = "date";
    private static final String EVENTS_TIME = "time";
    private static final String EVENTS_COLOR = "color";


    public DbHelper(Context context){
        super(context , DB_NAME, null, DB_VERSION);
    }

     public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMETABLE = "CREATE TABLE " + TIMETABLE + "("
                + WEEK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WEEK_SUBJECT + " TEXT,"
                + WEEK_FRAGMENT + " TEXT,"
                + WEEK_CONTACT + " TEXT,"
                + WEEK_ROOM + " TEXT,"
                + WEEK_FROM_TIME + " TEXT,"
                + WEEK_TO_TIME + " TEXT,"
                + WEEK_COLOR + " INTEGER" +  ")";

        String CREATE_HOMEWORKS = "CREATE TABLE " + TASKS + "("
                + TASKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASKS_SUBJECT + " TEXT,"
                + TASKS_DESCRIPTION + " TEXT,"
                + TASKS_DATE + " TEXT,"
                + TASKS_COLOR + " INTEGER" + ")";

        String CREATE_NOTES = "CREATE TABLE " + NOTES + "("
                + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTES_TITLE + " TEXT,"
                + NOTES_TEXT + " TEXT,"
                + NOTES_COLOR + " INTEGER" + ")";

        String CREATE_TEACHERS = "CREATE TABLE " + CONTACTS + "("
                + CONTACTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTACTS_NAME + " TEXT,"
                + CONTACTS_POST + " TEXT,"
                + CONTACTS_PHONE_NUMBER + " TEXT,"
                + CONTACTS_EMAIL + " TEXT,"
                + CONTACTS_COLOR + " INTEGER" + ")";

        String CREATE_EXAMS = "CREATE TABLE " + EVENTS + "("
                + EVENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EVENTS_SUBJECT + " TEXT,"
                + EVENTS_CONTACT + " TEXT,"
                + EVENTS_ROOM + " TEXT,"
                + EVENTS_DATE + " TEXT,"
                + EVENTS_TIME + " TEXT,"
                + EVENTS_COLOR + " INTEGER" + ")";

        db.execSQL(CREATE_TIMETABLE);
        db.execSQL(CREATE_HOMEWORKS);
        db.execSQL(CREATE_NOTES);
        db.execSQL(CREATE_TEACHERS);
        db.execSQL(CREATE_EXAMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE);

            case 2:
                db.execSQL("DROP TABLE IF EXISTS " + TASKS);

            case 3:
                db.execSQL("DROP TABLE IF EXISTS " + NOTES);

            case 4:
                db.execSQL("DROP TABLE IF EXISTS " + CONTACTS);

            case 5:
                db.execSQL("DROP TABLE IF EXISTS " + EVENTS);
                break;
        }
        onCreate(db);
    }

    /**
     * Methods for Week fragments
     **/
    public void insertWeek(Week week){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_SUBJECT, week.getSubject());
        contentValues.put(WEEK_FRAGMENT, week.getFragment());
        contentValues.put(WEEK_CONTACT, week.getTeacher());
        contentValues.put(WEEK_ROOM, week.getRoom());
        contentValues.put(WEEK_FROM_TIME, week.getFromTime());
        contentValues.put(WEEK_TO_TIME, week.getToTime());
        contentValues.put(WEEK_COLOR, week.getColor());
        db.insert(TIMETABLE,null, contentValues);
        db.update(TIMETABLE, contentValues, WEEK_FRAGMENT, null);
        db.close();
    }

    public void deleteWeekById(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TIMETABLE, WEEK_ID + " = ? ", new String[]{String.valueOf(week.getId())});
        db.close();
    }

    public void updateWeek(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_SUBJECT, week.getSubject());
        contentValues.put(WEEK_CONTACT, week.getTeacher());
        contentValues.put(WEEK_ROOM, week.getRoom());
        contentValues.put(WEEK_FROM_TIME,week.getFromTime());
        contentValues.put(WEEK_TO_TIME, week.getToTime());
        contentValues.put(WEEK_COLOR, week.getColor());
        db.update(TIMETABLE, contentValues, WEEK_ID + " = " + week.getId(), null);
        db.close();
    }

    public ArrayList<Week> getWeek(String fragment){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Week> weeklist = new ArrayList<>();
        Week week;
        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT * FROM "+TIMETABLE+" ORDER BY " + WEEK_FROM_TIME + " ) WHERE "+ WEEK_FRAGMENT +" LIKE '"+fragment+"%'",null);
        while (cursor.moveToNext()){
            week = new Week();
            week.setId(cursor.getInt(cursor.getColumnIndex(WEEK_ID)));
            week.setSubject(cursor.getString(cursor.getColumnIndex(WEEK_SUBJECT)));
            week.setTeacher(cursor.getString(cursor.getColumnIndex(WEEK_CONTACT)));
            week.setRoom(cursor.getString(cursor.getColumnIndex(WEEK_ROOM)));
            week.setFromTime(cursor.getString(cursor.getColumnIndex(WEEK_FROM_TIME)));
            week.setToTime(cursor.getString(cursor.getColumnIndex(WEEK_TO_TIME)));
            week.setColor(cursor.getInt(cursor.getColumnIndex(WEEK_COLOR)));
            weeklist.add(week);
        }
        return  weeklist;
    }

    /**
     * Methods for Tasks activity
     **/
    public void insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_SUBJECT, task.getSubject());
        contentValues.put(TASKS_DESCRIPTION, task.getDescription());
        contentValues.put(TASKS_DATE, task.getDate());
        contentValues.put(TASKS_COLOR, task.getColor());
        db.insert(TASKS,null, contentValues);
        db.close();
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_SUBJECT, task.getSubject());
        contentValues.put(TASKS_DESCRIPTION, task.getDescription());
        contentValues.put(TASKS_DATE, task.getDate());
        contentValues.put(TASKS_COLOR, task.getColor());
        db.update(TASKS, contentValues, TASKS_ID + " = " + task.getId(), null);
        db.close();
    }

    public void deleteTaskById(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASKS, TASKS_ID + " = ? ", new String[]{String.valueOf(task.getId())});
        db.close();
    }


    public ArrayList<Task> getTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Task> tasklist = new ArrayList<>();
        Task task;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TASKS + " ORDER BY datetime(" + TASKS_DATE + ") ASC",null);
        while (cursor.moveToNext()){
            task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndex(TASKS_ID)));
            task.setSubject(cursor.getString(cursor.getColumnIndex(TASKS_SUBJECT)));
            task.setDescription(cursor.getString(cursor.getColumnIndex(TASKS_DESCRIPTION)));
            task.setDate(cursor.getString(cursor.getColumnIndex(TASKS_DATE)));
            task.setColor(cursor.getInt(cursor.getColumnIndex(TASKS_COLOR)));
            tasklist.add(task);
        }
        cursor.close();
        db.close();
        return  tasklist;
    }

    /**
     * Methods for Notes activity
     **/
    public void insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_TITLE, note.getTitle());
        contentValues.put(NOTES_TEXT, note.getText());
        contentValues.put(NOTES_COLOR, note.getColor());
        db.insert(NOTES, null, contentValues);
        db.close();
    }

    public void updateNote(Note note)  {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_TITLE, note.getTitle());
        contentValues.put(NOTES_TEXT, note.getText());
        contentValues.put(NOTES_COLOR, note.getColor());
        db.update(NOTES, contentValues, NOTES_ID + " = " + note.getId(), null);
        db.close();
    }

    public void deleteNoteById(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTES, NOTES_ID + " =? ", new String[] {String.valueOf(note.getId())});
        db.close();
    }

    public ArrayList<Note> getNote() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Note> notelist = new ArrayList<>();
        Note note;
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTES, null);
        while (cursor.moveToNext()) {
            note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex(NOTES_ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(NOTES_TITLE)));
            note.setText(cursor.getString(cursor.getColumnIndex(NOTES_TEXT)));
            note.setColor(cursor.getInt(cursor.getColumnIndex(NOTES_COLOR)));
            notelist.add(note);
        }
        cursor.close();
        db.close();
        return notelist;
    }

    /**
     * Methods for Teachers activity
     **/
    public void insertContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_NAME, contact.getName());
        contentValues.put(CONTACTS_POST, contact.getPost());
        contentValues.put(CONTACTS_PHONE_NUMBER, contact.getPhonenumber());
        contentValues.put(CONTACTS_EMAIL, contact.getEmail());
        contentValues.put(CONTACTS_COLOR, contact.getColor());
        db.insert(CONTACTS, null, contentValues);
        db.close();
    }

    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_NAME, contact.getName());
        contentValues.put(CONTACTS_POST, contact.getPost());
        contentValues.put(CONTACTS_PHONE_NUMBER, contact.getPhonenumber());
        contentValues.put(CONTACTS_EMAIL, contact.getEmail());
        contentValues.put(CONTACTS_COLOR, contact.getColor());
        db.update(CONTACTS, contentValues, CONTACTS_ID + " = " + contact.getId(), null);
        db.close();
    }

    public void deleteContactById(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS, CONTACTS_ID + " =? ", new String[] {String.valueOf(contact.getId())});
        db.close();
    }

    public ArrayList<Contact> getContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Contact> contactlist = new ArrayList<>();
        Contact contact;
        Cursor cursor = db.rawQuery("SELECT * FROM " + CONTACTS, null);
        while (cursor.moveToNext()) {
            contact = new Contact();
            contact.setId(cursor.getInt(cursor.getColumnIndex(CONTACTS_ID)));
            contact.setName(cursor.getString(cursor.getColumnIndex(CONTACTS_NAME)));
            contact.setPost(cursor.getString(cursor.getColumnIndex(CONTACTS_POST)));
            contact.setPhonenumber(cursor.getString(cursor.getColumnIndex(CONTACTS_PHONE_NUMBER)));
            contact.setEmail(cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL)));
            contact.setColor(cursor.getInt(cursor.getColumnIndex(CONTACTS_COLOR)));
            contactlist.add(contact);
        }
        cursor.close();
        db.close();
        return contactlist;
    }

    /**
     * Methods for Events activity
     **/
    public void insertEvent(Events events) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_SUBJECT, events.getSubject());
        contentValues.put(EVENTS_CONTACT, events.getTeacher());
        contentValues.put(EVENTS_ROOM, events.getRoom());
        contentValues.put(EVENTS_DATE, events.getDate());
        contentValues.put(EVENTS_TIME, events.getTime());
        contentValues.put(EVENTS_COLOR, events.getColor());
        db.insert(EVENTS, null, contentValues);
        db.close();
    }

    public void updateEvent(Events events) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_SUBJECT, events.getSubject());
        contentValues.put(EVENTS_CONTACT, events.getTeacher());
        contentValues.put(EVENTS_ROOM, events.getRoom());
        contentValues.put(EVENTS_DATE, events.getDate());
        contentValues.put(EVENTS_TIME, events.getTime());
        contentValues.put(EVENTS_COLOR, events.getColor());
        db.update(EVENTS, contentValues, EVENTS_ID + " = " + events.getId(), null);
        db.close();
    }

    public void deleteExamById(Events events) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENTS, EVENTS_ID + " =? ", new String[] {String.valueOf(events.getId())});
        db.close();
    }

    public ArrayList<Events> getEvent() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Events> eventslist = new ArrayList<>();
        Events events;
        Cursor cursor = db.rawQuery("SELECT * FROM " + EVENTS, null);
        while (cursor.moveToNext()) {
            events = new Events();
            events.setId(cursor.getInt(cursor.getColumnIndex(EVENTS_ID)));
            events.setSubject(cursor.getString(cursor.getColumnIndex(EVENTS_SUBJECT)));
            events.setTeacher(cursor.getString(cursor.getColumnIndex(EVENTS_CONTACT)));
            events.setRoom(cursor.getString(cursor.getColumnIndex(EVENTS_ROOM)));
            events.setDate(cursor.getString(cursor.getColumnIndex(EVENTS_DATE)));
            events.setTime(cursor.getString(cursor.getColumnIndex(EVENTS_TIME)));
            events.setColor(cursor.getInt(cursor.getColumnIndex(EVENTS_COLOR)));

            eventslist.add(events);
        }
        cursor.close();
        db.close();
        return eventslist;
    }
}
