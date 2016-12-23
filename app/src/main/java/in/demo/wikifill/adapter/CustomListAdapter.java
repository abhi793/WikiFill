package in.demo.wikifill.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import in.demo.wikifill.Model.ListItemModel;
import in.demo.wikifill.R;

/**
 * Created by Abhishek Pc on 22-12-2016.
 */

public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<ListItemModel> listItemModels;
    private ArrayList<String> shuffledList;
    private ViewHolder holder;

    public CustomListAdapter(Activity activity, ArrayList<ListItemModel> listItemModels,ArrayList<String> shuffledList) {
        this.activity = activity;
        this.listItemModels = listItemModels;
        this.shuffledList = shuffledList;
    }

    @Override
    public int getCount() {
        return listItemModels.size();
    }

    @Override
    public Object getItem(int location) {
        return listItemModels.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         holder = new ViewHolder();
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);


            holder.start = (TextView) convertView
                    .findViewById(R.id.start_text);
            holder.end = (TextView) convertView.findViewById(R.id.end_text);
            holder.blank = (AutoCompleteTextView) convertView.findViewById(R.id.fill_blank);
            holder.adapter=new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line,shuffledList);

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // getting  data for the row
        ListItemModel m = listItemModels.get(position);

        // setting values to the ui fields
        holder.start.setText(m.getStartLine());
        holder.end.setText(m.getEndLine());
        holder.blank.setAdapter(holder.adapter);
        holder.blank.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                holder.blank.showDropDown();
                return false;
            }
        });

        return convertView;
    }
    private static class ViewHolder {
        public   TextView start;
        public   AutoCompleteTextView blank;
        public   TextView end;
        public ArrayAdapter<String> adapter;
    }

}
