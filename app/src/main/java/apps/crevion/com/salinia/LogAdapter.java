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

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private List<Note> noteList;

    public LogAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    public List<Note> getData() {
        return noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
        LogAdapter.ViewHolder viewHolder = new LogAdapter.ViewHolder(v, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewLog.setText(noteList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewLog;
        public Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);

            textViewLog = itemView.findViewById(R.id.text_log);
            this.context = context;
        }
    }
}
