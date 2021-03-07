package br.com.estudo.forum.controller.dto;

public class TokenDto {

    private String token;
    private String tipo;

    public TokenDto(String token, String tipo) {
        this.tipo = tipo;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }
}
