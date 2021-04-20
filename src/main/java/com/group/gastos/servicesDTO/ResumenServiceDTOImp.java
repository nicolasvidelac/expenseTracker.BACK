package com.group.gastos.servicesDTO;

import com.group.gastos.models.Resumen;
import com.group.gastos.models.dtos.ItemDTO;
import com.group.gastos.models.dtos.ResumenDTO;
import com.group.gastos.services.Intefaces.CategoriaService;
import com.group.gastos.services.Intefaces.ResumenService;
import com.group.gastos.servicesDTO.Interfaces.ResumenServiceDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ResumenServiceDTOImp implements ResumenServiceDTO {

    private final ResumenService _resumenService;
    private final ModelMapper _modelMapper;
    private final CategoriaService _CategoriaService;

    public ResumenDTO findActiveResumen(String username) throws IOException, InterruptedException {
        ResumenDTO resumenDTO = _modelMapper.map(_resumenService.findActiveResumen(username), ResumenDTO.class);
        resumenDTO.setNombreMes((resumenDTO.getNombreMes()));

        for (ItemDTO itemDTO: resumenDTO.getItems()
             ) {
            itemDTO.setCategoria_desc(_CategoriaService.getDescripcion(itemDTO.getCategoria_id()));
        }
        return resumenDTO;
    }

    public List<ResumenDTO> findAllResumenes(String username) throws IOException, InterruptedException {
        List<Resumen> resumenes = _resumenService.findAllResumenes(username);
        List<ResumenDTO> resumenesDTO = new ArrayList<>();

        for (Resumen resumen: resumenes){
            ResumenDTO resumenDTO = _modelMapper.map(resumen, ResumenDTO.class);
            resumenDTO.setNombreMes((resumenDTO.getNombreMes()));
            resumenesDTO.add(resumenDTO);
        }

        return resumenesDTO;
    }

    public ResumenDTO findByFechaInicio(String username, LocalDate localDate) throws IOException, InterruptedException {
        ResumenDTO resumenDTO = _modelMapper.map(_resumenService.findResumenByFechaInicio(localDate, username)
                , ResumenDTO.class);
        resumenDTO.setNombreMes(resumenDTO.getNombreMes());
        return resumenDTO;
    }

    public ResumenDTO updateResumen(String username, LocalDate localDate, Float nuevoMonto){
        return _modelMapper.map(_resumenService.updateResumen(username, localDate, nuevoMonto), ResumenDTO.class);
    }
}
