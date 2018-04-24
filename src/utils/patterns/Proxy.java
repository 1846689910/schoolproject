package utils.patterns;

public class Proxy {
    public static void main(String[] args) {
        OfficeInternetAccess access = new ProxyInternetAccess(12);
        access.grantAccess();
    }
}

/**
 * Proxy Pattern:
 * It provides the protection to the original object from the outside world.
 * */
interface OfficeInternetAccess{
    public void grantAccess();
}

class ProxyInternetAccess implements OfficeInternetAccess {
    private int userId;
    private RealInternetAccess access;
    public ProxyInternetAccess(int userId) {
        this.userId = userId;
    }

    @Override
    public void grantAccess(){
        int userCredentialLevel = getUserCredentialLevel(userId);
        if (userCredentialLevel > 5) {
            access = new RealInternetAccess(userId);
            access.grantAccess();
        } else {
            System.out.println("Internet access denied");
        }
    }

    public int getUserCredentialLevel(int userId){
        // retrieve the userCredentialLevel by using userId from database
        return (int)(Math.random() * 10 + 1);
    }

    private class RealInternetAccess implements OfficeInternetAccess {
        int userId;

        public RealInternetAccess(int userId) {
            this.userId = userId;
        }

        @Override
        public void grantAccess(){
            System.out.println("credential for internet access has been granted");
        }
    }

}