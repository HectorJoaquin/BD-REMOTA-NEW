package combase.example.norgan.basedatosremota01.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import combase.example.norgan.basedatosremota01.R;
import combase.example.norgan.basedatosremota01.entidades.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultarUsuarioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultarUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarUsuarioFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText etDocumentoConsultar;
    TextView etNombreConsultar,etPorfesionConsultar;
    Button btnConsultar;
    ImageView imagenId;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    private OnFragmentInteractionListener mListener;

    public ConsultarUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultarUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarUsuarioFragment newInstance(String param1, String param2) {
        ConsultarUsuarioFragment fragment = new ConsultarUsuarioFragment();
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

        View vista =inflater.inflate(R.layout.fragment_consultar_usuario, container, false);

        etDocumentoConsultar = (EditText)vista.findViewById(R.id.etDocumentoConsulta);
        etNombreConsultar = (TextView) vista.findViewById(R.id.etNombreConsulta);
        etPorfesionConsultar = (TextView) vista.findViewById(R.id.etPrfecionConsulta);
        btnConsultar = (Button)vista.findViewById(R.id.btnConsultar);
        imagenId = (ImageView)vista.findViewById(R.id.imagenId);


        requestQueue = Volley.newRequestQueue(getContext());

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cargarWebServices();

            }
        });

        return vista;
    }

    private void cargarWebServices() {


        String url = "http://192.168.1.41/BDRemota/wsJSONConsultarUsuarioImagen.php?documento="+etDocumentoConsultar.getText().toString();

        jsonObjectRequest =  new JsonObjectRequest(Request.Method.GET,url,null,this,this);

        requestQueue.add(jsonObjectRequest);



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



        Toast.makeText(getContext(), "No se pudo Consultar"+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {

        Toast.makeText(getContext(), "Mensaje "+response, Toast.LENGTH_SHORT).show();


        Usuario miusuario = new Usuario();

        JSONArray jsonArray = response.optJSONArray("usuario");
        JSONObject jsonObject =  null;


        try {

            jsonObject = jsonArray.getJSONObject(0);
            miusuario.setNombre(jsonObject.optString("nombre"));
            miusuario.setProfesion(jsonObject.optString("profesion"));
            miusuario.setDato(jsonObject.optString("imagen"));


        } catch (JSONException e) {
            e.printStackTrace();
        }



        etNombreConsultar.setText(miusuario.getNombre());
        etPorfesionConsultar.setText(miusuario.getProfesion());

        if (miusuario.getImagenId() !=null){

            imagenId.setImageBitmap(miusuario.getImagenId());
        }else{

            imagenId.setImageResource(R.drawable.nodisponible);
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
