package com.kenoki.app.syncadapter.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aoki on 29/09/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context,
                          String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase database) {
        createTable(database); // Crear la tabla "gasto"
    }

    /**
     * Crear tabla en la base de datos
     *
     * @param database Instancia de la base de datos
     */
    private void createTable(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractParaGastos.GASTO + " (" +
                ContractParaGastos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaGastos.Columnas.MONTO + " TEXT, " +
                ContractParaGastos.Columnas.ETIQUETA + " TEXT, " +
                ContractParaGastos.Columnas.FECHA + " TEXT, " +
                ContractParaGastos.Columnas.DESCRIPCION + " TEXT," +
                ContractParaGastos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaGastos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaGastos.ESTADO_OK+"," +
                ContractParaGastos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try { db.execSQL("drop table " + ContractParaGastos.GASTO); }
        catch (SQLiteException e) { }
        onCreate(db);
    }

}
