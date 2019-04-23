package com.centinela.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;


public class Settings {

    private DbHelper _dbHelper;

    public Settings(Context c) {
        _dbHelper = new DbHelper(c);
    }

    public void insertar(String titulo, String descripcion) throws DAOException {
        Log.i("SettingsDAO", "insertar()");
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            String[] args = new String[] {
                    titulo,
                    descripcion
            };
            db.execSQL("INSERT INTO settings(titulo, descripcion) VALUES(?,?)", args);
            Log.i("SettingsDAO", "Se insertó");
        } catch (Exception e) {
            throw new DAOException("SettingsDAO: Error al insertar: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public Settings obtener() throws DAOException {
        Log.i("SettingsDAO", "obtener()");
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        Settings modelo = new Settings(null);
        try {
            Cursor c = db.rawQuery("select id, titulo, descripcion from settings", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    int id = c.getInt(c.getColumnIndex("id"));
                    String titulo = c.getString(c.getColumnIndex("titulo"));
                    String descripcion = c.getString(c.getColumnIndex("descripcion"));
/*
                    modelo.setId(id);
                    modelo.setTitulo(titulo);
                    modelo.setDescripcion(descripcion);*/

                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            throw new DAOException("SettingsDAO: Error al obtener: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return modelo;
    }

    public ArrayList < Settings > buscar(String criterio) throws DAOException {
        Log.i("SettingsDAO", "buscar()");
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        ArrayList < Settings > lista = new ArrayList< Settings >();
        try {
            Cursor c = db.rawQuery("select id, titulo, descripcion from settings where titulo like '%" + criterio + "%' or descripcion like '%" + criterio + "%'", null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    int id = c.getInt(c.getColumnIndex("id"));
                    String titulo = c.getString(c.getColumnIndex("titulo"));
                    String descripcion = c.getString(c.getColumnIndex("descripcion"));

                 /*   Settings modelo = new Settings();
                   /* modelo.setId(id);
                    modelo.setTitulo(titulo);
                    modelo.setDescripcion(descripcion);*/

                    //lista.add(modelo);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            throw new DAOException("SettingsDAO: Error al obtener: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return lista;
    }

    public void eliminar(int id) throws DAOException {
        Log.i("SettingsDAO", "eliminar()");
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            String[] args = new String[] {
                    String.valueOf(id)
            };
            db.execSQL("DELETE FROM settings WHERE id=?", args);
        } catch (Exception e) {
            throw new DAOException("SettingsDAO: Error al eliminar: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void eliminarTodos() throws DAOException {
        Log.i("SettingsDAO", "eliminarTodos()");
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM settings");
        } catch (Exception e) {
            throw new DAOException("SettingsDAO: Error al eliminar todos: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

}