package br.com.al.screenmatch.service;

public interface IConverterDados {
    <T> T obterDados(String json, Class<T> classe);
}
