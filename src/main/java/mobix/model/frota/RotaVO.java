package mobix.model.frota;

import java.util.ArrayList;
import java.util.List;

public class RotaVO {
    private int id;
    private String nomeLinha;
    private String status; // Ex: "Ativa", "Inativa", "Em Manutenção"
    private List<String> paradas; // Uso de Collections
    private String horarioEstimado;

    // Construtor conforme especificado na Sprint: RotaVO(int id, String nomeLinha)
    public RotaVO(int id, String nomeLinha) {
        this.id = id;
        this.nomeLinha = nomeLinha;
        this.status = "Ativa"; // Padrão inicial
        this.paradas = new ArrayList<>(); // Inicializa a lista vazia para evitar NullPointerException
    }

    // Método para gerenciar a coleção de paradas
    public void adicionarParada(String nomeParada) {
        if (nomeParada != null && !nomeParada.trim().isEmpty()) {
            this.paradas.add(nomeParada);
        }
    }

    // Getters e Setters (Encapsulamento)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeLinha() { return nomeLinha; }
    public void setNomeLinha(String nomeLinha) { this.nomeLinha = nomeLinha; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getParadas() { 
        return paradas; 
    }

    public String getHorarioEstimado() { return horarioEstimado; }
    public void setHorarioEstimado(String horarioEstimado) { this.horarioEstimado = horarioEstimado; }
}