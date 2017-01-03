package com.example.caboc.sqliteestudiantesprofesores;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

// activity que se encarga de recoger los datos que nos hagan falta para hacer una busqueda de usuarios en la bbdd
public class Buscador extends AppCompatActivity implements View.OnClickListener {

    RadioButton radioButtonEstudiante;
    RadioButton radioButtonProfesor;
    RadioButton radioButtonTodos;

    EditText editTextCiclo;
    EditText editTextCurso;

    Button boton;

    private MyDBAdapter dbAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        radioButtonEstudiante = (RadioButton) findViewById(R.id.radioButtonEstudiantes);
        radioButtonProfesor = (RadioButton) findViewById(R.id.radioButtonProfesores);
        radioButtonTodos = (RadioButton) findViewById(R.id.radioButtonTodos);

        editTextCiclo = (EditText) findViewById(R.id.editTextCicloBuscador);
        editTextCurso = (EditText) findViewById(R.id.editTextCursoBuscador);

        boton = (Button) findViewById(R.id.buttonQuery);
        boton.setOnClickListener(this);

        dbAdapter = new MyDBAdapter(this);
        dbAdapter.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {

        ArrayList<Usuario> usuarios;
        usuarios = new ArrayList<Usuario>();

        switch (v.getId()){
            case R.id.buttonQuery :

                String[] whereArgs = new String[2];

                // aqui guardo las clausulas para hacer el where si no elige nada el usuario pongo null
                if(!editTextCurso.getText().toString().equals("")){
                    whereArgs[0] = editTextCurso.getText().toString();
                }else{
                    whereArgs[0] = null;
                }

                if(!editTextCiclo.getText().toString().equals("")){
                    whereArgs[1] = editTextCiclo.getText().toString();
                }else{
                    whereArgs[1] = null;
                }


                // aqui relleno el arrayList con los usuario segun la query que realizemos a la bbdd sqlite
                if(radioButtonEstudiante.isChecked()){
                    usuarios = dbAdapter.buscarEstudiantes(whereArgs);

                }else if(radioButtonProfesor.isChecked()){

                    usuarios = dbAdapter.buscarProfesores(whereArgs);

                }else{
                    usuarios = dbAdapter.buscarTodos(whereArgs);
                }


                // aqui controlo que haya resultados a mostrar en el reciclerView, si no hay muestro un toast de error
                if(!usuarios.isEmpty()) {

                    Intent intent = new Intent(this, ReciclerViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("array", (Serializable) usuarios);
                    intent.putExtra("bundle", bundle);

                    startActivity(intent);
                }
                else{
                    Toast.makeText(Buscador.this, "No hay resultados", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

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
}
