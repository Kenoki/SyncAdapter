package com.kenoki.app.syncadapter.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by aoki on 29/09/2015.
 */
public class ProviderDeGastos extends ContentProvider {
    /**
     * Nombre de la base de datos
     */
    private static final String DATABASE_NAME = "crunch_expenses.db";
    /**
     * Versión actual de la base de datos
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Instancia global del Content Resolver
     */
    private ContentResolver resolver;
    /**
     * Instancia del administrador de BD
     */
    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        // Inicializando gestor BD
        databaseHelper = new DatabaseHelper(
                getContext(),
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );

        resolver = getContext().getContentResolver();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

            // Obtener base de datos
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            // Comparar Uri
            int match = ContractParaGastos.uriMatcher.match(uri);

            Cursor c;

            switch (match) {
                case ContractParaGastos.ALLROWS:
                    // Consultando todos los registros
                    c = db.query(ContractParaGastos.GASTO, projection,
                            selection, selectionArgs,
                            null, null, sortOrder);
                    c.setNotificationUri(
                            resolver,
                            ContractParaGastos.CONTENT_URI);
                    break;
                case ContractParaGastos.SINGLE_ROW:
                    // Consultando un solo registro basado en el Id del Uri
                    long idGasto = ContentUris.parseId(uri);
                    c = db.query(ContractParaGastos.GASTO, projection,
                            ContractParaGastos.Columnas._ID + " = " + idGasto,
                            selectionArgs, null, null, sortOrder);
                    c.setNotificationUri(
                            resolver,
                            ContractParaGastos.CONTENT_URI);
                    break;
                default:
                    throw new IllegalArgumentException("URI no soportada: " + uri);
            }
            return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (ContractParaGastos.uriMatcher.match(uri)) {
            case ContractParaGastos.ALLROWS:
                return ContractParaGastos.MULTIPLE_MIME;
            case ContractParaGastos.SINGLE_ROW:
                return ContractParaGastos.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de gasto desconocido: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Validar la uri
        if (ContractParaGastos.uriMatcher.match(uri) != ContractParaGastos.ALLROWS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        // Inserción de nueva fila
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert(ContractParaGastos.GASTO, null, contentValues);
        if (rowId > 0) {
            Uri uri_gasto = ContentUris.withAppendedId(
                    ContractParaGastos.CONTENT_URI, rowId);
            resolver.notifyChange(uri_gasto, null, false);
            return uri_gasto;
        }
        throw new SQLException("Falla al insertar fila en : " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = ContractParaGastos.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case ContractParaGastos.ALLROWS:
                affected = db.delete(ContractParaGastos.GASTO,
                        selection,
                        selectionArgs);
                break;
            case ContractParaGastos.SINGLE_ROW:
                long idGasto = ContentUris.parseId(uri);
                affected = db.delete(ContractParaGastos.GASTO,
                        ContractParaGastos.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Elemento gasto desconocido: " +
                        uri);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;
        switch (ContractParaGastos.uriMatcher.match(uri)) {
            case ContractParaGastos.ALLROWS:
                affected = db.update(ContractParaGastos.GASTO, values,
                        selection, selectionArgs);
                break;
            case ContractParaGastos.SINGLE_ROW:
                String idGasto = uri.getPathSegments().get(1);
                affected = db.update(ContractParaGastos.GASTO, values,
                        ContractParaGastos.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        resolver.notifyChange(uri, null, false);
        return affected;
    }
}
