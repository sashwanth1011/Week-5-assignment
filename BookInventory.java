public class BookInventory {
    private final int copiesTotal;
    private int copiesAvailable;

    public BookInventory(int copiesTotal) {
        if (copiesTotal <= 0) {
            throw new IllegalArgumentException("construction rejected");
        }
        this.copiesTotal = copiesTotal;
        this.copiesAvailable = copiesTotal;
    }

    public void checkOut() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
        }
    }

    public void checkIn() {
        if (copiesAvailable < copiesTotal) {
            copiesAvailable++;
        }
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public static void main(String[] args) {
        System.out.println("--- Problem 3 Test ---");
        BookInventory b = new BookInventory(3);
        b.checkOut();
        b.checkOut();
        b.checkOut();
        b.checkOut(); // 4th attempt - silently rejected
        System.out.println("Available after 4 checkouts: " + b.getCopiesAvailable()); // Expected: 0

        b.checkIn();
        b.checkIn();
        b.checkIn();
        b.checkIn(); // 4th attempt - silently rejected
        System.out.println("Available after 4 checkins: " + b.getCopiesAvailable()); // Expected: 3
    }
}