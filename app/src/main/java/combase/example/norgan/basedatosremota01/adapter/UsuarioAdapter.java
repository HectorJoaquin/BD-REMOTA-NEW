package combase.example.norgan.basedatosremota01.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import combase.example.norgan.basedatosremota01.R;
import combase.example.norgan.basedatosremota01.entidades.Usuario;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>{

    ArrayList<Usuario> listaUsuarios;

    public UsuarioAdapter(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }




    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.usuarios_list,viewGroup,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);

        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder usuarioViewHolder, int i) {

        usuarioViewHolder.tvDocumentR.setText(listaUsuarios.get(i).getDocumento().toString());
        usuarioViewHolder.tvNombreR.setText(listaUsuarios.get(i).getNombre().toString());
        usuarioViewHolder.tvProfesionR.setText(listaUsuarios.get(i).getProfesion().toString());

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {

    TextView tvDocumentR,tvNombreR,tvProfesionR;



        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);


            tvDocumentR = (TextView)itemView.findViewById(R.id.tvDocumentR);
            tvNombreR = (TextView)itemView.findViewById(R.id.tvNombreR);
            tvProfesionR = (TextView)itemView.findViewById(R.id.tvProfesionR);






        }
    }
}
