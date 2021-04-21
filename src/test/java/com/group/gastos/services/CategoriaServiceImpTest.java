package com.group.gastos.services;

import com.group.gastos.models.Categoria;
import com.group.gastos.repositories.CategoriaRepository;
import com.group.gastos.services.Intefaces.CategoriaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataMongoTest
class CategoriaServiceImpTest {

    @Mock
    CategoriaRepository categoriaRepository;

    AutoCloseable autoCloseable;
    CategoriaService categoriaService;

    Categoria c1 = new Categoria("1", "Transporte");
    Categoria c2 = new Categoria("2", "");

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        categoriaService = new CategoriaServiceImp(categoriaRepository);

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getDescripcionPositivo() {
        when(categoriaRepository.findById(c1.getId())).thenReturn(java.util.Optional.ofNullable(c1));

        String result = categoriaService.getDescripcion(c1.getId());
        assertEquals(result, c1.getDescripcion());
    }

    @Test
    void getDescripcionNegativo() {
        given(categoriaRepository.findById(c2.getId())).willThrow(RuntimeException.class);

        assertThatThrownBy(() -> categoriaService.getDescripcion(c2.getId()));
    }

    @Test
    void savePositivo() {
        categoriaService.save(c1);

        ArgumentCaptor<Categoria> categoriaArgumentCaptor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).save(categoriaArgumentCaptor.capture());
        Categoria categoria = categoriaArgumentCaptor.getValue();

        assertEquals(categoria, c1);
    }

    @Test
    void saveNegativo() {
        assertThatThrownBy(() -> categoriaService.save(c2)).hasMessage("descripcion cannot be empty");

    }
}