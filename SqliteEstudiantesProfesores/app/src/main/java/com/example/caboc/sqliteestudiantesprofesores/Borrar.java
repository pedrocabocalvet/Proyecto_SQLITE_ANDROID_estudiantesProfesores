package com.example.caboc.sqliteestudiantesprofesores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class Borrar extends AppCompatActivity implements View.OnClickListener{

    TextView nombre;
    EditText editTextNombre;
    Button boton;

    Bundle bundle;  // lo usaremos para coger el bundle del intent y saber si vamos a borrar un estudiante o un profesor
    Boolean estudiante;     // para saber si vamos a borrar un estudiante o un profesor

    private MyDBAdapter dbAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);

        nombre = (TextView) findViewById(R.id.LabelNombreBorrar);
        editTextNombre = (EditText) findViewById(R.id.editTextIdBorrar);
        boton = (Button) findViewById(R.id.buttonBorrar);
        boton.setOnClickListener(this);

        bundle = getIntent().getExtras();

        // dependiendo si queremos borrar un estudiante o un profesor haremos una cosa o otra
        if(bundle.getString("usuario_a_borrar").equals("estudiante")){
            nombre.setText("Introduce el id del Estudiante a borrar");
            estudiante = true;
        }else{
            nombre.setText("Introduce el id del Profesor a borrar");
            estudiante = false;
        }

        dbAdapter = new MyDBAdapter(this);
        dbAdapter.open();
    }

    // creamos el options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonBorrar :

                if(!editTextNombre.getText().toString().equals("") && isNumeric(editTextNombre.getText().toString())) {
                    if (estudiante) {
                        if(dbAdapter.deleteEstudiante(Integer.parseInt(editTextNombre.getText().toString())))
                             Toast.makeText(Borrar.this, "Has borrado el estudiante", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Borrar.this, "No se pudo borrar", Toast.LENGTH_SHORT).show();
                    } else {
                        if(dbAdapter.deleteProfesor(Integer.parseInt(editTextNombre.getText().toString())))
                            Toast.makeText(Borrar.this, "Has borrado el Profesor", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Borrar.this, "No se pudo borrar", Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(Borrar.this, "No se pudo borrar", Toast.LENGTH_SHORT).show();

                editTextNombre.setText("");

                break;
        }
    }

    // para ir a los distintos activitys
    @Override
    // para ir a los distintos activitys
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        Bundle bundle;
        switch (item.getItemId()) {

            case R.id.añadirEstudiante:

                intent = new Intent(this,MainActivity.class);
                startActivity(intent);

                break;

            case R.id.añadirProfesor:

                intent = new Intent(this,ActivityAnadirProfesor.class);
                startActivity(intent);

                break;

            case R.id.borrarEstudiante :

                intent = new Intent(this,Borrar.class);
                bundle = new Bundle();
                bundle.putString("usuario_a_borrar","estudiante");
                intent.putExtras(bundle);
                startActivity(intent);

                break;

            case R.id.borrarProfesor :

                intent = new Intent(this,Borrar.class);
                bundle = new Bundle();
                bundle.putString("usuario_a_borrar","profesor");
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.borrarBBDD :
                if(dbAdapter.deleteDatabase())
                    Toast.makeText(getApplicationContext(), "BBDD borrada", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "No se ha podido borrar la BBDD", Toast.LENGTH_SHORT).show();
                break;

            case R.id.buscador :
                intent = new Intent(this,Buscador.class);
                startActivity(intent);
                break;

        }

        return true;
    }

    // funcion que averigua si un String es un numero o no
    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
}
