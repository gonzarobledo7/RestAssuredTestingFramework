package APITests.Utils;

public class Order {
    private int bookId;
    private String customerName;
 
    // Constructor
    public Order(int bookId, String customerName) {
        this.bookId = bookId;
        this.customerName = customerName;
    }
 
    // Getters y setters
    public int getBookId() {
        return bookId;
    }
 
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
 
    public String getCustomerName() {
        return customerName;
    }
 
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}