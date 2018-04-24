package utils.patterns;

public class Adapter {
}
/**
 * Adapter Pattern:
 converts the interface of a class into another interface that a client wants.

 In other words, to provide the interface according to client requirement while using the services of a class with a different interface.

 The Adapter Pattern is also known as Wrapper.
 使用接口去拓展已有类的功能
 * */

interface CreditCard {
    public String getBankAccountDetails();
    public String getCreditCardNumber();
}

class BankAccountDetails{
    private String accountNumber;
    private long checking;
    private long saving;

    protected String getAccountNumber() {
        return accountNumber;
    }

    protected long getChecking(){
        return checking;
    }

    protected long getSaving(){
        return saving;
    }

    protected String getDetails(){
        return accountNumber + " " + checking + " " + saving;
    }
}

class BankCustomer extends BankAccountDetails implements CreditCard {
    @Override
    public String getBankAccountDetails(){
        return this.getDetails();
    }

    @Override
    public String getCreditCardNumber(){
        return null;
    }
}
