package org.cop4656.assignment2.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class URLsViewModel extends ViewModel
{
    ArrayList<String> listOfUrls;
    MutableLiveData<ArrayList<String>> wrappedListOfUrls;
    String urlClicked;
    MutableLiveData<String> wrappedUrlClicked;

    {
        listOfUrls = new ArrayList<>();
        wrappedListOfUrls = new MutableLiveData<>();
        listOfUrls.add("https://developer.android.com");
        listOfUrls.add("https://firebase.google.com/");
        listOfUrls.add("https://www.fsu.edu/");
        wrappedListOfUrls.setValue(listOfUrls);
        urlClicked = "https://developer.android.com";
        wrappedUrlClicked = new MutableLiveData<>();
        wrappedUrlClicked.setValue(urlClicked);
    }

    public LiveData<ArrayList<String>> getWrappedListOfUrls()
    {
        return wrappedListOfUrls;
    }

    /*public void addUrl(String url)
    {
        listOfUrls = wrappedListOfUrls.getValue();
        listOfUrls.add(url);
        wrappedListOfUrls.setValue(listOfUrls);
    }*/

    public LiveData<String> getWrappedUrlClicked()
    {
        return wrappedUrlClicked;
    }

    public void setWrappedUrlClicked(String url)
    {
        urlClicked = url;
        wrappedUrlClicked.setValue(urlClicked);
    }
}
