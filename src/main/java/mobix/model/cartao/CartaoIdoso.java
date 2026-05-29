package mobix.model.cartao;

public class CartaoIdoso extends Cartao {
   public CartaoIdoso(Long id, String titular, double saldoInicial) {
        super(id, titular, saldoInicial); // Idoso não precisa necessariamente de saldo, mas passamos por padrão
    }

    @Override
    public double calcularTarifa(double tarifaBase) {
        return 0.0; // Tarifa zero, idoso não paga nada
    } 
}
