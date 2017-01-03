package com.example.caboc.sqliteestudiantesprofesores;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by caboc on 28/12/2016.
 */
public class AdaptadorEstudiantesProfesores extends RecyclerView.Adapter<AdaptadorEstudiantesProfesores.EstudiantesProfesoresViewHolder> {

    ArrayList<Usuario> usuarios;

    public AdaptadorEstudiantesProfesores(ArrayList<Usuario> usuarios){
        this.usuarios = usuarios;
        Log.d("prueba","llego al adaptador");
    }

    public static class EstudiantesProfesoresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtId;
        TextView txtNombre;
        TextView txtCargo;
        String curso;
        String ciclo;
        String edad;
        String extra;
        ImageButton botonVer;
        ImageButton botonModificar;

        ImageView foto;
        LinearLayout linearLayoutItem;

        public EstudiantesProfesoresViewHolder(View itemView) {
            super(itemView);

            txtId = (TextView) itemView.findViewById(R.id.textViewIdUsuario);
            txtNombre = (TextView) itemView.findViewById(R.id.textViewNombreUsuario);
            txtCargo = (TextView) itemView.findViewById(R.id.textViewCargoUsuario);
            foto = (ImageView) itemView.findViewById(R.id.imageViewFoto);
            linearLayoutItem = (LinearLayout) itemView.findViewById(R.id.linearLayoutItem);
            botonVer = (ImageButton) itemView.findViewById(R.id.imageButtonVer);
            botonModificar = (ImageButton) itemView.findViewById(R.id.imageButtonModificar);
            botonVer.setOnClickListener(this);
            botonModificar.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(),ActivityUsuario.class);
            Bundle bundle = new Bundle();

            bundle.putString("id",txtId.getText().toString());
            bundle.putString("nombre",txtNombre.getText().toString());
            bundle.putString("cargo",txtCargo.getText().toString());
            bundle.putString("ciclo",ciclo);
            bundle.putString("curso",curso);
            bundle.putString("edad",edad);
            bundle.putString("extra",extra);

            switch (v.getId()){
                case R.id.imageButtonVer :


                    bundle.putString("opcion","ver");

                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                    break;

                case R.id.imageButtonModificar:

                    bundle.putString("opcion","modificar");

                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                    break;
            }

        }
    }

    @Override
    public EstudiantesProfesoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new EstudiantesProfesoresViewHolder(v);

    }

    @Override
    public void onBindViewHolder(EstudiantesProfesoresViewHolder holder, int position) {

        holder.txtId.setText(""+usuarios.get(position).getId());
        holder.txtNombre.setText(usuarios.get(position).getNombre());
        holder.txtCargo.setText(usuarios.get(position).getCargo());
        holder.edad = ""+usuarios.get(position).getEdad();
        holder.curso = ""+usuarios.get(position).getCurso();
        holder.ciclo = usuarios.get(position).getCiclo();
        holder.extra = usuarios.get(position).getExtra();

        if(usuarios.get(position).getCargo().equals("Estudiante")){
            holder.foto.setImageResource(R.drawable.estudiante);
            holder.linearLayoutItem.setBackgroundResource(R.color.estudiantesItem);
        }else{
            holder.foto.setImageResource(R.drawable.usuario);
            holder.linearLayoutItem.setBackgroundResource(R.color.proesoresItem);
        }


    }

    @Override
    public int getItemCount() {
        return usuarios.size();

    }
}
