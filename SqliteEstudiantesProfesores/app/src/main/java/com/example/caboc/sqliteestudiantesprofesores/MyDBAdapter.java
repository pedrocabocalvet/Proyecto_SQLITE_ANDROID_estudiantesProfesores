package com.example.caboc.sqliteestudiantesprofesores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by caboc on 16/12/2016.
 */
public class MyDBAdapter {

    Context context;    // aqui guardamos el context del activity donde la llamamos

    // Constantes de la bbdd
    private static final String DATABASE_NAME = "colegio2.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLEESTUDIANTES = "estudiantes";
    private static final String DATABASE_TABLEPROFESORES = "profesores";

    // Clase SQLiteOpenHelper para crear/actualizar la base de datos
    private MyDbHelper dbHelper;

    // querys para lanzar a la bbdd
    private static final String TABLEESTUDIANTES_CREATE = "CREATE TABLE "+DATABASE_TABLEESTUDIANTES+" (_id integer primary key autoincrement, nombre text, edad integer, ciclo text, curso integer, notaMedia integer);";
    private static final String TABLEPROFESORES_CREATE = "CREATE TABLE "+ DATABASE_TABLEPROFESORES + "(_id integer primary key autoincrement, nombre text, edad integer, ciclo text, curso integer, despacho text);";
    private static final String TABLE_DROPESTUDIANTES = "DROP TABLE IF EXISTS "+DATABASE_TABLEESTUDIANTES+";";
    private static final String TABLE_DROPPROFESORES = "DROP TABLE IF EXISTS "+DATABASE_TABLEPROFESORES+";";

    // Instancia de la base de datos
    private SQLiteDatabase db;

    // CONSTANTES QUE REPRESENTAN LOS NOMBRES DE LAS TABLAS
    private static final String NOMBRE = "nombre";
    private static final String EDAD = "edad";
    private static final String CICLO = "ciclo";
    private static final String CURSO = "curso";
    private static final String NOTAMEDIA = "notaMedia";
    private static final String DESPACHO = "despacho";


