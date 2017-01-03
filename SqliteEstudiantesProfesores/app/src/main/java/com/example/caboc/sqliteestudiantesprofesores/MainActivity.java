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
import android.widget.Toast;

// este activity es la pantalla de añadirEstudiante
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextNombreEstudiante;
    EditText editTextCursoEstudiante;
    EditText editTextEdadEstudiante;
    EditText editTextCicloEstudiante;
    EditText editTextNotaMediaEstudiante;

    Button botonAñadirEstudiante;

    private MyDBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCicloEstudiante = (EditText) findViewById(R.id.editTextCicloProfesor);
        editTextCursoEstudiante = (EditText) findViewById(R.id.editTextCursoEstudiante);
        editTextEdadEstudiante = (EditText) findViewById(R.id.editTextEdadEstudiante);
        editTextNombreEstudiante = (EditText) findViewById(R.id.editTextNombreEstudiante);
        editTextNotaMediaEstudiante = (EditText) findViewById(R.id.editTextDespachoProfesor);

        botonAñadirEstudiante = (Button) findViewById(R.id.botonAñadirEstudiante);
        botonAñadirEstudiante.setOnClickListener(this);

        dbAdapter = new MyDBAdapter(this);
        dbAdapter.open();
    }

    // creamos el options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.botonAñadirEstudiante:
                if(!editTextCicloEstudiante.getText().toString().equals("") && !editTextCursoEstudiante.getText().toString().equals("") && !editTextEdadEstudiante.getText().toString().equals("") && !editTextNombreEstudiante.getText().toString().equals("") && !editTextNotaMediaEstudiante.getText().toString().equals("")){
                    dbAdapter.insertarAlumno(editTextNombreEstudiante.getText().toString(),Integer.parseInt(editTextEdadEstudiante.getText().toString()), editTextCicloEstudiante.getText().toString(), Integer.parseInt(editTextCursoEstudiante.getText().toString()), Integer.parseInt(editTextNotaMediaEstudiante.getText().toString()));
                    Toast.makeText(MainActivity.this, "Alumno añadido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Faltan rellenar datos", Toast.LENGTH_SHORT).show();
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
