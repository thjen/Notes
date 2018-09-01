package qthjen_dev.io.notes.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import qthjen_dev.io.notes.Model.Note;
import qthjen_dev.io.notes.R;
import qthjen_dev.io.notes.Utils.OnPressDelete;
import qthjen_dev.io.notes.Utils.OnPressEdit;
import qthjen_dev.io.notes.Utils.OnPressItem;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Note> arrayList;
    private ArrayList<Note> listFilter;

    private OnPressDelete onDelete;
    private OnPressEdit onEdit;
    private OnPressItem onPress;

    public NoteAdapter(Context context, ArrayList<Note> arrayList, OnPressDelete onDelete, OnPressEdit onEdit
                        , OnPressItem onPress) {
        this.context = context;
        this.arrayList = arrayList;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
        this.onPress = onPress;
        this.listFilter = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_note_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.note.setMaxLines(2);
        holder.note.setEllipsize(TextUtils.TruncateAt.END);
        holder.note.setText(arrayList.get(position).getNote());
        holder.timestamp.setText(arrayList.get(position).getTimestamp());
        holder.dot.setText(Html.fromHtml("&#9679"));
        holder.dot.setTextColor(getRandomColorDot("400"));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete.onDelete(Integer.parseInt(arrayList.get(position).getNid()), position);
                holder.swipell.close();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit.onEdit(Integer.parseInt(arrayList.get(position).getNid()), position);
                holder.swipell.close();
            }
        });

        holder.swipell.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPress.onPress(arrayList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // create dot random color
    private int getRandomColorDot(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor,
                "array", context.getPackageName());
        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String work = constraint.toString();
                if (work.isEmpty()) {
                    arrayList = listFilter;
                } else {
                    ArrayList<Note> filteredList = new ArrayList<>();
                    for (Note row:listFilter) {
                        if (row.getNote().toLowerCase().contains(work.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    arrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<Note>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dot)
        TextView dot;
        @BindView(R.id.noteDes)
        TextView note;
        @BindView(R.id.timestamp)
        TextView timestamp;
        @BindView(R.id.delete)
        TextView delete;
        @BindView(R.id.edit)
        TextView edit;
        @BindView(R.id.swipell)
        SwipeLayout swipell;

        View myView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            myView = itemView;
        }
    }
}
