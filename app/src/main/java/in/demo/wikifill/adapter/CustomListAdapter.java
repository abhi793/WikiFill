package in.demo.wikifill.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.demo.wikifill.Model.ListItemModel;
import in.demo.wikifill.R;



public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<ListItemModel> listItemModels;
    private ArrayList<String> shuffledList;
    private String beforeTextchange;
    private ArrayAdapter<String> adapter;


    public CustomListAdapter(Activity activity, ArrayList<ListItemModel> listItemModels,
                             ArrayList<String> shuffledList) {
        this.activity = activity;
        this.listItemModels = listItemModels;
        this.shuffledList = shuffledList;
    }

    @Override
    public int getCount() {
        return listItemModels.size();
    }

    @Override
    public ListItemModel getItem(int location) {
        return listItemModels.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @SuppressLint({"ViewHolder", "InflateParams"})
        View myView = inflater.inflate(R.layout.list_item, null);

        final ViewHolder holder = new ViewHolder();
        holder.position = position;
        holder.start = (TextView) myView
                .findViewById(R.id.start_text);
        holder.end = (TextView) myView.findViewById(R.id.end_text);
        holder.blank = (AutoCompleteTextView) myView.findViewById(R.id.fill_blank);
        holder.backSpaceImageview = (ImageView) myView.findViewById(R.id.backspace_image);
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, shuffledList);


        // setting values to the ui fields
        holder.start.setText(listItemModels.get(position).getStartLine());
        holder.end.setText(listItemModels.get(position).getEndLine());
        holder.blank.setText(listItemModels.get(position).getBlank());
        System.out.println("listItem" + listItemModels.get(position).getBlank());
        holder.blank.setAdapter(adapter);

        holder.backSpaceImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.blank.getText().toString().equals("")) {
                    shuffledList.add(holder.blank.getText().toString());
                    holder.blank.setText("");
                    listItemModels.get(position).setBlank("");
                }
            }
        });
        holder.blank.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                holder.blank.showDropDown();
                System.out.println("onTouch" + position);
                return false;
            }


        });
        holder.blank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               beforeTextchange = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if( !charSequence.toString().equals("")) {
                    System.out.println("onTextChanged" + shuffledList.size());
                    listItemModels.get(position).setBlank(charSequence.toString());
                    shuffledList.remove(charSequence.toString());
                    if(beforeTextchange!=null && !beforeTextchange.equals(""))
                        shuffledList.add(beforeTextchange);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return myView;
    }
    private static class ViewHolder {
        int position;
        TextView start;
        AutoCompleteTextView blank;
        TextView end;
        ImageView backSpaceImageview;
    }

}
