package com.hooneys.mvvmproject.ListPack.Note;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hooneys.mvvmproject.R;
import com.hooneys.mvvmproject.Rooms.Entitys.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    //RecyclerAdapter =상속=> ListAdapter (For Animation!)
    //부모가 List 를 가지고 있음! 여기에는 정의 안함.
    private OnItemClickListener listener;

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note note, @NonNull Note newItem) {
            //말 그대로 아이템이 같은가.
            return note.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note note, @NonNull Note newItem) {
            //중요 정보가 같은가.
            return note.getTitle().equals(newItem.getTitle()) &&
                    note.getDescription().equals(newItem.getDescription()) &&
                    note.getPriority() == note.getPriority();
        }
    };

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_note, viewGroup, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int position) {
        //List 정보는 부모에게 있기 때문에 부모의 Get Item을 사용함.
        Note item = getItem(position);
        noteHolder.title.setText(item.getTitle());
        noteHolder.desription.setText(item.getDescription());
        noteHolder.priority.setText(String.valueOf(item.getPriority()));
    }

    //이부분도 부모가 대신해줌
//    @Override
//    public int getItemCount() {
//        return notes.size();
//    }
//    public void setNotes(List<Note> notes){
//        this.notes = notes;
//        notifyDataSetChanged();
//    }

    public Note getNoteAt(int position){
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        private TextView title, desription, priority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_view_title);
            desription = (TextView) itemView.findViewById(R.id.text_view_description);
            priority = (TextView) itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();  // 선택한 아이템의 위치를 알 수 있음
                    if(listener != null &&
                            position != RecyclerView.NO_POSITION) {//삭제 이후에 0 이하의 위치일 때
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
