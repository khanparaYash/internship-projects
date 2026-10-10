package project4ordermanagement.order.entity;

public enum OrderStatus {
    CREATED,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return switch (this) {
            case CREATED -> nextStatus == PAID;
            case PAID -> nextStatus == SHIPPED;
            case SHIPPED -> nextStatus == DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }
}
