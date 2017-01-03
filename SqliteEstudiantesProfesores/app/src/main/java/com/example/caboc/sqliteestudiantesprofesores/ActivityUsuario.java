package com.example.caboc.sqliteestudiantesprofesores;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityUsuario extends AppCompatActivity implements View.OnClickListener{
    EditText id;
    EditText nombre;
    EditText curso;
    EditText ciclo;
    EditText extra;
    EditText edad;
    EditText cargo;
    TextView labelExtra;
    Button botonSalir;
    Button botonModificar;
    ImageView foto;


    // lo usaremos para guardar los valores y saber cuales ha modificado el usuario
    String nombreAntiguo;
    String cursoAntiguo;
    String cicloAntiguo;
    String extraAntiguo;
    String edadAntiguo;
    String cargoAntiguo;

    private MyDBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_usuario);

        id = (EditText) findViewById(R.id.editTextIDusuarioAct);
        nombre = (EditText) findViewById(R.id.editTextNombreUsuarioAct);
        curso = (EditText) findViewById(R.id.editTextCursoUsuarioAct);
        ciclo = (EditText) findViewById(R.id.editTextCicloUsuarioAct);
        extra = (EditText) findViewById(R.id.editTextExtraUsuarioAct);
        edad = (EditText) findViewById(R.id.editTextEdadUsuarioActivity);
        cargo = (EditText) findViewById(R.id.editTextCargoUsuarioAct);
        foto = (ImageView) findViewById(R.id.imageViewUsuarioActivity);

        labelExtra = (TextView) findViewById(R.id.textViewExtraUsuarioAct);

        botonModificar = (Button) findViewById(R.id.buttonModificar);
        botonSalir = (Button) findViewById(R.id.buttonSalir);

        botonModificar.setOnClickListener(this);
        botonSalir.setOnClickListener(this);
        //  aqui asignamos los valores del usuario seleccionado en cada uno de los views del activity
        Bundle bundle = getIntent().getExtras();
        id.setText(bundle.getString("id"));
        nombre.setText(bundle.getString("nombre"));
        curso.setText(bundle.getString("curso"));
        ciclo.setText(bundle.getString("ciclo"));
        extra.setText(bundle.getString("extra"));
        edad.setText(bundle.getString("edad"));
        cargo.setText(bundle.getString("cargo"));

        // estos dos elementos estaran desactivados siempre para que el usuario no pueda modificarlos
        id.setEnabled(false);
        cargo.setEnabled(false);

        // simplemente cambio el texto de un label en funcion de si es profesor o estudiante
        if(cargo.getText().toString().equals("Estudiante")){
            labelExtra.setText("Nota");
            foto.setImageResource(R.drawable.estudiante);
        }else if(cargo.getText().toString().equals("Profesor")){
            labelExtra.setText("Despacho");
            foto.setImageResource(R.drawable.usuario);
        }else{
            labelExtra.setText("extra");
        }

        // si usamos el activity a forma de ver ponemos los campos desactivados y quitamos el boton modificar
        if(getIntent().getExtras().getString("opcion").equals("ver")){

            nombre.setEnabled(false);
            curso.setEnabled(false);
            ciclo.setEnabled(false);
            extra.setEnabled(false);
            edad.setEnabled(false);



            botonModificar.setVisibility(View.INVISIBLE);
        }else{  // si usamos el activity en forma de modificar guardamos los valores que tenia para saber cuales ha modificado el usuario

            nombreAntiguo = nombre.getText().toString();
            cursoAntiguo = curso.getText().toString();
            cicloAntiguo = ciclo.getText().toString();
            extraAntiguo = extra.getText().toString();
            edadAntiguo = edad.getText().toString();
            cargoAntiguo = cargo.getText().toString();


        }

        dbAdapter = new MyDBAdapter(this);
        dbAdapter.open();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSalir :
                finish();
                break;

            case R.id.buttonModificar :
                // si los campos que hay son iguales que los que habia no hace falta modificar nada
                if(nombre.getText().toString().equals(nombreAntiguo) && curso.getText().toString().equals(cursoAntiguo) && ciclo.getText().toString().equals(cicloAntiguo) && extra.getText().toString().equals(extraAntiguo) && edad.getText().toString().equals(edadAntiguo) && cargo.getText().toString().equals(cargoAntiguo)){
                    Toast.makeText(ActivityUsuario.this, "No has modificado ningun campo", Toast.LENGTH_SHORT).show();
                }else{  // significa que algun campo ha sido modificado

                    int columnasAfectadas = 0;

                    int idModificar = Integer.parseInt(id.getText().toString());
                    String nombreNuevo = nombre.getText().toString();
                    int cursoNuevo = Integer.parseInt(curso.getText().toString());
                    String cicloNuevo = ciclo.getText().toString();
                    String extraNuevo = extra.getText().toString(); // en caso de estudiante sera int y en caso de profesor string
                    int edadNuevo = Integer.parseInt(edad.getText().toString());
                    String cargoNuevo = cargo.getText().toString();


                    // hago la modificacion en funcion de si es un estudiante o un profesor
                    if(cargoNuevo.equals("Estudiante")){
                        columnasAfectadas = dbAdapter.modificarEstudiante(idModificar,nombreNuevo,cursoNuevo,cicloNuevo, Integer.parseInt(extraNuevo), edadNuevo);

                    }else{
                        columnasAfectadas = dbAdapter.modificarProfesor(idModificar,nombreNuevo,cursoNuevo,cicloNuevo, extraNuevo, edadNuevo);
                    }

                    // en el caso de que haya filas modificadas significa que el usuario ha sido modificado por lo tanto muestro el toast
                    if(columnasAfectadas != 0){
                        Toast.makeText(ActivityUsuario.this, "Usuario modificado", Toast.LENGTH_SHORT).show();

                    }
                }

                break;
        }
    }
}
