import java.util.Arrays;

public class LoanReceipt {
    private final String memberId;
    private final String[] bookIds;

    public LoanReceipt(String memberId, String[] bookIds) {
        this.memberId = memberId;
        this.bookIds = (bookIds != null) ? Arrays.copyOf(bookIds, bookIds.length) : new String[0];
    }

    public String getMemberId() {
        return memberId;
    }

    public String[] getBookIds() {
        return Arrays.copyOf(bookIds, bookIds.length);
    }

    public LoanReceipt withCorrectedBookId(int index, String newId) {
        if (index < 0 || index >= bookIds.length) {
            return this;
        }
        String[] updatedBookIds = Arrays.copyOf(bookIds, bookIds.length);
        updatedBookIds[index] = newId;
        return new LoanReceipt(this.memberId, updatedBookIds);
    }

    public static String processNightlyCirculation(LoanReceipt[] receipts) {
        int processed = 0;
        int nullSkipped = 0;
        int referenceOnlyCount = 0;
        int regularCount = 0;

        if (receipts != null) {
            for (LoanReceipt receipt : receipts) {
                if (receipt == null) {
                    nullSkipped++;
                } else {
                    processed++;
                    if (receipt instanceof ReferenceOnlyLoanReceipt) {
                        referenceOnlyCount++;
                    } else {
                        regularCount++;
                    }
                }
            }
        }

        return processed + " processed | " + nullSkipped + " null skipped | " +
               referenceOnlyCount + " reference-only | " + regularCount + " regular";
    }

    public static void main(String[] args) {
        System.out.println("--- Problem 5 Test ---");
        LoanReceipt r = new LoanReceipt("LIB-8841", new String[]{"BK-100", "BK-101"});

        // Defensive copy test
        String[] ids = r.getBookIds();
        ids[0] = "HACKED";
        System.out.println("Original Book ID after mutating getter: " + r.getBookIds()[0]); // Expected: BK-100

        // Immutable wither test
        LoanReceipt corrected = r.withCorrectedBookId(1, "BK-102");
        System.out.println("Original Receipts Book IDs: " + Arrays.toString(r.getBookIds())); // ["BK-100", "BK-101"]
        System.out.println("Corrected Receipts Book IDs: " + Arrays.toString(corrected.getBookIds())); // ["BK-100", "BK-102"]

        // Nightly circulation test
        LoanReceipt[] receipts = {
            new ReferenceOnlyLoanReceipt("LIB-001", new String[]{"BK-200"}, "Reading Room 3"),
            null,
            new LoanReceipt("LIB-002", new String[]{"BK-201"})
        };
        System.out.println(processNightlyCirculation(receipts));
    }
}

class ReferenceOnlyLoanReceipt extends LoanReceipt {
    private final String roomNumber;

    public ReferenceOnlyLoanReceipt(String memberId, String[] bookIds, String roomNumber) {
        super(memberId, bookIds);
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}