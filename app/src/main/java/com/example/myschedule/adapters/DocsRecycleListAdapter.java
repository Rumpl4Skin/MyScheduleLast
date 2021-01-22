package com.example.myschedule.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.data.Admins;
import com.example.myschedule.data.Docs;
import com.example.myschedule.data.RecyclerViewDocsHolder;
import com.example.myschedule.data.RecyclerViewHolder;

import java.io.IOException;
import java.io.InputStream;


public class DocsRecycleListAdapter extends RecyclerView.Adapter<RecyclerViewDocsHolder> {
    private Docs[] docs;
    private Context context;

    public DocsRecycleListAdapter(Docs[] docs1, Context c) {
        docs=docs1;
        context=c;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_docs;
    }

    @NonNull
    @Override
    public RecyclerViewDocsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewDocsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDocsHolder holder, int position) {
        holder.getNameView().setText(String.valueOf(docs[position].getName()));
        Bundle bundle = new Bundle();
        bundle.putString("doc_name", docs[position].getName());
        bundle.putString("doc_img", docs[position].getImg());
        holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_doc_detail,bundle));
    }

    @Override
    public int getItemCount() {
        return docs.length;
    }
}
