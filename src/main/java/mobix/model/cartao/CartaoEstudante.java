package mobix.model.cartao;

public class CartaoEstudante extends Cartao{
   public CartaoEstudante(Long id, String titular, double saldoInicial) {
        super(id, titular, saldoInicial);
    }

    @Override
    public double calcularTarifa(double tarifaBase) {
        return tarifaBase * 0.5; // Aplica 50% de desconto
    }
} 

