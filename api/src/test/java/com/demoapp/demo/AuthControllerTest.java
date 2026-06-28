package com.demoapp.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCadastrarUsuarioComEmailValidoESenhaForte() throws Exception {
        String email = "cadastro" + System.nanoTime() + "@teste.com";

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\",\"password\":\"Senha@123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)));
    }

    @Test
    void deveRetornarCredenciaisInvalidasQuandoLoginEstiverIncorreto() throws Exception {
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"naoexiste@teste.com\",\"password\":\"Senha@123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Credenciais inválidas")));
    }

    @Test
    void deveExibirMensagemEmailJaCadastradoQuandoEmailJaExistir() throws Exception {
        String email = "duplicado" + System.nanoTime() + "@teste.com";
        String body = "{\"email\":\"" + email + "\",\"password\":\"Senha@123\"}";

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("E-mail já está em uso")));
    }
}
