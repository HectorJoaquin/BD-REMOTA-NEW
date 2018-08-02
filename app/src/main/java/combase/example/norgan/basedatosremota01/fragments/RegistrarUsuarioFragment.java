package combase.example.norgan.basedatosremota01.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import combase.example.norgan.basedatosremota01.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static android.support.v4.content.ContextCompat.getCodeCacheDir;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrarUsuarioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarUsuarioFragment extends Fragment {
    //implements Response.Listener<JSONObject>,Response.ErrorListener
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int COD_SELECCIONA = 10;

    //Variables para usar la camara web
    private static final String CARPETA_PRINCIPAL="misImagenesApp/"; //directorio principal
    private static final String CARPETA_IMAGEN="imagenes/"; //carpeta donde se guardaran las imagens tomadas
    private static final String DIRECTORIO_IMAGEN=CARPETA_PRINCIPAL + CARPETA_IMAGEN; //ruta dde la carpeta de directorios
    private String path; //almacena la ruta de la imagen
    File fileImagen; //los dos se usan para al momento de tomar la foto el que esta abajo tambien
    Bitmap bitmap;
    private static final int COD_FOTO = 20;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    EditText etDocumento,etNombre,etProfesion;
    Button btnRegistrar;
    ImageView imgFoto;
    Button btnFoto;

    //permite la conexion con nuestro web service METODO GET
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    //METODO POST
    StringRequest stringRequest;


    public RegistrarUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarUsuarioFragment newInstance(String param1, String param2) {
        RegistrarUsuarioFragment fragment = new RegistrarUsuarioFragment();
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

        View vista = inflater.inflate(R.layout.fragment_registrar_usuario, container, false);

        etDocumento = (EditText)vista.findViewById(R.id.etDocumento);
        etNombre = (EditText)vista.findViewById(R.id.etNombre);
        etProfesion = (EditText)vista.findViewById(R.id.etProfesion);
        btnRegistrar =  (Button)vista.findViewById(R.id.btnRegistrar);
        imgFoto = (ImageView)vista.findViewById(R.id.imgFoto);
        btnFoto =(Button)vista.findViewById(R.id.btnFoto);

        requestQueue = Volley.newRequestQueue(getContext());




        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostrarDialoOpciones();

            }
        });


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cargarweb();

            }
        });

        return vista;
    }





    private void mostrarDialoOpciones() {

        //Toast.makeText(getContext(), "MOSTRAR OPCIONES", Toast.LENGTH_SHORT).show();
        final CharSequence [] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elegir una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (opciones[i].equals("Tomar Foto")){
                    //llamado al metodo para activar la camara
                   Toast.makeText(getContext(), "Cargar Camara, Desactivada", Toast.LENGTH_SHORT).show();
                   abrirCamara();

                }else{
                    if (opciones[i].equals("Elegir de Galeria")){
                        //action_get_content es como muetra la busqueda de las foto pero se puede cmbiar por ACTION_PICK

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);


                    }else {
                        dialogInterface.dismiss();
                    }

                }



            }
        });

        builder.show();

    }

    private void abrirCamara() {


        File miFile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (isCreada ==false){

            isCreada=miFile.mkdirs();
        }
        if (isCreada==true){
            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString()+".jpg";

            path = Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+File.separator+nombre;
            fileImagen = new File(path);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));
            startActivityForResult(intent,COD_FOTO);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case COD_SELECCIONA:

                Uri miPath = data.getData();
                imgFoto.setImageURI(miPath);

                try {
                    bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                    imgFoto.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;


            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path"," "+path);
                            }
                        });

                bitmap= BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);

                break;

        }

    }

    private void cargarweb() {

        //String url = "http://192.168.1.41/BDRemota/wsJSONRegistro.php?documento="+etDocumento.getText().toString()+"&nombre="+etNombre.getText().toString()+"&profesion="+etProfesion.getText().toString();
        //url = url.replace(" ","%20");
        //jsonObjectRequest =  new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        //requestQueue.add(jsonObjectRequest);

        String url = "http://192.168.1.41/BDRemota/wsJSONRegistroMovil.php?";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.trim().equalsIgnoreCase("registra")){

                    etDocumento.setText("");
                    etNombre.setText("");
                    etProfesion.setText("");

                    Toast.makeText(getContext(), "Seleccion exitoso", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(getContext(), "Nose a registratdo ", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "NO SE PUDO CONECTAR AL WEB SERVICE", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                String documento = etDocumento.getText().toString();
                String nombre = etNombre.getText().toString();
                String profesion = etProfesion.getText().toString();


                String imagen = convertirImgString(bitmap);


                Map<String,String> parametros = new HashMap<>();
                parametros.put("documento",documento);
                parametros.put("nombre",nombre);
                parametros.put("profesion",profesion);
                parametros.put("imagen",imagen);


                return parametros;
            }
        };

requestQueue.add(stringRequest);

    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
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

    //@Override
    //public void onErrorResponse(VolleyError error) {


    //  Toast.makeText(getContext(), "No se pudo registrar"+error.toString(), Toast.LENGTH_SHORT).show();
    //  Log.i("ERROR",error.toString());

    // }

    //@Override
    //public void onResponse(JSONObject response) {


    //  Toast.makeText(getContext(), "Correctamente Ingresado", Toast.LENGTH_SHORT).show();



    ////  etDocumento.setText("");
    //etNombre.setText("");
    //  etProfesion.setText("");


    //}

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
