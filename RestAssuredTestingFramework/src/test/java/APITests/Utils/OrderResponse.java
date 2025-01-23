package APITests.Utils;

public class OrderResponse {
    private String orderId;
    private boolean created; // Agregar este campo
 
    // Getters y setters
    public String getOrderId() {
        return orderId;
    }
 
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
 
    // Getter y setter para el campo creado
    public boolean isCreated() {
        return created;
    }
 
    public void setCreated(boolean created) {
        this.created = created;
    }
}
