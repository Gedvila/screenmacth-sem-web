package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.security.cert.LDAPCertStoreParameters;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=2e4f75ca";
    private ConverteDados conversor = new ConverteDados();
    private int ano;

    public void exibeMenu(){
        System.out.println("\nDigite o nome da série que quer obter informações: ");
        var nomeSerie = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO +nomeSerie.replace(" ","+")
                .toLowerCase() + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        dados.toString();
        System.out.println("\nSérie: " + dados.titulo() +
                "\nTemporadas: " + dados.totalDeTemporadas() +
                "\nAvaliação da série: " + dados.avaliacao() +
                "\n"
        );

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalDeTemporadas();i++){

             json = consumoAPI.obterDados(ENDERECO +nomeSerie.replace(" ","+")
                    .toLowerCase()+"&season="+ i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            dadosTemporada.toString();
            temporadas.add(dadosTemporada);
            ;
        }


        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t-> t.episodios().stream())
                .collect(Collectors.toList());


        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        LocalDate dataBusca = LocalDate.of(ano,1,1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.forEach(f ->{
            System.out.println("\nTemporada: " + f.getTemporada() +
                    "\nTítulo: " + f.getTitulo() +
                    "\nEpisódio: " + f.getNumeroEpisodio() +
                    "\nAvaliação: " + f.getAvaliacao() +
                    "\nData de lançamento: " + f.getDataLancamento().format(formatador)
                    );
        });


   }
}

