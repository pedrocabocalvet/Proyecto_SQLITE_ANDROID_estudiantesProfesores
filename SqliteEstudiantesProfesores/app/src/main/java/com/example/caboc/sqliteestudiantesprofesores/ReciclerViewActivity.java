package com.example.caboc.sqliteestudiantesprofesores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class ReciclerViewActivity extends AppCompatActivity {

    ArrayList<Usuario> usuarios;
    RecyclerView rv;
    RecyclerView.LayoutManager rvLM;
    MyDBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recicler_view);



        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        usuarios = (ArrayList<Usuario>) args.getSerializable("array");



//        usuarios = new ArrayList<Usuario>();
//        usuarios.add(new Usuario(1,"pedro","estudiante"));
//        usuarios.add(new Usuario(2,"pedro2","estudiante"));
//        usuarios.add(new Usuario(3,"pedro3","Profesor"));



        rv = (RecyclerView) findViewById(R.id.elMeuRecyclerView);
        rvLM = new LinearLayoutManager(this);
        rv.setLayoutManager(rvLM);

        AdaptadorEstudiantesProfesores arVEP =  new AdaptadorEstudiantesProfesores(usuarios);
        rv.setAdapter(arVEP);

        dbAdapter = new MyDBAdapter(this);
        dbAdapter.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
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
