package entities;

public class Customer {

    private String name;
    private String email;
    private String telephonenumber;

    public Customer(String name, String email, String telephonenumber) {
        this.name = name;
        this.email = email;
        this.telephonenumber = telephonenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }
}
