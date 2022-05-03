package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huntinggame.R;

import java.util.function.Consumer;

import Utility.UtilityMethods;
import classes.TTlistAdapter;
import classes.TopTenItem;

public class TT_Fragment extends Fragment {

    TTlistAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.top_ten_fragment, container, false);

        adapter = new TTlistAdapter(UtilityMethods.loadTopTen(v.getContext()));
        RecyclerView rcv = v.findViewById(R.id.TT_RCV);

        rcv.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcv.getContext(),
                LinearLayoutManager.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);
        return v;
    }

    public void setOnTTItemClicked(Consumer<TopTenItem> consumer) {
        adapter.setOnClickListener(consumer);
    }
}