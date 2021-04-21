package com.group.gastos.others.enums;

import lombok.Getter;

@Getter
public enum MesesEnum {
    JANUARY("Enero"),
    FEBRUARY("Febrero"),
    MARCH("Marzo"),
    APRIL("Abril"),
    MAY("Mayo"),
    JUNE("Junio"),
    JULY("Julio"),
    AUGUST("Agosto"),
    SEPTEMBER("Septiembre"),
    OCTOBER("Octubre"),
    NOVEMBER("Noviembre"),
    DECEMBER("Diciembre");

    private final String label;

    MesesEnum(String label) {
        this.label = label;
    }
}
