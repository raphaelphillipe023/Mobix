package mobix.model.cartao;

public abstract class Cartao {
    private Long id;
    private double saldo;
    private String titular;

    // Construtor Completo
    public Cartao(Long id, String titular, double saldoInicial) {
        this.id = id;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    // Método comum para todos os cartões
    public void adicionarSaldo(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    // Método que executa a lógica do débito usando o polimorfismo do calcularTarifa
    public boolean debitar(double tarifaBase) {
        double tarifaFinal = calcularTarifa(tarifaBase);
        if (this.saldo >= tarifaFinal) {
            this.saldo -= tarifaFinal;
            return true; // Débito realizado com sucesso
        }
        return false; // Saldo insuficiente (futuramente jogaremos uma Exception aqui!)
    }

    // MÉTODO ABSTRATO: Cada subclasse será obrigada a dizer como calcula sua própria tarifa
    public abstract double calcularTarifa(double tarifaBase);

    // Getters e Setters (Encapsulamento)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getSaldo() { return saldo; }
    // Não criamos setSaldo para impedir alterações diretas no saldo sem passar por debitar/adicionarSaldo

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
}