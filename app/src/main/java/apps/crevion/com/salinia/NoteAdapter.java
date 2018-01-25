package apps.crevion.com.salinia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yusufaw on 11/20/17.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    public List<Note> getData() {
        return noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        NoteAdapter.ViewHolder viewHolder = new NoteAdapter.ViewHolder(v, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewNote.setText(noteList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNote;
        public Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);

            textViewNote = itemView.findViewById(R.id.text_note);
            this.context = context;
        }
    }
}
