package com.cookandroid.amgibbang;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class TodoFragment extends Fragment {
    private static final String TAG = "TodoFragment";

    RecyclerView recyclerView;
    NoteAdapter adapter;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_todo, container, false);

        initUI(rootView);

        loadNoteListData();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNoteListData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    public int loadNoteListData() {

        String loadSql = "select _id, TODO, checkBox from " + NoteDatabase.TABLE_NOTE + " order by _id desc";

        int recordCount = -1;
        NoteDatabase database = NoteDatabase.getInstance(context);

        if(database != null){
            Cursor outCursor = database.rawQuery(loadSql);

            recordCount = outCursor.getCount();

            ArrayList<Note> items = new ArrayList<>();

            for(int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String todo = outCursor.getString(1);
                int checkBox = outCursor.getInt(2);
                items.add(new Note(_id,todo,checkBox));
            }

            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

        return recordCount;
    }


    private void initUI(ViewGroup rootView){
        recyclerView = rootView.findViewById(R.id.recyclerView_todo);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
    }
}
