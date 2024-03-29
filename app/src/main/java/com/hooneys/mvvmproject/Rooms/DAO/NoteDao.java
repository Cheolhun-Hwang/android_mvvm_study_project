package com.hooneys.mvvmproject.Rooms.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hooneys.mvvmproject.Rooms.Entitys.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note_table ORDER BY prior DESC")
    LiveData<List<Note>> getAllNotes();
    //LiveData Change Listen And Change Automatically.

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();
}
