package model;

import java.util.*;

/** Tipos de licencia y su jerarquía, según el análisis. */
public enum Licencia {
    A("AUTOMOTOR", 3), B("AUTOMOTOR", 2), C("AUTOMOTOR", 1), M("MOTO", 1);
    private String familia;
    private int nivel;
    private Licencia(String familia, int nivel) { this.familia = familia; this.nivel = nivel; }
    public boolean autoriza(Licencia requerida) {
        return requerida != null && familia.equals(requerida.familia) && nivel >= requerida.nivel;
    }
    public static Licencia desdeTexto(String texto) {
        if (texto == null) throw new IllegalArgumentException("Licencia inválida: use A, B, C o M.");
        try { return valueOf(texto.trim().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("Licencia inválida: use A, B, C o M."); }
    }
}
