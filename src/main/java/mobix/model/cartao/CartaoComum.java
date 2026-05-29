package mobix.model.cartao;

public class CartaoComum extends Cartao {
    public CartaoComum(Long id, String titular, double saldoInicial) {
        super(id, titular, saldoInicial);
    }

    @Override
    public double calcularTarifa(double tarifaBase) {
        return tarifaBase; // Cobra o valor integral
    }
}

