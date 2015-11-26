package ec.orangephi.tcg.intefaces;

/** Los Activities principales deben de implementar esta interfaz para que los adapters y framents
 * puedan comunicarse con el Activity Host sin tight coupling.
 * Created by gesuwall on 11/23/15.
 */
public interface CardCollector {
    /**
     * Muestra una carta en la pantalla de forma detallada.
     * @param code el codigo de la carta a mostrar.
     */
    void viewCard(String code);

    void showNewCardDialog();
}
