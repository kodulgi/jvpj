package observer;

public class AdminObserver implements Observer {
    private String adminName;

    public AdminObserver(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public void update(String message) {
        System.out.println("[관리자 알림] " + adminName + "님: " + message);
    }
}