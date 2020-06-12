package com.tmstudios.grammymaster;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
	DatabaseAdapter b;
    View view;
    RecyclerView recyclerView;
    ArrayList<DatabaseAdapter.Result> arrayList;

    CustomAdapter adapter;
	
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
		arrayList = new ArrayList<DatabaseAdapter.Result>();
        
		b = DatabaseAdapter.getInstance(this.getContext());
		DatabaseAdapter.Condition t = new DatabaseAdapter.Condition();
		arrayList.addAll(b.getAwardList(t));
		
        adapter = new CustomAdapter(arrayList,R.layout.item_list_big);
        recyclerView.setAdapter(adapter);    
        return view;
    }


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

        ArrayList<DatabaseAdapter.Result> arrayList;
		int cardLayout;
        public CustomAdapter(ArrayList<DatabaseAdapter.Result> arrayList,int cardLayout) {
            this.arrayList = arrayList;
			this.cardLayout = cardLayout;
        }
        @Override
        public  ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(cardLayout, viewGroup, false);
            return new ViewHolder(view);
        }
        @Override
        public  void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.name.setText(arrayList.get(position).award);
            viewHolder.image.setImageResource(R.drawable.ic_stars_white_24dp);
        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }
		
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView image;
            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }
}
