package combase.example.norgan.basedatosremota01.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import combase.example.norgan.basedatosremota01.R;
import combase.example.norgan.basedatosremota01.entidades.Usuario;

public class UsuarioImagenAdapter extends RecyclerView.Adapter<UsuarioImagenAdapter.UsuarioImagenViewHolder> {

    ArrayList<Usuario> listaUsuarios;

    public UsuarioImagenAdapter(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }




    @NonNull
    @Override
    public UsuarioImagenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.usuario_list_imagen,viewGroup,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);

        return new UsuarioImagenViewHolder(vista);

    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioImagenViewHolder usuarioImagenViewHolder, int i) {


        usuarioImagenViewHolder.tvDocumentoI.setText(listaUsuarios.get(i).getDocumento().toString());
        usuarioImagenViewHolder.tvNombreI.setText(listaUsuarios.get(i).getNombre().toString());
        usuarioImagenViewHolder.tvProfesionI.setText(listaUsuarios.get(i).getProfesion().toString());

        if (listaUsuarios.get(i).getImagenId() !=null){

            usuarioImagenViewHolder.imagenI.setImageBitmap(listaUsuarios.get(i).getImagenId());


        }else{

            usuarioImagenViewHolder.imagenI.setImageResource(R.drawable.nodisponible);
        }



    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class UsuarioImagenViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenI;
        TextView tvDocumentoI,tvNombreI,tvProfesionI;


        public UsuarioImagenViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenI = (ImageView)itemView.findViewById(R.id.imagenI);
            tvDocumentoI = (TextView)itemView.findViewById(R.id.tvDocumentoI);
            tvNombreI = (TextView)itemView.findViewById(R.id.tvNombreI);
            tvProfesionI = (TextView)itemView.findViewById(R.id.tvProfesionI);



        }
    }
}
