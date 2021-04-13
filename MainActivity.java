package com.example.morse2text;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    Button btVibrate;
    Vibrator vibrator;
    TextView tview;
    TextView Aview;
    Button btSpace;
    Button btClear;
    Button btDec;
    Button user1;
    Button user2;

    String UID = "a";
    String SendID = "b";

    char T[] = {'%', 'E', 'T', 'I', 'A', 'N', 'M', 'S', 'U', 'R',
            'W', 'D', 'K', 'G', 'O', 'H', 'V', 'F', '#', 'L', '#',
            'P', 'J', 'B', 'X', 'C', 'Y', 'Z', 'Q'};

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseDatabase dB = FirebaseDatabase.getInstance();
    DatabaseReference myRef = dB.getReference(UID);
    DatabaseReference Myref2 = database.getReference(SendID);



    private void M2T()
    {
        String data = tview.getText().toString();
        int tr = 0;
        String a = "";
        Aview.setText("");

        for(int i = 0; i < data.length(); i++)
        {
            if(tr > 28)
                break;
            if(data.charAt(i) == '.')
            {
                tr = 2*tr + 1;
            }
            if(data.charAt(i) == '_')
            {
                tr = 2*tr + 2;
            }
            if(data.charAt(i) == ' ')
            {
                a = a + T[tr];
//                a.concat(String.valueOf(T[tr]));
                tr = 0;
            }
            if(data.charAt(i) == '/')
            {
                a = a + T[tr];
                tr = 0;
                a = a + " ";
            }

        }
        if(UID.charAt(0) != '\0') {
            myRef.setValue(a);
        }

    }

    private void speak()
    {
        String text = Aview.getText().toString();
        mTTs.setPitch(1.02f);
        mTTs.setSpeechRate(1.02f);
        mTTs.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    long start = System.currentTimeMillis();
    long end = 0;
    private TextToSpeech mTTs;
    boolean mtts_okay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTTs = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)
                {
                    int result = mTTs.setLanguage(Locale.ENGLISH);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    mtts_okay = true;
                    Toast.makeText(MainActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Myref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Aview.setText(value);

            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
                Aview.setText("Failed");

            }
        });

        btVibrate = findViewById(R.id.button11);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        tview = findViewById(R.id.textView2);
        btSpace = findViewById(R.id.button22);
        btClear = findViewById(R.id.button33);
        btDec = findViewById(R.id.button44);
        Aview = findViewById(R.id.Alpha);
        user1 = findViewById(R.id.Uno1);
        user2 = findViewById(R.id.Uno2);



        btVibrate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //vibrator.vibrate(1000);
                tview.append(".");
            }
        });

        user1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                user1.setEnabled(false);
                user2.setEnabled(false);
                UID = "User1";
                SendID = "User2";
                Myref2 = database.getReference(SendID);
                myRef = database.getReference(UID);

                Myref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        Aview.setText(value);

                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        // Failed to read value
                        //Log.w(TAG, "Failed to read value.", error.toException());
                        Aview.setText("Failed");

                    }
                });
            }
        });

        user2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                user1.setEnabled(false);
                user2.setEnabled(false);
                UID = "User2";
                SendID = "User1";
                Myref2 = database.getReference(SendID);
                myRef = database.getReference(UID);

                Myref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        Aview.setText(value);

                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        // Failed to read value
                        //Log.w(TAG, "Failed to read value.", error.toException());
                        Aview.setText("Failed");

                    }
                });
            }


        });

        btVibrate.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                //vibrator.vibrate(1000);
                tview.append("_");
                return true;
            }
        });

        btSpace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                tview.append(" ");
            }
        });

        btSpace.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                tview.append("/");
                return true;
            }
        });

        btClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                speak();
            }
        });

        btDec.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TextView temp = findViewById(R.id.textView2);
                btClear.setEnabled(false);
                btSpace.setEnabled(false);
                btDec.setEnabled(false);
                btVibrate.setEnabled(false);
                String vtext = temp.getText().toString();
                M2T();
                for(int i = 0; i < vtext.length(); i++)
                {
                    if(vtext.charAt(i) == '.')
                    {
                        vibrator.vibrate(50);
                        /**********************Delay code**********************/
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /**********************Delay code**********************/
                    }
                    if(vtext.charAt(i) == '_')
                    {
                        vibrator.vibrate(200);
                        /**********************Delay code**********************/
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /**********************Delay code**********************/
                    }
                    if(vtext.charAt(i) == ' ')
                    {
                        /**********************Delay code**********************/
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /**********************Delay code**********************/
                    }

                    if(vtext.charAt(i) == '/')
                    {
                        /**********************Delay code**********************/
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /**********************Delay code**********************/
                    }
                }
                tview.setText("");
                btClear.setEnabled(true);
                btSpace.setEnabled(true);
                btDec.setEnabled(true);
                btVibrate.setEnabled(true);
            }
        });


    }

    @Override
    protected void onDestroy() {
        if(mTTs != null)
        {
            mTTs.stop();
            mTTs.shutdown();
        }
        super.onDestroy();
    }
}