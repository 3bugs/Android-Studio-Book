package com.example.simplenote3;

import static com.example.simplenote3.Constants._ID;
import static com.example.simplenote3.Constants.AUTHORITY;
import static com.example.simplenote3.Constants.URI_NOTES;
import static com.example.simplenote3.Constants.TABLE_NAME;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class NotesProvider extends ContentProvider {
    private static final int NOTES = 1;
    private static final int NOTES_WITH_ID = 2;

    private NotesHelper helper;     // Helper object สำหรับทำงานกับฐานข้อมูล
    private UriMatcher uriMatcher;  // ออบเจ็คสำหรับเปรียบเทียบ (match) URI

    @Override
    public boolean onCreate() {
        // สร้าง UriMatcher แล้วเพิ่มรูปแบบ (pattern) ของ URI ที่เราสนใจเข้าไป
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "notes", NOTES);
        uriMatcher.addURI(AUTHORITY, "notes/#", NOTES_WITH_ID);
        // สร้าง Helper object
        helper = new NotesHelper(getContext());

        return true;
    }

    /****************************************************************
     * เมธอดสำหรับคิวรีฐานข้อมูล เพื่ออ่าน note ทั้งหมดหรือ note หนึ่งๆมาใช้ในแอพ
     ****************************************************************/
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String orderBy) {
        String whereClause = "";

        // ถ้าเป็น URI ที่ระบุถึง note หนึ่งๆ ให้เพิ่มเงื่อนไข (คำสั่ง WHERE ของภาษา SQL) ลงในคิวรี
        if (uriMatcher.match(uri) == NOTES_WITH_ID) {
            long id = Long.parseLong(uri.getPathSegments().get(1));
            whereClause = " WHERE _id = " + id + " ";
        }

        // ติดต่อฐานข้อมูลและรันคิวรี
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, datetime(time/1000, 'unixepoch', 'localtime') as time, content FROM "
                + TABLE_NAME + whereClause + " ORDER BY " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);

        // บอกให้ cursor คอยเฝ้าดูการเปลี่ยนแปลงข้อมูลที่ URI นั้น
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /****************************************************************
     * เมธอดสำหรับเพิ่ม note ใหม่ลงฐานข้อมูล
     ****************************************************************/
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();

        // ตรวจสอบรูปแบบของ URI ว่าถูกต้องหรือไม่
        if (uriMatcher.match(uri) != NOTES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // เพิ่มข้อมูลลงฐานข้อมูล
        long id = db.insertOrThrow(TABLE_NAME, null, values);

        // แจ้งออบเจ็คที่คอยเผ้าดูว่ามีการเปลี่ยนแปลงเกิดขึ้นที่ URI นั้น
        Uri newUri = ContentUris.withAppendedId(URI_NOTES, id);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    /****************************************************************
     * เมธอดสำหรับลบ note ในฐานข้อมูล ซึ่งไม่ได้ใช้
     * เพราะในแอพนี้เราไม่ได้ออกแบบให้ผู้ใช้สามารถลบหรือแก้ไข note ได้
     ****************************************************************/
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES_WITH_ID:
                long id = Long.parseLong(uri.getPathSegments().get(1));
                count = db.delete(TABLE_NAME, appendRowId(selection, id),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // แจ้งออบเจ็คที่คอยเผ้าดูว่ามีการเปลี่ยนแปลงเกิดขึ้นที่ URI นั้น
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /****************************************************************
     * เมธอดสำหรับแก้ไข note ในฐานข้อมูล ซึ่งไม่ได้ใช้
     * เพราะในแอพนี้เราไม่ได้ออกแบบให้ผู้ใช้สามารถลบหรือแก้ไข note ได้
     ****************************************************************/
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES_WITH_ID:
                long id = Long.parseLong(uri.getPathSegments().get(1));
                count = db.update(TABLE_NAME, values, appendRowId(selection, id),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // แจ้งออบเจ็คที่คอยเผ้าดูว่ามีการเปลี่ยนแปลงเกิดขึ้นที่ URI นั้น
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /****************************************************************
     * เมธอดสำหรับเพิ่มเงื่อนไขลงในคิวรีเพื่อระบุถึง note หนึ่งๆ ในกรณีลบหรือแก้ไข note
     * (เมธอดนี้ จะถูกเรียกจากเมธอด delete และ update อีกที)
     ****************************************************************/
    private String appendRowId(String selection, long id) {
        return _ID + "=" + id
                + (!TextUtils.isEmpty(selection)
                ? " AND (" + selection + ')'
                : "");
    }

    // MIME type ของ note ทั้งหมด
    private static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/vnd.example.simplenote";
    // MIME type ของ note หนึ่งๆ
    private static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/vnd.example.simplenote";

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case NOTES:
                return CONTENT_TYPE;
            case NOTES_WITH_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
