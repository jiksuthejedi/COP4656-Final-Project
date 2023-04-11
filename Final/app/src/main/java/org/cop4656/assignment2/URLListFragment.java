package org.cop4656.assignment2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.cop4656.assignment2.model.URLsViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class URLListFragment extends Fragment
{
    DatabaseReference databaseRef;

    public URLListFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.url_list_fragment, container, false);
    }



    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        databaseRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference d1 = databaseRef.child("texts");
        Task<DataSnapshot> t = d1.get();
        while(! t.isSuccessful())
        {}
        ListView listView = (ListView) view.findViewById(R.id.URListView);
                DataSnapshot d = t.getResult();
                ArrayList<String> listOfURLs = new ArrayList<String>();
                for(DataSnapshot childSnapshot : d.getChildren())
                {
                    listOfURLs.add(childSnapshot.getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listOfURLs);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

        //Pull from database and display all of the texts in it



    }
}