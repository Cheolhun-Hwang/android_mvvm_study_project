package com.hooneys.mvvmproject;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hooneys.mvvmproject.ListPack.Note.NoteAdapter;
import com.hooneys.mvvmproject.Rooms.Entitys.Note;
import com.hooneys.mvvmproject.ViewModels.NoteViewModels;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static int SIG_ADD = 701;
    public final static int SIG_UPDATE = 702;
    private NoteViewModels noteViewModels;
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(noteAdapter);

        //Fragments 에서 자신에게 초기화 하면 프레그먼트 삭제시 포커스 등 사라짐.
        //이때, GetActivity를 통해서 하단의 Activity에게 포커스를 줄 수 있음
        noteViewModels = ViewModelProviders.of(MainActivity.this).get(NoteViewModels.class);
        noteViewModels.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
//                noteAdapter.setNotes(notes);    //바뀌면 지속적으로 바뀜!! 그래서 따로둠...
                //ListAdapter 변경으로 인해 부모가 리스트 정보를 가지고 있음
                //이벤트는 추가, 제거로 인하여 위치 변경 하는거 추가됨!!
                noteAdapter.submitList(notes);
            }
        });

        addButton = findViewById(R.id.button_add_note);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, SIG_ADD);
            }
        });

        //SWIPE EVENT RECYCLER VIEW!
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                noteViewModels.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note delete...", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                //Shift+F6 Activity 명 변경
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());    //Update 를 위해
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, SIG_UPDATE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModels.deleteAllNotes();
                Toast.makeText(this, "All notes deleted...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIG_ADD && resultCode == RESULT_OK) {
            String s_title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String s_desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int prio = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(s_title, s_desc, prio);
            noteViewModels.insert(note);

            Toast.makeText(getApplicationContext(),
                    "Note saved...",
                    Toast.LENGTH_SHORT).show();
        }if (requestCode == SIG_UPDATE && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(this, "Note can't be updated..", Toast.LENGTH_SHORT).show();
                return;
            }
            String s_title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String s_desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int prio = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(s_title, s_desc, prio);
            note.setId(id);
            noteViewModels.update(note);
            //Room 에서 Primary 가 같으면 바로 Update함 .. 별도 처리 안해도 됨.
            Toast.makeText(getApplicationContext(),
                    "Note updated...",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
