package org.cop4656.assignment2;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.lifecycle.ViewModelProvider;

import org.cop4656.assignment2.model.URLsViewModel;

import java.util.ArrayList;

public class URLListFragment extends Fragment
{
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
        ListView listView = view.findViewById(R.id.URListView);
        URLsViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(URLsViewModel.class);
        ArrayList<String> listOfURLs = sharedViewModel.getWrappedListOfUrls().getValue();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listOfURLs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String urlClicked = listOfURLs.get(position);
                sharedViewModel.setWrappedUrlClicked(urlClicked);
            }
        });
    }
}