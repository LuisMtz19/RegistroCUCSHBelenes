package ccv.checkhelzio.registrocucshbelenes.ui;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ccv.checkhelzio.registrocucshbelenes.R;

public class EventosAdaptador extends RecyclerView.Adapter<EventosAdaptador.EventosViewHolder> {

    private List<Eventos> eventos;

    public EventosAdaptador(List<Eventos> eventos) {
        this.eventos = eventos;
    }

    @Override
    public EventosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eventos, parent, false);
        return new EventosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventosViewHolder eventosViewHolder , int position) {
        Eventos evento = eventos.get(position);
        eventosViewHolder.titulo_evento.setText(evento.getTitulo_evento());
        eventosViewHolder.nombre_org.setText(evento.getNombre_organizador());
        eventosViewHolder.auditorio.setText(evento.getAuditorio());
        eventosViewHolder.horario.setText(evento.getHorario());
        eventosViewHolder.contenedor.setCardBackgroundColor(evento.getFondo());
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class EventosViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo_evento, nombre_org, auditorio, horario;
        private CardView contenedor;

        public EventosViewHolder(View itemView) {
            super(itemView);
            titulo_evento = (TextView) itemView.findViewById(R.id.tv_titulo);
            nombre_org = (TextView) itemView.findViewById(R.id.tv_organizador);
            auditorio = (TextView) itemView.findViewById(R.id.tv_auditorio);
            horario = (TextView) itemView.findViewById(R.id.tv_horario);
            contenedor = (CardView) itemView.findViewById(R.id.boton_eventos);
        }
    }
}
