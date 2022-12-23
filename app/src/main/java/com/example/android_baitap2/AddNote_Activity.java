package com.example.android_baitap2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class AddNote_Activity extends AppCompatActivity {

    private EditText title,description;
    private MaterialButton btnSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        setUpComponents();
    }

    public void setUpComponents()
    {
        title=findViewById(R.id.noteTitle);
        description=findViewById(R.id.noteDescription);
        btnSaveNote=findViewById(R.id.btnSaveNote);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        if(bundle!=null)
        {
            Note note= (Note) bundle.getSerializable(MainActivity.NOTE_KEY);
            title.setText(note.getTitle());
            description.setText(note.getDescription());
            btnSaveNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Note note=new Note(title.getText().toString(),description.getText().toString(),System.currentTimeMillis());
                    EditNote(note,bundle.getInt(MainActivity.POSITION_KEY));
                }
            });
        }
        else
        {
            btnSaveNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateNewNote();
                }
            });
        }
    }

    public void CreateNewNote()
    {
        String Title=title.getText().toString();
        if(Title.length()==0)
        {
            Toast.makeText(this, "You can't leave empty title", Toast.LENGTH_SHORT).show();
            return;
        }
        String Description=description.getText().toString();
        long currentTime=System.currentTimeMillis();
        Note note=new Note(Title,Description,currentTime);

        String all=Title+","+Description+","+currentTime+"\n";
        try {
            File file=new File(MainActivity.fileName);
            if(!file.exists())
            {
                file.createNewFile();
            }
            FileOutputStream fos=new FileOutputStream(file,true);
            fos.write(all.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }catch (Exception e)
        {
            Log.e("Loi",e.toString());
        }
        Intent i=new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable(MainActivity.NOTE_KEY,note);
        i.putExtras(bundle);
        setResult(RESULT_OK,i);
        finish();
    }

    public void EditNote(Note note,int position)
    {
        int lineIndex=0;
        String allLine="";
        try{

            FileInputStream fis=new FileInputStream(MainActivity.fileName);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();

            while (line != null) {
                if(lineIndex!=position)
                {
                    allLine=allLine+line+"\n";
                }
                else
                {
                    String lineReplace=note.getTitle()+","+note.getDescription()+","+System.currentTimeMillis();
                    allLine=allLine+lineReplace+"\n";
                }
                lineIndex++;
                line=br.readLine();
            }
            File file=new File(MainActivity.fileName);
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(allLine.getBytes(StandardCharsets.UTF_8));
            fis.close();
            br.close();
            fos.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable(MainActivity.NOTE_KEY,note);
        bundle.putInt(MainActivity.POSITION_KEY,position);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}