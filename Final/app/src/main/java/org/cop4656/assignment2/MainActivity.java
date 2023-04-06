package org.cop4656.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.cop4656.assignment2.model.URLsViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

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

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String message = ((TextView)findViewById(R.id.newWord)).getText().toString();
                if(!message.contains(" ")) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("keywords");

                    myRef.setValue(message);
                }
                else
                {}
            }
        });
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
            //check if the message contains any words in the database and if it does add it to the database

            /*if (validateMessage(bookMark))
            {
                String url = bookMark.substring(6);
                URLsViewModel sharedViewModel = new ViewModelProvider(this).get(URLsViewModel.class);
                ArrayList<String> listOfUrls = sharedViewModel.getWrappedListOfUrls().getValue();
                if (listOfUrls.size() < 5)
                {
                    listOfUrls.add(url);
                }
                else
                {
                    listOfUrls.remove(4);
                    listOfUrls.add(url);
                }
                ListView listView = findViewById(R.id.URListView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfUrls);
                listView.setAdapter(adapter);
                sharedViewModel.setWrappedUrlClicked(url);
            }
            else
            {
                Toast.makeText(this, "No valid bookmark was found in SMS!", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private boolean validateMessage(String bookMark)
    {
        //modify to check if text contains in database
        return bookMark.startsWith("booky:");
    }


}