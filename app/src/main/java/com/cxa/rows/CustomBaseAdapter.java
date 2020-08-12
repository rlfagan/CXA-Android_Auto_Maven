package com.cxa.rows;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxa.MainActivity;
import com.cxa.R;
import com.cxa.search.ListOfProductsActivity;
import com.tl.uic.Tealeaf;

import java.util.ArrayList;

/**
 * Created by dancrisan on 1/13/17.
 */

public class CustomBaseAdapter extends BaseAdapter implements Filterable {
    public Context context;
    public ArrayList<RowItem> rowItems;
    public ArrayList<RowItem> orig;
    public ViewHolder holder;
    boolean fromSearch;

    public CustomBaseAdapter(Context context, ArrayList<RowItem> items, boolean fromSearch) {
        super();
        this.context = context;
        this.rowItems = items;
        this.fromSearch = fromSearch;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtDesc;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<RowItem> results = new ArrayList<RowItem>();
                if (orig == null)
                    orig = rowItems;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final RowItem g : orig) {
                            if (g.getDesc().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                rowItems = (ArrayList<RowItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_main_list_row, parent, false);
            holder = new ViewHolder();
            holder.txtDesc = convertView.findViewById(R.id.desc);
            holder.imageView = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RowItem rowItem = (RowItem) getItem(position);

        final int indexRow = position;

        holder.txtDesc.setText(rowItem.getDesc());

        holder.imageView.setImageResource(rowItem.getImageId());

        if (fromSearch) {
            holder.imageView.setVisibility(View.GONE);
        }

        //mask second row of MainActivity class
        if (indexRow == 2 && context.getClass().equals(MainActivity.class)) {
            holder.txtDesc.setTag("third-row");
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvents(indexRow);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }


    public void clickEvents(int indexRow) {
        if (indexRow == 0) {
            Intent intent = new Intent(context, ListOfProductsActivity.class);
            Bundle b = new Bundle();
            b.putString("key", "Tablets");
            intent.putExtras(b);
            context.startActivity(intent);
        } else if (indexRow == 1) {
            Intent intent = new Intent(context, ListOfProductsActivity.class);
            Bundle b = new Bundle();
            b.putString("key", "Furniture");
            intent.putExtras(b);
            context.startActivity(intent);
        } else if (indexRow == 2) {
            Intent intent = new Intent(context, ListOfProductsActivity.class);
            Bundle b = new Bundle();
            b.putString("key", "Laptops");
            intent.putExtras(b);
            context.startActivity(intent);
        }
    }
}