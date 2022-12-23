package com.example.android_baitap2;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcl;
    private NoteAdapter mAdapter;
    private ArrayList<Note> noteList;
    public static final String fileName= Environment.getDataDirectory().getPath()+"/data/com.example.android_baitap2/MyNotes.txt";
    public static final String NOTE_KEY="NOTE_KEY";
    public static final String POSITION_KEY="POSITION_KEY";
    public static final int EDIT_NOTE_REQUEST=100;
    public static final int CREATE_NOTE_REQUEST=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpComponents();

    }

    public void setUpComponents()
    {
        rcl=findViewById(R.id.rclMain);
        noteList=new ArrayList<>();

        ArrayList<Note> temp=lastSavedNotes();
        if(temp!=null&&temp.size()!=0)
        {
            noteList=temp;
        }
        mAdapter=new NoteAdapter(noteList, new INoteClickListener() {
            @Override
            public void onClickHandle(Note note, int position) {
                noteClickHandle(note,position);
            }

            @Override
            public void OnLongClickHandle(int lineIndex) {
                noteLongClickHandle(lineIndex);
            }
        });

        rcl.setLayoutManager(new LinearLayoutManager(this,rcl.VERTICAL,false));
        rcl.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                alertDialog.setTitle("More Information")
                        .setMessage("Do you want to know more about us?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("google.com"));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create().show();
                return true;

            case R.id.add:
                Intent intent=new Intent(this,AddNote_Activity.class);
                startActivityForResult(intent,CREATE_NOTE_REQUEST);
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + item);
        }
    }

    public ArrayList<Note> lastSavedNotes()
    {
        ArrayList<Note> temp=new ArrayList<>();
        try {

            File file=new File(fileName);
            if(!file.exists())
            {
                Toast.makeText(this, "No directory", Toast.LENGTH_SHORT).show();
                return temp;
            }

            FileInputStream fis=new FileInputStream(fileName);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            while (line != null) {
                String[] data=line.split(",");
                Log.e("Loi", String.valueOf(data.length));
                Note note=new Note(data[0],data[1],Long.parseLong(data[2]));
                temp.add(note);
                line = br.readLine();
            }
            fis.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EDIT_NOTE_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(this,"Edit Note Success",Toast.LENGTH_SHORT).show();
                Bundle bundle=data.getExtras();
                Note note= (Note) bundle.getSerializable(NOTE_KEY);
                noteList.set(bundle.getInt(MainActivity.POSITION_KEY),note);
                mAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==CREATE_NOTE_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(this, "Create Note Success", Toast.LENGTH_SHORT).show();
                Bundle bundle=data.getExtras();
                noteList.add((Note) bundle.getSerializable(NOTE_KEY));
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    public void noteClickHandle(Note note,int position)
    {
        Intent intent=new Intent(this,AddNote_Activity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(NOTE_KEY,note);
        bundle.putInt(POSITION_KEY,position);
        intent.putExtras(bundle);
        startActivityForResult(intent,EDIT_NOTE_REQUEST);
    }

    public void noteLongClickHandle(int position)
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("Delete")
                .setMessage("Do you want to delete this Note?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNote(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create().show();
    }

    public void deleteNote(int lineDeleteIndex)
    {
        int lineIndex=0;
        String allLine="";
        try{

            FileInputStream fis=new FileInputStream(fileName);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));

            String line = br.readLine();
            while (line != null) {
                if(lineIndex!=lineDeleteIndex)
                {
                    allLine=allLine+line+"\n";
                }
                lineIndex++;
                line=br.readLine();
            }
            File file=new File(fileName);
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(allLine.getBytes(StandardCharsets.UTF_8));
            fis.close();
            br.close();
            fos.close();
            noteList.remove(lineDeleteIndex);
            mAdapter.notifyDataSetChanged();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}