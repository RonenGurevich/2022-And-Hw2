package classes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huntinggame.R;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TTlistAdapter extends RecyclerView.Adapter<TTlistAdapter.ViewHolder> {

   private ArrayList<TopTenItem> items;
   private Consumer<TopTenItem> onClickListener = null;

   public TTlistAdapter(ArrayList<TopTenItem> items){
       this.items = items;
   }

    public void setOnClickListener(Consumer<TopTenItem> onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
   @Override
   public TTlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup vg, int type) {
       View view = LayoutInflater.from(vg.getContext())
               .inflate(R.layout.tt_item, vg, false);

       return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull TTlistAdapter.ViewHolder viewHolder, int position){
       TopTenItem item = items.get(position);
       viewHolder.setScore(item.getScore());

       viewHolder.itemView.setOnClickListener(view -> {
           if(onClickListener != null)
               onClickListener.accept(item);
           //TODO: change to call googlemaps API here
           Log.d("ViewHolderitem", item.toString());
       });
   }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

       private TextView scoreTXT;

        public ViewHolder(View view) {
            super(view);
            scoreTXT = view.findViewById(R.id.TT_TXT_item);

        }

        public void setScore(int score) {
            scoreTXT.setText(new StringBuilder().append(scoreTXT.getText().toString()).append(score).toString());
        }

    }
}
