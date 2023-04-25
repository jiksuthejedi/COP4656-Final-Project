package org.cop4656.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.cop4656.assignment2.model.URLsViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //Instantiate database here, database will have two tables, one for keywords and one for texts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED)
        {
            String[] permissions = new String[]{Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissions, 101);
        }

        Intent intent = getIntent();
        String bookMark = intent.getStringExtra("sms");
        addURLAndDisplayWebpage(bookMark);

        Button button = (Button)findViewById(R.id.button);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(0, URLListFragment.class, null);
        transaction.addToBackStack(null);
        transaction.commit();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.child("keywords").getChildrenCount();
                        int currentIndex = 0;
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            if (childSnapshot.child(Integer.toString(currentIndex)).getValue(String.class).toLowerCase().contains(((EditText) findViewById(R.id.newWord)).getText().toString().toLowerCase())) {
                                ((EditText) findViewById(R.id.newWord)).setTextColor(Color.RED);
                                Context context = getApplicationContext();
                                CharSequence text = "Keyword already exists";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                return;
                            }
                            currentIndex++;
                        }
                        String s = ((EditText)findViewById(R.id.newWord)).getText().toString();
                        databaseRef.child("keywords").child(Integer.toString((int) count)).setValue(((EditText) findViewById(R.id.newWord)).getText().toString());
                        ((EditText) findViewById(R.id.newWord)).setTextColor(Color.GRAY);
                        Context context = getApplicationContext();
                        CharSequence text = "Question added";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        Intent i = new Intent(MainActivity.this,URLListFragment.class);

    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        String bookMark = intent.getStringExtra("sms");
        addURLAndDisplayWebpage(bookMark);
    }

    private void addURLAndDisplayWebpage(String bookMark)
    {
        if (bookMark != null)
        {
            DatabaseReference d = databaseRef.child("texts");
            DatabaseReference g = d.push();

            DatabaseReference k = databaseRef.child("keywords");
            Task<DataSnapshot> t = k.get();
            while(!t.isComplete()){}
            DataSnapshot i = t.getResult();

            int currentIndex = 0;
            boolean has = false;
            for (DataSnapshot childSnapshot : i.getChildren()) {
                if (bookMark.contains(childSnapshot.child(Integer.toString(currentIndex)).getValue(String.class).toLowerCase())) {
                    has = true;
                }
                currentIndex++;
            }
            if(has)
                g.setValue(bookMark);
            else
            {
                Context context = getApplicationContext();
                CharSequence text = "No keywords in text";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return;
            }

            //check if the message contains any words in the database and if it does add it to the database

            /*if (validateMessage(bookMark))
            {

                ArrayList<String> listOfUrls = new ArrayList<String>();
                for(DataSnapshot childSnapshot : d.getChildren())
                {
                    listOfUrls.add(childSnapshot.getValue(String.class));
                }
                listOfUrls.add(url);
                ListView listView = findViewById(R.id.URListView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfUrls);
                listView.setAdapter(adapter);
            }
            else
            {
                Toast.makeText(this, "No valid keyword was found in SMS!", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private boolean validateMessage(String bookMark)
    {


        return true;
    }


}