package com.kenoki.app.syncadapter.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aoki on 29/09/2015.
 */
public class ContractParaGastos {

    /**
     * Autoridad del Content Provider
     */
    public final static String AUTHORITY
            = "com.herprogramacion.crunch_expenses";
    /**
     * Representación de la tabla a consultar
     */
    public static final String GASTO = "gasto";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + GASTO;
    /**
     * Tipo MIME que retorna la consulta de {@link CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + GASTO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + GASTO);
    /**
     * Comparador de URIs de contenido
     */
    public static final UriMatcher uriMatcher;
    /**
     * Código para URIs de multiples registros
     */
    public static final int ALLROWS = 1;
    /**
     * Código para URIS de un solo registro
     */
    public static final int SINGLE_ROW = 2;


    // Asignación de URIs
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, GASTO, ALLROWS);
        uriMatcher.addURI(AUTHORITY, GASTO + "/#", SINGLE_ROW);
    }

    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String MONTO = "monto";
        public final static String ETIQUETA = "etiqueta";
        public final static String FECHA = "fecha";
        public final static String DESCRIPCION = "descripcion";

        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }

}
