package cn.xpbootcamp.legacy_code.entity;

public class User {
    private long id;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isBalancedEnough(double amount) {
        return getBalance() >= amount;
    }

    private void addBalance(double amount) {
        setBalance(getBalance() + amount);
    }

    private void minusBalanced(double amount) {
        setBalance(getBalance() - amount);
    }

    public boolean transform(User target, double amount) {
        if (isBalancedEnough(amount)) {
            minusBalanced(amount);
            target.addBalance(amount);
            return true;
        }
        return false;
    }
}
