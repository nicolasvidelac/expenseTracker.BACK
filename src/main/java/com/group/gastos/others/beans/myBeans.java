package com.group.gastos.others.beans;

import com.group.gastos.models.EstadoResumen;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class myBeans {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public EstadoResumen getEstadoActivo(){
        return new EstadoResumen("1", "activo");
    }

//    @Bean
//    public EstadoResumen getEstadoInactivo(){
//        return new EstadoResumen("2", "inactivo");
//    }

}
