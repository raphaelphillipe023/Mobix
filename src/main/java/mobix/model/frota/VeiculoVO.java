package mobix.model.frota;

public class VeiculoVO {
    private String placa;
    private int capacidade;
    private String tipo; 

   
    public VeiculoVO(String placa, int capacity, String tipo) {
        this.placa = placa;
        this.capacidade = capacity;
        this.tipo = tipo;
    }

    
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}