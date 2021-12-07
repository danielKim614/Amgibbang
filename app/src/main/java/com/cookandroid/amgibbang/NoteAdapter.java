package com.cookandroid.amgibbang;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    ArrayList<Note> items = new ArrayList<Note>();



    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.todo_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note item = items.get(position);
        int state = item.getCheckBox();
        holder.setItem(item);
        holder.setLayout();
        if(state == 1)
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layoutTodo;
        CheckBox checkBox;
        Button deleteButton;

        public ViewHolder(View itemView){
            super(itemView);
            this.layoutTodo = itemView.findViewById(R.id.layoutTodo);
            this.checkBox = itemView.findViewById(R.id.todoCheckBox);
            this.deleteButton = itemView.findViewById(R.id.todoDeleteButton);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int state;

                    if (checkBox.isChecked()) {
                        state = 1;
                    }
                    else {
                        state = 0;
                    }
                    String TODO = (String) checkBox.getText();
                    saveCheckBox(TODO, state);

                }

                Context context;

                private void saveCheckBox(String TODO, int state){
                    String checkBoxSql;
                    if (state == 1) {
                        checkBoxSql = "update " + NoteDatabase.TABLE_NOTE + " set " + "  checkBox = '1'" + " where " + "  TODO = '" + TODO + "'";
                    }
                    else {
                        checkBoxSql = "update " + NoteDatabase.TABLE_NOTE + " set " + "  checkBox = '0'" + " where " + "  TODO = '" + TODO + "'";
                    }
                    Log.v("checkBox",""+state);
                    NoteDatabase database = NoteDatabase.getInstance(context);
                    database.execSQL(checkBoxSql);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String TODO = (String) checkBox.getText();
                    deleteToDo(TODO);
                    Toast.makeText(v.getContext(),"삭제 되었습니다.",Toast.LENGTH_SHORT).show();
                }

                Context context;

                private void deleteToDo(String TODO){
                    String deleteSql = "delete from " + NoteDatabase.TABLE_NOTE + " where " + "  TODO = '" + TODO+"'";
                    NoteDatabase database = NoteDatabase.getInstance(context);
                    database.execSQL(deleteSql);
                }
            });
        }
        public void setItem(Note item){checkBox.setText(item.getTodo());}

        public void setLayout(){layoutTodo.setVisibility(View.VISIBLE);}
    }

    public void setItems(ArrayList<Note> items){this.items = items;}
}