    // constructor, cogemos el context del activity que nos llama y con el iniciamos el dbhelper
    public MyDBAdapter(Context c){
        this.context = c;
        dbHelper = new MyDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // aqui guardaremos en db una instancia de la bbdd la intentaremos abrir con escritura o solo con lectura
    public void open(){

        try{
            db = dbHelper.getWritableDatabase();
        }catch(SQLiteException e){
            db = dbHelper.getReadableDatabase();
        }

    }

    // funcion que se encarga de insertar alumnos en la bbdd

    public void insertarAlumno(String nombre, int edad, String ciclo,int curso,int notaMedia){
        //Creamos un nuevo registro de valores a insertar
        ContentValues newValues = new ContentValues();
        //Asignamos los valores de cada campo
        newValues.put(NOMBRE,nombre);
        newValues.put(EDAD,edad);
        newValues.put(CICLO,ciclo);
        newValues.put(CURSO,curso);
        newValues.put(NOTAMEDIA,notaMedia);
        db.insert(DATABASE_TABLEESTUDIANTES,null,newValues);
    }

    // funcion que se encarga de insertar profesores en la bbdd
    public void insertarProfesor(String nombre, int edad, String ciclo,int curso,String despacho){
        //Creamos un nuevo registro de valores a insertar
        ContentValues newValues = new ContentValues();
        //Asignamos los valores de cada campo
        newValues.put(NOMBRE,nombre);
        newValues.put(EDAD,edad);
        newValues.put(CICLO,ciclo);
        newValues.put(CURSO,curso);
        newValues.put(DESPACHO,despacho);
        db.insert(DATABASE_TABLEPROFESORES,null,newValues);
    }

    // funcion que borra un estudiante de su tabla mediante el id
    public boolean deleteEstudiante(int id)
    {
        return db.delete(DATABASE_TABLEESTUDIANTES, "_id =" + id, null) > 0;
    }

    // funcion que borra un profesor de su tabla mediante el id
    public boolean deleteProfesor(int id)
    {
        return db.delete(DATABASE_TABLEPROFESORES, "_id =" + id, null) > 0;
    }

    // funcion que borra la bbdd
    public boolean deleteDatabase(){
       return context.deleteDatabase(DATABASE_NAME);
    }

    // funcion que hace una busqueda de los estudiantes pasandole como parametros un array de condiciones where.
    // whereArgs[0] sera la condicion where para curso
    // whereArgs[1] sera la condicion where para ciclo
    public ArrayList<Usuario> buscarEstudiantes(String[] whereArgs){

        // preparamos el arrayList que devolveremos mas adelante
        ArrayList <Usuario> usuarios;
        usuarios = new ArrayList<>();

        // preparamos el String que sera el where para lanzar la query. ej: where ciclo = ?
        String whereClause = "";
        boolean pasar = true;   // servira para saber si tenemos que entrar en el segundo if o hemos cambiado el array por uno de un elemento

        // si le pasamos null en las dos posiciones del array parametro significa que queremos hacer una busqueda de todos los usuarios
        // sin clausula where
        if(whereArgs[0] == null && whereArgs[1] == null){

            whereArgs = null;
            whereClause = null;

        }else {     // si le hemos pasado alguna posicion del array parametro significa que como minimo hay una clausula where

            // hay una condicion where para curso
            if (whereArgs[0] != null) {
                whereClause = "curso = ? ";

                // este if sirve para saber si luego va ha haber otra condicion para curso y poder preparar el whereClause con un and
                if (whereArgs[1] != null) {

                    whereClause = whereClause + " and ";

                }else{  // si solo está esta condicion where preparamos el array whereArgs con un solo elemento para que no se lie cuando lanzamos la query

                    String temp = whereArgs[0];
                    whereArgs = new String[1];
                    whereArgs[0] = temp;
                    pasar = false;  // esto es para que no de problemas en el segundo if ya que hemos cambiado el array a un elemento
                }

            }
            // si ha pasado por el primer if y no tiene mas clausulas where significa que aqui habremos cambiado el array whereArgs a un
            // array con un solo elemento por lo cual no puede pasar de este if por que si no daria una excepcion al intentar acceder a la
            // posicion 1 de un array de un elemento
            if(pasar) {
                // si tiene segunda condicion where entramos aqui preparamos la query y en caso de que no hubiera habido condicion where uno
                // preparamos el array whereArgs a un array de una posicion para que no de problemas al lanzar la query
                if (whereArgs[1] != null) {

                    whereClause = whereClause + "ciclo = ? ";

                    if (whereArgs[0] == null) {
                        String temp = whereArgs[1];
                        whereArgs = new String[1];
                        whereArgs[0] = temp;
                    }
                }
            }

        }


        Cursor cursor;      // lo necesitaremos para recoger la query que lanzemos

        cursor =  db.query(DATABASE_TABLEESTUDIANTES,null,whereClause,whereArgs,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            // recorremos el cursor y cogemos las columnas que nos interesan para formar un usuario
            do {

                Usuario usuario = new Usuario(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getInt(4),""+cursor.getString(5),"Estudiante");
                usuarios.add(usuario);

            }while (cursor.moveToNext());   // se mueve a la siguiente posicion

        }


        return usuarios;

    }
    // funcion que hace una busqueda de los profesores pasandole como parametros un array de condiciones where.
    // whereArgs[0] sera la condicion where para curso
    // whereArgs[1] sera la condicion where para ciclo
    public ArrayList<Usuario> buscarProfesores(String[] whereArgs){

        ArrayList <Usuario> usuarios;
        usuarios = new ArrayList<>();

        // preparamos el String que sera el where para lanzar la query. ej: where ciclo = ?
        String whereClause = "";
        boolean pasar = true;   // servira para saber si tenemos que entrar en el segundo if o hemos cambiado el array por uno de un elemento

        // si le pasamos null en las dos posiciones del array parametro significa que queremos hacer una busqueda de todos los usuarios
        // sin clausula where
        if(whereArgs[0] == null && whereArgs[1] == null){

            whereArgs = null;
            whereClause = null;


        }else {     // si le hemos pasado alguna posicion del array parametro significa que como minimo hay una clausula where

            // hay una condicion where para curso
            if (whereArgs[0] != null) {
                whereClause = "curso = ? ";
                Log.d("prueba",whereClause);
                // este if sirve para saber si luego va ha haber otra condicion para curso y poder preparar el whereClause con un and
                if (whereArgs[1] != null) {

                    whereClause = whereClause + " and ";
                    Log.d("prueba",whereClause);
                }else{  // si solo está esta condicion where preparamos el array whereArgs con un solo elemento para que no se lie cuando lanzamos la query

                    String temp = whereArgs[0];
                    whereArgs = new String[1];
                    whereArgs[0] = temp;
                    pasar = false;  // esto es para que no de problemas en el segundo if ya que hemos cambiado el array a un elemento

                }

            }

            // si ha pasado por el primer if y no tiene mas clausulas where significa que aqui habremos cambiado el array whereArgs a un
            // array con un solo elemento por lo cual no puede pasar de este if por que si no daria una excepcion al intentar acceder a la
            // posicion 1 de un array de un elemento
            if(pasar) {

                // si tiene segunda condicion where entramos aqui preparamos la query y en caso de que no hubiera habido condicion where uno
                // preparamos el array whereArgs a un array de una posicion para que no de problemas al lanzar la query
                if (whereArgs[1] != null) {

                    whereClause = whereClause + "ciclo = ? ";
                    Log.d("prueba",whereClause);
                    if (whereArgs[0] == null) {

                        String temp = whereArgs[1];
                        whereArgs = new String[1];
                        whereArgs[0] = temp;

                    }
                }
            }

        }


        Cursor cursor;


        cursor =  db.query(DATABASE_TABLEPROFESORES,null,whereClause,whereArgs,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            // recorremos el cursor y cogemos las columnas que nos interesan para formar un usuario
            do {
                Usuario usuario = new Usuario(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getInt(4),cursor.getString(5),"Profesor");
                usuarios.add(usuario);

            }while (cursor.moveToNext());   // mueve a la siguiente posicion del cursor

        }


        return usuarios;

    }

    // funcion que hace una busqueda de los profesores y alumnos pasandole como parametros un array de condiciones where.
    // whereArgs[0] sera la condicion where para curso
    // whereArgs[1] sera la condicion where para ciclo
    public ArrayList<Usuario> buscarTodos(String[] whereArgs){
        ArrayList <Usuario> usuarios;
        ArrayList<Usuario> profesores;
        ArrayList<Usuario> estudiantes;

        profesores = this.buscarProfesores(whereArgs);
        estudiantes = this.buscarEstudiantes(whereArgs);

        usuarios = new ArrayList<>(estudiantes);
        usuarios.addAll(profesores);

        return usuarios;



    }

    // funcion que modifica el estudiante con id q le pasamos por parametro. devuelve las filas modificadas
    public int modificarEstudiante(int id, String nombreNuevo, int cursoNuevo, String cicloNuevo, int notaMediaNuevo, int edadNuevo){

        int rowAffected =0;
        ContentValues newValues = new ContentValues();

        newValues.put(NOMBRE,nombreNuevo);
        newValues.put(EDAD,edadNuevo);
        newValues.put(CICLO,cicloNuevo);
        newValues.put(CURSO,cursoNuevo);
        newValues.put(NOTAMEDIA,notaMediaNuevo);

        rowAffected = db.update(DATABASE_TABLEESTUDIANTES,newValues," _id="+ id,null);
        return rowAffected;
    }

    // funcion que modifica el profesor con id q le pasamos por parametro. devuelve las filas modificadas
    public int modificarProfesor(int id, String nombreNuevo, int cursoNuevo, String cicloNuevo, String despachoNuevo, int edadNuevo){

        int rowAffected =0;
        ContentValues newValues = new ContentValues();

        newValues.put(NOMBRE,nombreNuevo);
        newValues.put(EDAD,edadNuevo);
        newValues.put(CICLO,cicloNuevo);
        newValues.put(CURSO,cursoNuevo);
        newValues.put(DESPACHO,despachoNuevo);



        rowAffected = db.update(DATABASE_TABLEPROFESORES,newValues," _id="+ id,null);

        return rowAffected;
    }



    private static class MyDbHelper extends SQLiteOpenHelper {

        public MyDbHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context,name,factory,version);

        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_DROPESTUDIANTES);
            db.execSQL(TABLE_DROPPROFESORES);
            db.execSQL(TABLEESTUDIANTES_CREATE);
            db.execSQL(TABLEPROFESORES_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(TABLE_DROPESTUDIANTES);
            db.execSQL(TABLE_DROPPROFESORES);
            onCreate(db);
        }
    }
}
