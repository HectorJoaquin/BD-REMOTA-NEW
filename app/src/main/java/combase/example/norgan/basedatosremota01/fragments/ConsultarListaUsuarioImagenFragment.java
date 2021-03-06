package combase.example.norgan.basedatosremota01.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import combase.example.norgan.basedatosremota01.R;
import combase.example.norgan.basedatosremota01.adapter.UsuarioAdapter;
import combase.example.norgan.basedatosremota01.adapter.UsuarioImagenAdapter;
import combase.example.norgan.basedatosremota01.entidades.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultarListaUsuarioImagenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultarListaUsuarioImagenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarListaUsuarioImagenFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerImagen;
    ArrayList<Usuario> listaUsuarios;
    RequestQueue requestQueue;
    JsonObjectRequest json;

    public ConsultarListaUsuarioImagenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultarListaUsuarioImagenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarListaUsuarioImagenFragment newInstance(String param1, String param2) {
        ConsultarListaUsuarioImagenFragment fragment = new ConsultarListaUsuarioImagenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista=inflater.inflate(R.layout.fragment_consultar_lista_usuario_imagen, container, false);

        listaUsuarios = new ArrayList<>();
        recyclerImagen = (RecyclerView)vista.findViewById(R.id.recyclerImagen);
        recyclerImagen.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerImagen.setHasFixedSize(true);

        requestQueue = Volley.newRequestQueue(getContext());

        cargarImagenesWeb();


        return vista;
    }

    private void cargarImagenesWeb() {


        String url ="http://192.168.1.41/BDRemota/wsJSONConsultarListaImagenes.php";
        json =  new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(json);





    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {




    }

    @Override
    public void onResponse(JSONObject response) {




        Toast.makeText(getContext(), "Mensaje "+response, Toast.LENGTH_SHORT).show();

        Usuario usuario = null;

        JSONArray json =  response.optJSONArray("usuario");

        try {
            for (int i = 0; i < json.length(); i++) {

                usuario = new Usuario();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                usuario.setDocumento(jsonObject.optInt("documento"));
                usuario.setNombre(jsonObject.optString("nombre"));
                usuario.setProfesion(jsonObject.optString("profesion"));
                usuario.setDato(jsonObject.optString("imagen"));
                listaUsuarios.add(usuario);


            }

            UsuarioImagenAdapter adapter = new UsuarioImagenAdapter(listaUsuarios);
            recyclerImagen.setAdapter(adapter);

        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();


        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
