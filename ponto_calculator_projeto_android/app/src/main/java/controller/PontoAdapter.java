package controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponto_calculator.R;

import java.util.List;

import model.Ponto;

public class PontoAdapter extends RecyclerView.Adapter<PontoAdapter.ViewHolder>
{

    private List<Ponto> dataList;
    private Context context;

    public PontoAdapter(Context context, List<Ponto> dataList)
    {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_ponto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Ponto item = dataList.get(position);
        holder.textViewItem.setText(item.isInt_outLiteral());
        holder.tvTimePonto.setText(item.getDataPonto());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewItem, tvTimePonto;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            tvTimePonto = itemView.findViewById(R.id.tvTimePonto);
        }
    }
}