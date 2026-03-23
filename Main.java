import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class Main {

    static ArrayList<Evento> eventos = new ArrayList<>();
    static Usuario usuario;

    static final String ARQUIVO = "events.data";

    static String[] CATEGORIAS = {
        "Festa", "Show", "Esportivo", "Educacional", "Outros"
    };

    public static void main(String[] args) {

        carregarEventos();

        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== SISTEMA DE EVENTOS ===");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Cadastrar evento");
            System.out.println("3 - Listar eventos");
            System.out.println("4 - Participar de evento");
            System.out.println("5 - Cancelar participação");
            System.out.println("6 - Meus eventos");
            System.out.println("7 - Sair");
            System.out.print("Escolha: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: cadastrarUsuario(sc); break;
                case 2: cadastrarEvento(sc); break;
                case 3: listarEventos(); break;
                case 4: participarEvento(sc); break;
                case 5: cancelarParticipacao(sc); break;
                case 6: listarMeusEventos(); break;
                case 7:
                    salvarEventos();
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 7);

        sc.close();
    }

    static void cadastrarUsuario(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Cidade: ");
        String cidade = sc.nextLine();

        usuario = new Usuario(nome, email, cidade);
        System.out.println("Usuário cadastrado!");
    }

    static void cadastrarEvento(Scanner sc) {

        System.out.print("Nome do evento: ");
        String nome = sc.nextLine();

        System.out.print("Endereço: ");
        String endereco = sc.nextLine();

        System.out.print("Cidade do evento: ");
        String cidade = sc.nextLine();

        System.out.println("Categorias:");
        for (int i = 0; i < CATEGORIAS.length; i++) {
            System.out.println(i + " - " + CATEGORIAS[i]);
        }

        System.out.print("Escolha a categoria: ");
        int cat = sc.nextInt();
        sc.nextLine();

        System.out.print("Descrição: ");
        String descricao = sc.nextLine();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime data = null;

        while (data == null) {
            try {
                System.out.print("Data (dd/MM/yyyy HH:mm): ");
                String dataStr = sc.nextLine();
                data = LocalDateTime.parse(dataStr, formato);
            } catch (Exception e) {
                System.out.println("Formato inválido! Ex: 25/03/2026 20:00");
            }
        }

        eventos.add(new Evento(nome, endereco, cidade, CATEGORIAS[cat], data, descricao));

        System.out.println("Evento cadastrado!");
    }

    static void listarEventos() {

        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento.");
            return;
        }

        for (int i = 0; i < eventos.size() - 1; i++) {
            for (int j = i + 1; j < eventos.size(); j++) {
                if (eventos.get(i).data.isAfter(eventos.get(j).data)) {
                    Evento temp = eventos.get(i);
                    eventos.set(i, eventos.get(j));
                    eventos.set(j, temp);
                }
            }
        }

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        int i = 0;
        for (Evento e : eventos) {

            if (usuario != null && !e.cidade.equalsIgnoreCase(usuario.cidade)) {
                continue;
            }

            String status;

            if (e.data.isBefore(agora)) {
                status = "JÁ ACONTECEU";
            } else if (e.data.isAfter(agora)) {
                status = "VAI ACONTECER";
            } else {
                status = "ACONTECENDO AGORA";
            }

            System.out.println("\n[" + i + "] " + e.nome);
            System.out.println("Cidade: " + e.cidade);
            System.out.println("Categoria: " + e.categoria);
            System.out.println("Data: " + e.data.format(formato));
            System.out.println("Status: " + status);
            System.out.println("Descrição: " + e.descricao);

            i++;
        }
    }

    static void participarEvento(Scanner sc) {

        listarEventos();

        System.out.print("Escolha o índice: ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index >= 0 && index < eventos.size()) {
            eventos.get(index).participantes.add(usuario.nome);
            System.out.println("Confirmado!");
        }
    }

    static void cancelarParticipacao(Scanner sc) {

        listarEventos();

        System.out.print("Escolha o índice: ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index >= 0 && index < eventos.size()) {
            eventos.get(index).participantes.remove(usuario.nome);
            System.out.println("Cancelado!");
        }
    }

    static void listarMeusEventos() {

        for (Evento e : eventos) {
            if (e.participantes.contains(usuario.nome)) {
                System.out.println("✔ " + e.nome);
            }
        }
    }

    static void salvarEventos() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO));

            for (Evento e : eventos) {
                writer.write(e.nome + ";" +
                             e.endereco + ";" +
                             e.cidade + ";" +
                             e.categoria + ";" +
                             e.data + ";" +
                             e.descricao);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Erro ao salvar");
        }
    }

    static void carregarEventos() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO));

            String linha;

            while ((linha = reader.readLine()) != null) {
                try {
                    String[] p = linha.split(";");

                    eventos.add(new Evento(
                            p[0], p[1], p[2], p[3],
                            LocalDateTime.parse(p[4]),
                            p[5]
                    ));
                } catch (Exception e) {
                    System.out.println("Linha ignorada");
                }
            }

            reader.close();

        } catch (IOException e) {
        }
    }
}
