package com.ayush.wikisearch.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ayush.wikisearch.R;
import com.ayush.wikisearch.adapter.SearchSuggestArrayAdapter;
import com.ayush.wikisearch.model.ErrorObject;
import com.ayush.wikisearch.model.SearchSuggestion;
import com.ayush.wikisearch.network.ClientGenerator;
import com.ayush.wikisearch.network.RequestBuilder;
import com.ayush.wikisearch.network.WSResponseListener;
import com.ayush.wikisearch.utils.AppUtil;
import com.ayush.wikisearch.view.OnLoadMoreListener;

public class MainActivity extends AppCompatActivity implements OnLoadMoreListener {

    private EditText searchBox;
    private ImageButton headerSearchButton;
    private SearchSuggestArrayAdapter arrayAdapter;
    private Handler handler;
    private String previousSearch = "";
    private ListView suggestionListView;
    private View footerView;
    private int currentPage = 0;
    private String prefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        initUI();
        initListeners();
    }

    private void initUI() {
        searchBox = (EditText) findViewById(R.id.et_search);
        headerSearchButton = (ImageButton) findViewById(R.id.btn_searchCross);
        suggestionListView = (ListView) findViewById(R.id.lv_suggestion);
        footerView = LayoutInflater.from(this).inflate(R.layout.progress_bar, null);
        suggestionListView.addFooterView(footerView);
    }

    private void initListeners() {

        headerSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBox.setText("");
                searchBox.requestFocus();
            }
        });

        /* text changed listener so that new api will be called and clear the previous one  */

        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    headerSearchButton.setSelected(true);
                } else {
                    headerSearchButton.setSelected(false);
                }
            }

            /* Api will be called after a delay of 800ms   */

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!previousSearch.equalsIgnoreCase(text)) {
                    if (arrayAdapter != null)
                        arrayAdapter.clear();
                    currentPage = 0;
                    suggestionListView.setVisibility(View.GONE);
                    sendRequest("autosuggest", text, 800);
                    previousSearch = text;
                }

            }
        });

    }

    /* update method to hide and show the loader  */

    public void updateFooterVisibility(boolean visibility) {
        if (visibility) {
            footerView.setVisibility(View.VISIBLE);
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    /* method that manages api & UI when new character is added or deleted */

    public synchronized void sendRequest(final String tag, final String text, int delay) {
        if (!TextUtils.isEmpty(text)) {
            suggestionListView.setVisibility(View.VISIBLE);
            removeDelayedCalls(tag);
            headerSearchButton.setImageResource(R.drawable.drawable_progress_cross);
            AnimationDrawable animationDrawable = (AnimationDrawable) headerSearchButton.getDrawable();
            animationDrawable.start();
            headerSearchButton.setClickable(false);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    callApi(text, currentPage);
                }
            };
            handler.postAtTime(r, tag, SystemClock.uptimeMillis() + delay);
        } else {
            headerSearchButton.setImageResource(R.drawable.sel_search);
            headerSearchButton.setClickable(true);
            if (arrayAdapter != null)
                arrayAdapter.clear();
            currentPage = 0;
            suggestionListView.setVisibility(View.GONE);
        }
    }

    /* method to call image search suggestion api, handled retrofit and api error separately because api is throwing error with status code of 200 */

    private void callApi(final CharSequence s, final int currentPage) {
        prefix = s.toString();
        if (!TextUtils.isEmpty(prefix)) {
            RequestBuilder requestBuilder = ClientGenerator.createService(RequestBuilder.class);
            requestBuilder.getSearchSuggestion("query", "pageimages", "json", "thumbnail", "50", "50", "prefixsearch", prefix, currentPage * 10 + "", new WSResponseListener<SearchSuggestion>(this) {
                @Override
                public void onSuccess(SearchSuggestion result) {

                    if (result.error == null) {
                        headerSearchButton.setImageResource(R.drawable.sel_search);
                        headerSearchButton.setClickable(true);
                        if (result.query.pages != null && searchBox.getText() != null && searchBox.getText().length() > 0) {
                            if (arrayAdapter == null) {
                                arrayAdapter = new SearchSuggestArrayAdapter(MainActivity.this, result.query);
                                arrayAdapter.setPage(10);
                                arrayAdapter.setOnLoadMoreListener(MainActivity.this);
                                arrayAdapter.setContinue(result._continue);
                                suggestionListView.setAdapter(arrayAdapter);
                            } else {
                                arrayAdapter.addAll(result.query.pages);
                                arrayAdapter.setContinue(result._continue);
                            }
                        }
                    } else {
                        headerSearchButton.setImageResource(R.drawable.sel_search);
                        headerSearchButton.setClickable(true);
                        AppUtil.showShortToast(MainActivity.this, result.error.getErrorMessage());
                    }
                }

                @Override
                public void onError(ErrorObject error) {
                    headerSearchButton.setImageResource(R.drawable.sel_search);
                    headerSearchButton.setClickable(true);
                    AppUtil.showShortToast(MainActivity.this, error.getErrorMessage());
                }
            });
        }
    }

    /* method to remove the delayed api calls */

    private void removeDelayedCalls(String tag) {
        handler.removeCallbacksAndMessages(tag);
    }

    /* overriding onLoadMore method for pagination */

    @Override
    public void onLoadMore(boolean isLoading) {
        if (isLoading) {
            currentPage++;
            updateFooterVisibility(true);
            callApi(prefix, currentPage);
        } else
            updateFooterVisibility(false);
    }
}
