package com.group.gastos.services;

import com.group.gastos.models.*;
import com.group.gastos.others.others.ResumenUtils;
import com.group.gastos.repositories.CategoriaRepository;
import com.group.gastos.repositories.ResumenRepository;
import com.group.gastos.repositories.UsuarioRepository;
import com.group.gastos.services.Intefaces.ItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemServiceImpTest {

    AutoCloseable autoCloseable;
    ItemService _itemService;
    Categoria ca1 = new Categoria("1", "Comida");
    Item it1 = new Item("1", "item1", LocalDate.now(), 1500F, true, 1,
            0, ca1.getId());
    Usuario us1 = new Usuario("1", "username", "user name", "password", 0F,
            false, true);
    Resumen rs1 = new Resumen("1", 0F, 0F, LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1), "mes", 0F, us1.getId(),
            new EstadoResumen("1", "activo"), new ArrayList<Item>() {
    });
    Resumen rs2 = new Resumen("2", 0F, 0F, LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1), "mes", 0F, us1.getId(),
            new EstadoResumen("1", "inactivo"), new ArrayList<Item>() {
    });
    @Mock
    private ResumenRepository resumenRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    private final ResumenUtils resumenUtils = new ResumenUtils();

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        _itemService = new ItemServiceImp(resumenRepository, usuarioRepository, categoriaRepository, resumenUtils);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveItemPositivo() {
        when(usuarioRepository.findByUsername(us1.getUsername())).thenReturn(Optional.ofNullable(us1));
        when(resumenRepository.findAll()).thenReturn(Collections.singletonList(rs1));
        when(categoriaRepository.findById(ca1.getId())).thenReturn(Optional.ofNullable(ca1));
        rs1.getItems().add(it1);
        when(resumenRepository.save(rs1)).thenReturn(rs1);
        _itemService.saveItem(us1.getUsername(), it1);

        ArgumentCaptor<Resumen> categoriaArgumentCaptor = ArgumentCaptor.forClass(Resumen.class);
        verify(resumenRepository).save(categoriaArgumentCaptor.capture());
        Resumen resumen = categoriaArgumentCaptor.getValue();

        assertEquals(resumen.getItems().stream().findFirst().orElseThrow(), it1);
    }

    @Test
    void saveItemNegativoItemSinDescripcion() {
        it1.setDescripcion("");
        assertThatThrownBy(() -> _itemService.saveItem(us1.getUsername(), it1)).hasMessage("Invalid item attributes");
    }


    @Test
    void saveItemNegativoUsuarioNoExiste() {
        when(usuarioRepository.findByUsername(us1.getUsername())).thenThrow(UsernameNotFoundException.class);
        when(categoriaRepository.findById(ca1.getId())).thenReturn(Optional.ofNullable(ca1));

        assertThatThrownBy(() -> _itemService.saveItem(us1.getUsername(), it1)).hasMessage("User not found");
    }

    @Test
    void saveItemNegativoResumenNoExiste() {
        when(categoriaRepository.findById(ca1.getId())).thenReturn(Optional.ofNullable(ca1));

        when(usuarioRepository.findByUsername(us1.getUsername())).thenReturn(Optional.ofNullable(us1));
        when(resumenRepository.findAll()).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> _itemService.saveItem(us1.getUsername(), it1)).hasMessage("Resumen not found");
    }

    @Test
    void saveItemNegativoCategoriaNoExiste() {
        when(categoriaRepository.findById(ca1.getId())).thenThrow(NoSuchElementException.class);

        assertThatThrownBy(() -> _itemService.saveItem(us1.getUsername(), it1)).hasMessage("Category not found");
    }

    @Test
    void updateItemPositivo() {
        rs1.getItems().add(it1);
        when(usuarioRepository.findByUsername(us1.getUsername())).thenReturn(Optional.ofNullable(us1));
        when(resumenRepository.findAll()).thenReturn(Collections.singletonList(rs1));
        when(categoriaRepository.findById(ca1.getId())).thenReturn(Optional.ofNullable(ca1));
        when(resumenRepository.save(rs1)).thenReturn(rs1);
        _itemService.updateItem(us1.getUsername(), it1.getId(), it1);

        ArgumentCaptor<Resumen> categoriaArgumentCaptor = ArgumentCaptor.forClass(Resumen.class);
        verify(resumenRepository).save(categoriaArgumentCaptor.capture());
        Resumen resumen = categoriaArgumentCaptor.getValue();

        assertEquals(resumen.getItems().stream().findFirst().orElseThrow(), it1);
    }

    @Test
    void updateItemNegativoItemSinCategoria() {
        it1.setCategoria_id("");
        assertThatThrownBy(() -> _itemService.updateItem(us1.getUsername(), it1.getId(), it1)).hasMessage("Invalid item attributes");
    }

    @Test
    void updateItemNegativoUsuarioNoExiste() {
        when(usuarioRepository.findByUsername(us1.getUsername())).thenThrow(UsernameNotFoundException.class);
        when(categoriaRepository.findById(ca1.getId())).thenReturn(Optional.ofNullable(ca1));

        assertThatThrownBy(() -> _itemService.saveItem(us1.getUsername(), it1)).hasMessage("User not found");
    }

    @Test
    void updateItemNegativoResumenNoExiste() {
        when(categoriaRepository.findById(ca1.getId())).thenReturn(Optional.ofNullable(ca1));

        when(usuarioRepository.findByUsername(us1.getUsername())).thenReturn(Optional.ofNullable(us1));
        when(resumenRepository.findAll()).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> _itemService.saveItem(us1.getUsername(), it1)).hasMessage("Resumen not found");
    }

    @Test
    void updateItemNegativoCategoriaNoExiste() {
        when(categoriaRepository.findById(ca1.getId())).thenThrow(NoSuchElementException.class);

        assertThatThrownBy(() -> _itemService.saveItem(us1.getUsername(), it1)).hasMessage("Category not found");
    }

    @Test
    void getItemPositivo() {
        rs1.getItems().add(it1);
        when(usuarioRepository.findByUsername(us1.getUsername())).thenReturn(Optional.ofNullable(us1));
        when(resumenRepository.findAll()).thenReturn(Arrays.asList(rs2, rs1));

        assertEquals(_itemService.getItem(us1.getUsername(), it1.getId()), it1);
    }

    @Test
    void getItemNegativoNoExiste() {
        when(usuarioRepository.findByUsername(us1.getUsername())).thenReturn(Optional.ofNullable(us1));
        when(resumenRepository.findAll()).thenReturn(Collections.singletonList(rs1));

        assertThatThrownBy(() -> _itemService.getItem(us1.getUsername(), it1.getId())).hasMessage("Item not found");
    }

    @Test
    void deleteItemPositivo() {
        rs1.getItems().add(it1);
        rs1.setTotalGasto(it1.getMonto());
        when(usuarioRepository.findByUsername(us1.getUsername())).thenReturn(Optional.ofNullable(us1));
        when(resumenRepository.findAll()).thenReturn(Arrays.asList(rs2, rs1));

        _itemService.deleteItem(us1.getUsername(), it1.getId());
        ArgumentCaptor<Resumen> categoriaArgumentCaptor = ArgumentCaptor.forClass(Resumen.class);
        verify(resumenRepository).save(categoriaArgumentCaptor.capture());
        Resumen resumen = categoriaArgumentCaptor.getValue();

        assertEquals(0F, resumen.getTotalGasto());
        assertEquals(new ArrayList<>(), resumen.getItems());
    }

    @Test
    void deleteItemNegativo() {
        when(usuarioRepository.findByUsername(us1.getUsername())).thenReturn(Optional.ofNullable(us1));
        when(resumenRepository.findAll()).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> _itemService.deleteItem(us1.getUsername(), it1.getId()))
                .hasMessage("Item not found");
    }
}