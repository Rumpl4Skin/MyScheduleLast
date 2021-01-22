package com.example.myschedule.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.MainActivity;
import com.example.myschedule.R;
import com.example.myschedule.ui.doc.DocDetailFragment;
import com.example.myschedule.ui.login.LoginActivity;

public class RecyclerViewDocsHolder extends RecyclerView.ViewHolder {

    private TextView Name;

    public RecyclerViewDocsHolder(@NonNull View itemView) {
        super(itemView);
        Name = itemView.findViewById(R.id.docs_name);
    }

    public TextView getNameView() {
        return Name;
    }
}
