package com.libreria.enums;

public enum RolEnum {

    USER,
    ADMIN,
    INVITED;

    // Método auxiliar para obtener el rol con prefijo
    public String getRoleWithPrefix() {
        return "ROLE_" + this.name();
    }
}
