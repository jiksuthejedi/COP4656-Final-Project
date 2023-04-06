package org.cop4656.assignment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.cop4656.assignment2.model.URLsViewModel;

public class WebViewFragment extends Fragment
{
    public WebViewFragment()
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
        return inflater.inflate(R.layout.my_web_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        URLsViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(URLsViewModel.class);
        String urlClicked = sharedViewModel.getWrappedUrlClicked().getValue();
        webView.loadUrl(urlClicked);
        sharedViewModel.getWrappedUrlClicked().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                webView.loadUrl(s);
            }
        });
    }
}
