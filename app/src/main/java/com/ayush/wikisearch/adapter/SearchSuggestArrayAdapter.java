package com.ayush.wikisearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ayush.wikisearch.R;
import com.ayush.wikisearch.model.Continue;
import com.ayush.wikisearch.model.PageElement;
import com.ayush.wikisearch.model.Pages;
import com.ayush.wikisearch.utils.AppUtil;
import com.ayush.wikisearch.view.OnLoadMoreListener;

import java.util.Map;

/**
 * Created by ayushkedia on 03/06/16.
 */
public class SearchSuggestArrayAdapter extends BaseAdapter {
    private final LayoutInflater inflator;
    private Pages mObjects;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;
    private int pageSize;
    private Continue _continue;
    private static final int FOOTER_IMAGE = 2;
    private static final int IMAGE = 1;

    public SearchSuggestArrayAdapter(Context context, Pages objects) {
        this.context = context;
        inflator = LayoutInflater.from(context);
        mObjects = objects;
    }

    public void clear() {
        if (mObjects != null)
            mObjects.pages.clear();
    }

    /* OnLoadMoreListener for pagination */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setPage(int pageSize) {
        this.pageSize = pageSize;
    }

    /*  set continue so that it will be checked during pagination api calls */
    public void setContinue(Continue _continue) {
        this._continue = _continue;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mObjects.pages.size() - 1 && mObjects.pages.size() == pageSize && _continue != null)
            return FOOTER_IMAGE;
        else
            return IMAGE;
    }

    @Override
    public int getCount() {
        return mObjects.pages.size();
    }

    @Override
    public PageElement getItem(int position) {
        return mObjects.pages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position) == FOOTER_IMAGE) {
            onLoadMoreListener.onLoadMore(true);
        } else {
            onLoadMoreListener.onLoadMore(false);
        }

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.item_search_suggestion, parent, false);
        }
        View view = convertView;
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);

        Object Key = mObjects.pages.keySet().toArray()[position];
        PageElement valueForKey = mObjects.pages.get(Key);

        /* using glide to load image from network where default image is bg_wiki */
        if (valueForKey.thumbnail != null && valueForKey.thumbnail.source != null) {
            AppUtil.getInstance().loadImageGlide(context, valueForKey.thumbnail.source, imageView, R.drawable.bg_wiki);
        } else
            imageView.setImageResource(R.drawable.bg_wiki);

        return view;
    }

    public void addAll(Map<String, PageElement> collection) {
        mObjects.pages.putAll(collection);
        setPage(mObjects.pages.size());
        notifyDataSetChanged();
    }

}
