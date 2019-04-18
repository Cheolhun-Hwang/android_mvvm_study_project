package com.hooneys.mvvmproject.Rooms.Repositorys;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.hooneys.mvvmproject.Rooms.DAO.NoteDao;
import com.hooneys.mvvmproject.Rooms.DAO.NoteDatabase;
import com.hooneys.mvvmproject.Rooms.Entitys.Note;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    //이것이 가져오는 Repository!!
    //Api도 동일!!
    public NoteRepository(Application application){
        //Use Application Context. SingleTone Just One.
        NoteDatabase database = NoteDatabase.getInstance(application);
        this.noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note){
        new InsertNoteAsycTask(noteDao).execute(note);      //AsyncTask 실행
    }

    public void update(Note note){
        new UpdateNoteAsycTask(noteDao).execute(note);
    }

    public void delete(Note note){
        new DeleteNoteAsycTask(noteDao).execute(note);
    }

    public void deleteAllNotes(){
        new DeleteAllNoteAsycTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    private static class InsertNoteAsycTask extends AsyncTask<Note, Void, Void>{
        private NoteDao noteDao;
        private InsertNoteAsycTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {  //여러개의 배열이긴 하지만
            noteDao.insert(notes[0]);                   //이번에는 하나만 받기 때문에.
            return null;
        }
    }

    private static class UpdateNoteAsycTask extends AsyncTask<Note, Void, Void>{
        private NoteDao noteDao;
        private UpdateNoteAsycTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {  //여러개의 배열이긴 하지만
            noteDao.update(notes[0]);                   //이번에는 하나만 받기 때문에.
            return null;
        }
    }

    private static class DeleteNoteAsycTask extends AsyncTask<Note, Void, Void>{
        private NoteDao noteDao;
        private DeleteNoteAsycTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {  //여러개의 배열이긴 하지만
            noteDao.delete(notes[0]);                   //이번에는 하나만 받기 때문에.
            return null;
        }
    }

    private static class DeleteAllNoteAsycTask extends AsyncTask<Void, Void, Void>{
        private NoteDao noteDao;
        private DeleteAllNoteAsycTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {  //여러개의 배열이긴 하지만
            noteDao.deleteAllNotes();                   //이번에는 하나만 받기 때문에.
            return null;
        }
    }
}
