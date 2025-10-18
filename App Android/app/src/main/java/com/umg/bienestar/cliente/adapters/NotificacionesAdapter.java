package com.umg.bienestar.cliente.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.models.Notificacion;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {

    private Context context;
    private List<Notificacion> notificaciones;

    public NotificacionesAdapter(Context context) {
        this.context = context;
        this.notificaciones = new ArrayList<>();
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        Notificacion notificacion = notificaciones.get(position);
        holder.bind(notificacion);
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcono, tvTitulo, tvMensaje, tvFecha;
        View indicadorNoLeida;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcono = itemView.findViewById(R.id.tvIcono);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            indicadorNoLeida = itemView.findViewById(R.id.indicadorNoLeida);
        }

        public void bind(Notificacion notificacion) {
            // Icono según tipo
            String icono = getIconoPorTipo(notificacion.getTipo());
            tvIcono.setText(icono);

            // Datos
            tvTitulo.setText(notificacion.getTitulo());
            tvMensaje.setText(notificacion.getMensaje());

            // Tiempo relativo
            String tiempoRelativo = calcularTiempoRelativo(notificacion.getFecha());
            tvFecha.setText("🕐 " + tiempoRelativo);

            // Indicador de no leída
            indicadorNoLeida.setVisibility(notificacion.isLeida() ? View.GONE : View.VISIBLE);
        }

        private String getIconoPorTipo(String tipo) {
            switch (tipo) {
                case "CITA_CONFIRMADA":
                    return "✅";
                case "CITA_RECHAZADA":
                    return "❌";
                case "RECORDATORIO":
                    return "⏰";
                case "FACTURA":
                    return "💳";
                case "CANCELACION":
                    return "🚫";
                default:
                    return "🔔";
            }
        }

        private String calcularTiempoRelativo(String fecha) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                Date fechaNotificacion = sdf.parse(fecha);
                Date ahora = new Date();

                long diferenciaMs = ahora.getTime() - fechaNotificacion.getTime();
                long minutos = TimeUnit.MILLISECONDS.toMinutes(diferenciaMs);
                long horas = TimeUnit.MILLISECONDS.toHours(diferenciaMs);
                long dias = TimeUnit.MILLISECONDS.toDays(diferenciaMs);

                if (minutos < 1) {
                    return "Hace un momento";
                } else if (minutos < 60) {
                    return "Hace " + minutos + " min";
                } else if (horas < 24) {
                    return "Hace " + horas + " hora" + (horas > 1 ? "s" : "");
                } else if (dias < 7) {
                    return "Hace " + dias + " día" + (dias > 1 ? "s" : "");
                } else {
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    return outputFormat.format(fechaNotificacion);
                }
            } catch (ParseException e) {
                return fecha;
            }
        }
    }
}