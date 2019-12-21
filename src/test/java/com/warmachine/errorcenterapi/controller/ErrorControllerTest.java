package com.warmachine.errorcenterapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmachine.errorcenterapi.ContextsLoads;
import com.warmachine.errorcenterapi.Messages;
import com.warmachine.errorcenterapi.base.controller.BaseControllerTest;
import com.warmachine.errorcenterapi.controller.error.request.ErrorRequest;
import com.warmachine.errorcenterapi.controller.error.response.ErrorMessageResponse;
import com.warmachine.errorcenterapi.controller.error.response.ErrorResponse;
import com.warmachine.errorcenterapi.entity.Error;
import com.warmachine.errorcenterapi.entity.User;
import com.warmachine.errorcenterapi.repository.ErrorsRepository;
import com.warmachine.errorcenterapi.service.impl.ErrorServiceImpl;
import com.warmachine.errorcenterapi.util.Ambiente;
import com.warmachine.errorcenterapi.util.Level;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Import(ContextsLoads.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ErrorControllerTest extends BaseControllerTest {

    private static final String SESSION_URL_CREATE = "/v1/errors/create";
    private static final String SESSION_URL_DETAIL = "/v1/errors/detail";
    private static final String SESSION_URL_DELETE = "/v1/errors/delete/1";
    private static final String SESSION_URL_ARCHIVE = "/v1/errors/archive/1";

    private ObjectMapper mapper;

    @MockBean
    private ErrorServiceImpl errorService;

    @MockBean
    private ErrorsRepository errorsRepository;

    @Autowired
    protected MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper();
    }

    public void deveRetornarSucessoQuandoReceboUmIdCorretoParaArquivarUmErro() throws Exception {
        Error error = Error.builder().id(1L).build();
        final String expectedResponse = loadResourceAsString("json/archive/archive-error-response.json");

        final ErrorMessageResponse response = new ErrorMessageResponse(Messages.ERROR_ARCHIVED);

        when(errorService.archive(error.getId())).thenReturn(response);

        when(errorsRepository.findById(error.getId())).thenReturn(java.util.Optional.of(error));

        mockMvc.perform(put(SESSION_URL_ARCHIVE))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void deveRetornarExcessaoQuandoNaoForPossivelAcharUmErroParaDeletar() throws Exception {
        final String expectedResponse = loadResourceAsString("json/exception/exception.json");

        when(errorService.delete(1L)).thenThrow(new IllegalArgumentException(Messages.UNABLE_TO_FIND_ERROR));

        mockMvc.perform(delete(SESSION_URL_DELETE))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void deveRetornarErrosQuandoDetalho() throws Exception {
        final String expectedResponse = loadResourceAsString("json/detail/detail-response.json");

        when(errorService.detailAllErrors()).thenReturn(Lists.newArrayList(
                ErrorResponse.builder().ambient(Ambiente.DESENVOLVIMENTO).description("teste1").level(Level.WARNING).usernameFromUser("username").status(null).ipOrigin("ip").build(),
                ErrorResponse.builder().ambient(Ambiente.HOMOLOGACAO).description("teste2").level(Level.DEBUG).usernameFromUser("username").status(null).ipOrigin("ip").build()
        ));

        mockMvc.perform(get(SESSION_URL_DETAIL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));


    }

    @Test
    public void deveDeletarQuandoReceboUmIdValido() throws Exception {
        Error error = Error.builder().id(1L).build();
        final String expectedResponse = loadResourceAsString("json/delete/delete-error-response.json");

        final ErrorMessageResponse response = new ErrorMessageResponse(Messages.ERROR_DELETED);

        when(errorService.delete(error.getId())).thenReturn(response);

        when(errorsRepository.findById(error.getId())).thenReturn(java.util.Optional.of(error));

        mockMvc.perform(delete(SESSION_URL_DELETE))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

    }


    @Test
    public void deveRetornarSucessoQuandoReceboUmErroCorretoParaSalvar() throws Exception {
        final JsonNode requestJson = loadAsJsonFromResource("json/create-error/create-error-request.json");
        final String expectedResponse = loadResourceAsString("json/create-error/create-error-response.json");

        final ErrorMessageResponse response = new ErrorMessageResponse(Messages.ERROR_CREATED);

        when(errorService.createError(any(ErrorRequest.class))).thenReturn(response);

        mockMvc.perform(post(SESSION_URL_CREATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestJson)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void deveRetornarErroQuandoReceboUmErroNull() throws Exception {
        String json = "{}";
        final String expectedResponse = loadResourceAsString("json/create-error/errors/response/create-error-response-null.json");

        mockMvc.perform(post(SESSION_URL_CREATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

        verify(errorsRepository, times(0)).save(any(Error.class));

    }

    @Test
    public void deveRetornarErroQuandoReceboUmErroSemAmbientEDescription() throws Exception {
        final JsonNode requestJson = loadAsJsonFromResource("json/create-error/errors/request/create-error-request-ambient-description.json");
        final String expectedResponse = loadResourceAsString("json/create-error/errors/response/create-error-response-ambient-description.json");

        mockMvc.perform(post(SESSION_URL_CREATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

        verify(errorsRepository, times(0)).save(any(Error.class));

    }

    @Test
    public void deveRetornarErroQuandoReceboUmErroSemDescriptionELevel() throws Exception {
        final JsonNode requestJson = loadAsJsonFromResource("json/create-error/errors/request/create-error-request-description-level.json");
        final String expectedResponse = loadResourceAsString("json/create-error/errors/response/create-error-response-description-level.json");

        mockMvc.perform(post(SESSION_URL_CREATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

        verify(errorsRepository, times(0)).save(any(Error.class));

    }

    @Test
    public void deveRetornarErroQuandoReceboUmErroSemDescription() throws Exception {
        final JsonNode requestJson = loadAsJsonFromResource("json/create-error/errors/request/create-error-request-description.json");
        final String expectedResponse = loadResourceAsString("json/create-error/errors/response/create-error-response-description.json");

        mockMvc.perform(post(SESSION_URL_CREATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

        verify(errorsRepository, times(0)).save(any(Error.class));

    }

    @Test
    public void deveRetornarErroQuandoReceboUmErroSemLevel() throws Exception {
        final JsonNode requestJson = loadAsJsonFromResource("json/create-error/errors/request/create-error-request-level.json");
        final String expectedResponse = loadResourceAsString("json/create-error/errors/response/create-error-response-level.json");

        mockMvc.perform(post(SESSION_URL_CREATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

        verify(errorsRepository, times(0)).save(any(Error.class));

    }

    @Test
    public void deveRetornarErroQuandoReceboUmErroSemAmbient() throws Exception {
        final JsonNode requestJson = loadAsJsonFromResource("json/create-error/errors/request/create-error-request-ambient.json");
        final String expectedResponse = loadResourceAsString("json/create-error/errors/response/create-error-response-ambient.json");

        mockMvc.perform(post(SESSION_URL_CREATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

        verify(errorsRepository, times(0)).save(any(Error.class));

    }
}
