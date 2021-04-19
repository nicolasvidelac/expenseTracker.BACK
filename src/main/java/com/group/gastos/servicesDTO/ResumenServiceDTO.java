package com.group.gastos.servicesDTO;

import com.group.gastos.models.dtos.ItemDTO;
import com.group.gastos.models.dtos.ResumenDTO;
import com.group.gastos.services.CategoriaService;
import com.group.gastos.services.ResumenService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ResumenServiceDTO {

    private final ResumenService _resumenService;
    private final ModelMapper _modelMapper;
    private final CategoriaService _CategoriaService;



    public ResumenDTO findActiveResumen(String username) {
        ResumenDTO resumenDTO = _modelMapper.map(_resumenService.findActiveResumen(username), ResumenDTO.class);
        resumenDTO.setNombreMes((resumenDTO.getNombreMes()));

        for (ItemDTO itemDTO: resumenDTO.getItems()
             ) {
            itemDTO.setCategoria_desc(_CategoriaService.getDescripcion(itemDTO.getCategoria_id()));
        }

        return resumenDTO;
    }

    public ResumenDTO findByFechaInicio(String username, LocalDate localDate) {
        ResumenDTO resumenDTO = _modelMapper.map(_resumenService.findResumenByFechaInicio(localDate, username)
                , ResumenDTO.class);
        resumenDTO.setNombreMes(resumenDTO.getNombreMes());
        return resumenDTO;
    }

    public ResumenDTO updateResuen(String username, LocalDate localDate, Float nuevoMonto){
        return _modelMapper.map(_resumenService.updateResumen(username, localDate, nuevoMonto), ResumenDTO.class);
    }
}
