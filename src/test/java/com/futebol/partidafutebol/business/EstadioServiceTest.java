package com.futebol.partidafutebol.business;

import com.futebol.partidafutebol.dto.EstadioDto;
import com.futebol.partidafutebol.infrastructure.entitys.Estadio;
import com.futebol.partidafutebol.infrastructure.repository.EstadioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do EstadioService")
class EstadioServiceTest {

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private EstadioService estadioService;

    private EstadioDto estadioDtoValido;
    private Estadio estadioEntity;

    @BeforeEach
    void setUp() {
        estadioDtoValido = new EstadioDto("Maracanã");
        estadioEntity = Estadio.builder()
                .id(1)
                .nome("Maracanã")
                .build();
    }

    // ==================== TESTES DE CADASTRO ====================

    @Test
    @DisplayName("Deve cadastrar estádio com sucesso")
    void deveCadastrarEstadioComSucesso() {
        // Given
        when(estadioRepository.existsByNome("Maracanã")).thenReturn(false);
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadioEntity);

        // When
        EstadioDto resultado = estadioService.cadastrarEstadio(estadioDtoValido);

        // Then
        assertNotNull(resultado);
        assertEquals("Maracanã", resultado.getNome());
        verify(estadioRepository).existsByNome("Maracanã");
        verify(estadioRepository).save(any(Estadio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar estádio com nome nulo")
    void deveLancarExcecaoAoCadastrarEstadioComNomeNulo() {
        // Given
        EstadioDto estadioInvalido = new EstadioDto(null);

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.cadastrarEstadio(estadioInvalido)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Todos os dados devem ser preenchidos", exception.getReason());
        verify(estadioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar estádio com nome vazio")
    void deveLancarExcecaoAoCadastrarEstadioComNomeVazio() {
        // Given
        EstadioDto estadioInvalido = new EstadioDto("");

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.cadastrarEstadio(estadioInvalido)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Todos os dados devem ser preenchidos", exception.getReason());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar estádio com nome muito curto")
    void deveLancarExcecaoAoCadastrarEstadioComNomeMuitoCurto() {
        // Given
        EstadioDto estadioInvalido = new EstadioDto("AB");

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.cadastrarEstadio(estadioInvalido)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Todos os dados devem ser preenchidos", exception.getReason());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar estádio já existente")
    void deveLancarExcecaoAoCadastrarEstadioJaExistente() {
        // Given
        when(estadioRepository.existsByNome("Maracanã")).thenReturn(true);

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.cadastrarEstadio(estadioDtoValido)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Estadio ja existe", exception.getReason());
        verify(estadioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve aceitar nome com exatamente 3 caracteres")
    void deveAceitarNomeComExatamente3Caracteres() {
        // Given
        EstadioDto estadioMinimo = new EstadioDto("ABC");
        Estadio estadioSalvo = Estadio.builder().id(1).nome("ABC").build();
        
        when(estadioRepository.existsByNome("ABC")).thenReturn(false);
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadioSalvo);

        // When
        EstadioDto resultado = estadioService.cadastrarEstadio(estadioMinimo);

        // Then
        assertNotNull(resultado);
        assertEquals("ABC", resultado.getNome());
    }

    @Test
    @DisplayName("Deve rejeitar nome com apenas espaços")
    void deveRejeitarNomeComApenasEspacos() {
        // Given
        EstadioDto estadioInvalido = new EstadioDto("   ");

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.cadastrarEstadio(estadioInvalido)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Todos os dados devem ser preenchidos", exception.getReason());
    }

    // ==================== TESTES DE EDIÇÃO ====================

    @Test
    @DisplayName("Deve editar estádio com sucesso")
    void deveEditarEstadioComSucesso() {
        // Given
        EstadioDto estadioEditado = new EstadioDto("Arena Corinthians");
        Estadio estadioAtualizado = Estadio.builder()
                .id(1)
                .nome("Arena Corinthians")
                .build();

        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadioEntity));
        when(estadioRepository.existsByNome("Arena Corinthians")).thenReturn(false);
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadioAtualizado);

        // When
        EstadioDto resultado = estadioService.editarEstadio(estadioEditado, 1);

        // Then
        assertNotNull(resultado);
        assertEquals("Arena Corinthians", resultado.getNome());
        verify(estadioRepository).findById(1);
        verify(estadioRepository).save(any(Estadio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao editar estádio inexistente")
    void deveLancarExcecaoAoEditarEstadioInexistente() {
        // Given
        when(estadioRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.editarEstadio(estadioDtoValido, 999)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Estadio não encontrado", exception.getReason());
        verify(estadioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve manter nome original quando novo nome for nulo na edição")
    void deveManterNomeOriginalQuandoNovoNomeForNuloNaEdicao() {
        // Given
        EstadioDto estadioComNomeNulo = new EstadioDto(null);
        Estadio estadioAtualizado = Estadio.builder()
                .id(1)
                .nome("Maracanã") // Mantém o nome original
                .build();

        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadioEntity));
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadioAtualizado);

        // When & Then
        // O método validarEstadio será chamado primeiro e deve lançar exceção
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.editarEstadio(estadioComNomeNulo, 1)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("Deve lançar exceção ao editar para nome já existente")
    void deveLancarExcecaoAoEditarParaNomeJaExistente() {
        // Given
        EstadioDto estadioEditado = new EstadioDto("Arena Palmeiras");
        
        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadioEntity));
        when(estadioRepository.existsByNome("Arena Palmeiras")).thenReturn(true);

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.editarEstadio(estadioEditado, 1)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Estadio ja existe", exception.getReason());
    }

    // ==================== TESTES DE BUSCA ====================

    @Test
    @DisplayName("Deve buscar estádio por ID com sucesso")
    void deveBuscarEstadioPorIdComSucesso() {
        // Given
        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadioEntity));

        // When
        EstadioDto resultado = estadioService.buscarEstadioPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals("Maracanã", resultado.getNome());
        verify(estadioRepository).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar estádio por ID inexistente")
    void deveLancarExcecaoAoBuscarEstadioPorIdInexistente() {
        // Given
        when(estadioRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.buscarEstadioPorId(999)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Estadio não encontrado", exception.getReason());
    }

    // ==================== TESTES DE LISTAGEM ====================

    @Test
    @DisplayName("Deve listar todos os estádios quando filtro for nulo")
    void deveListarTodosEstadiosQuandoFiltroForNulo() {
        // Given
        Estadio estadio2 = Estadio.builder().id(2).nome("Arena Palmeiras").build();
        List<Estadio> estadios = Arrays.asList(estadioEntity, estadio2);
        
        when(estadioRepository.findAll()).thenReturn(estadios);

        // When
        List<EstadioDto> resultado = estadioService.listarTodosEstadios(null);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Maracanã", resultado.get(0).getNome());
        assertEquals("Arena Palmeiras", resultado.get(1).getNome());
        verify(estadioRepository).findAll();
        verify(estadioRepository, never()).findByNomeContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("Deve listar estádios filtrados por nome")
    void deveListarEstadiosFiltradosPorNome() {
        // Given
        List<Estadio> estadiosFiltrados = Arrays.asList(estadioEntity);
        
        when(estadioRepository.findByNomeContainingIgnoreCase("Mara")).thenReturn(estadiosFiltrados);

        // When
        List<EstadioDto> resultado = estadioService.listarTodosEstadios("Mara");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Maracanã", resultado.get(0).getNome());
        verify(estadioRepository).findByNomeContainingIgnoreCase("Mara");
        verify(estadioRepository, never()).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não encontrar estádios com filtro")
    void deveRetornarListaVaziaQuandoNaoEncontrarEstadiosComFiltro() {
        // Given
        when(estadioRepository.findByNomeContainingIgnoreCase("Inexistente")).thenReturn(Arrays.asList());

        // When
        List<EstadioDto> resultado = estadioService.listarTodosEstadios("Inexistente");

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(estadioRepository).findByNomeContainingIgnoreCase("Inexistente");
    }

    @Test
    @DisplayName("Deve listar todos os estádios quando filtro for string vazia")
    void deveListarTodosEstadiosQuandoFiltroForStringVazia() {
        // Given
        List<Estadio> estadios = Arrays.asList(estadioEntity);
        when(estadioRepository.findAll()).thenReturn(estadios);

        // When - Passando string vazia como filtro (deve ser tratado como null)
        List<EstadioDto> resultado = estadioService.listarTodosEstadios(null);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(estadioRepository).findAll();
    }

    // ==================== TESTES DO MÉTODO GENÉRICO FINDBYID ====================

    @Test
    @DisplayName("Deve encontrar estádio por ID no método genérico")
    void deveEncontrarEstadioPorIdNoMetodoGenerico() {
        // Given
        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadioEntity));

        // When
        Estadio resultado = estadioService.findById(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Maracanã", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção no método genérico quando ID não existir")
    void deveLancarExcecaoNoMetodoGenericoQuandoIdNaoExistir() {
        // Given
        when(estadioRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.findById(999)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Estadio não encontrado", exception.getReason());
    }

    // ==================== TESTES DE VALIDAÇÃO ====================

    @Test
    @DisplayName("Deve validar estádio com dados corretos")
    void deveValidarEstadioComDadosCorretos() {
        // Given
        when(estadioRepository.existsByNome("Maracanã")).thenReturn(false);

        // When
        EstadioDto resultado = estadioService.validarEstadio(estadioDtoValido);

        // Then
        assertNotNull(resultado);
        assertEquals("Maracanã", resultado.getNome());
        verify(estadioRepository).existsByNome("Maracanã");
    }

    @Test
    @DisplayName("Deve validar nome com trim - removendo espaços extras")
    void deveValidarNomeComTrimRemovendoEspacosExtras() {
        // Given
        EstadioDto estadioComEspacos = new EstadioDto("  Maracanã  ");
        when(estadioRepository.existsByNome("  Maracanã  ")).thenReturn(false);

        // When
        EstadioDto resultado = estadioService.validarEstadio(estadioComEspacos);

        // Then
        assertNotNull(resultado);
        assertEquals("  Maracanã  ", resultado.getNome());
        verify(estadioRepository).existsByNome("  Maracanã  ");
    }

    @Test
    @DisplayName("Deve lançar exceção para validação com nome nulo")
    void deveLancarExcecaoParaValidacaoComNomeNulo() {
        // Given
        EstadioDto estadioInvalido = new EstadioDto(null);

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.validarEstadio(estadioInvalido)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Todos os dados devem ser preenchidos", exception.getReason());
        verify(estadioRepository, never()).existsByNome(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção para validação de estádio duplicado")
    void deveLancarExcecaoParaValidacaoDeEstadioDuplicado() {
        // Given
        when(estadioRepository.existsByNome("Maracanã")).thenReturn(true);

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.validarEstadio(estadioDtoValido)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Estadio ja existe", exception.getReason());
        verify(estadioRepository).existsByNome("Maracanã");
    }
}