package com.example.myschedule.ui.doc;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.example.myschedule.R;

import java.io.IOException;
import java.io.InputStream;

public class DocDetailFragment extends Fragment {


    public DocDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_doc_detail, container, false);
        TextView name = root.findViewById(R.id.name_doc);
        ImageView doc_img = root.findViewById(R.id.img_doc);
        name.setText(getArguments().getString("doc_name"));
        InputStream inputStream = null;
        try{
            inputStream = root.getContext().getApplicationContext().getAssets().open(getArguments().getString("doc_img"));
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            doc_img.setImageDrawable(drawable);
            doc_img.setOnTouchListener(new ImageMatrixTouchHandler(root.getContext()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(inputStream!=null)
                    inputStream.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

        }
        return root;
    }
}