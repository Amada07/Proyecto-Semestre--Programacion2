package com.umg.bienestar.cliente.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.models.Factura;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FacturasAdapter extends RecyclerView.Adapter<FacturasAdapter.FacturaViewHolder> {

    private Context context;
    private List<Factura> facturas;

    public FacturasAdapter(Context context) {
        this.context = context;
        this.facturas = new ArrayList<>();
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_factura, parent, false);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        Factura factura = facturas.get(position);
        holder.bind(factura);
    }

    @Override
    public int getItemCount() {
        return facturas.size();
    }

    class FacturaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumeroFactura, tvServicio, tvFecha, tvMonto;

        public FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumeroFactura = itemView.findViewById(R.id.tvNumeroFactura);
            tvServicio = itemView.findViewById(R.id.tvServicio);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvMonto = itemView.findViewById(R.id.tvMonto);
        }

        public void bind(Factura factura) {
            tvNumeroFactura.setText("Factura #" + factura.getNumeroFactura());
            tvServicio.setText("Servicio: " + factura.getServicioNombre());

            // Fecha formateada
            String fechaFormateada = formatearFecha(factura.getFechaEmision());
            tvFecha.setText("📅 " + fechaFormateada);

            // Monto
            tvMonto.setText(String.format(Locale.getDefault(), "Total: Q %.2f", factura.getMonto()));
        }

        private String formatearFecha(String fecha) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                Date date = inputFormat.parse(fecha);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return fecha;
            }
        }
    }
}