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

// activity añadirProfesor
public class ActivityAnadirProfesor extends AppCompatActivity implements View.OnClickListener{

    EditText editTextNombre;
    EditText editTextCurso;
    EditText editTextEdad;
    EditText editTextCiclo;
    EditText editTextDespacho;

    Button boton;

    private MyDBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_anadir_profesor);

        editTextNombre = (EditText) findViewById(R.id.editTextNombreProfesor);
        editTextCurso = (EditText) findViewById(R.id.editTextCursoProfesor);
        editTextEdad = (EditText) findViewById(R.id.editTextEdadProfesor);
        editTextCiclo = (EditText) findViewById(R.id.editTextCicloProfesor);
        editTextDespacho = (EditText) findViewById(R.id.editTextDespachoProfesor);

        boton = (Button) findViewById(R.id.botonAñadirProfesor);
        boton.setOnClickListener(this);

        dbAdapter = new MyDBAdapter(this);
        dbAdapter.open();
    }

    // para crear el optionsmenu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonAñadirProfesor:

                // String nombre, int edad, String ciclo,int curso,String despacho

                if(!editTextCiclo.getText().toString().equals("") && !editTextCurso.getText().toString().equals("") && !editTextEdad.getText().toString().equals("") && !editTextNombre.getText().toString().equals("") && !editTextDespacho.getText().toString().equals("")){
                    dbAdapter.insertarProfesor(editTextNombre.getText().toString(),Integer.parseInt(editTextEdad.getText().toString()), editTextCiclo.getText().toString(), Integer.parseInt(editTextCurso.getText().toString()),editTextDespacho.getText().toString());
                    Toast.makeText(ActivityAnadirProfesor.this, "Profesor añadido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this.getApplicationContext(), "Faltan rellenar datos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
