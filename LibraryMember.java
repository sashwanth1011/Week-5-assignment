import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LibraryMember {
    private String membershipId;
    private String name;
    private boolean premiumMember;
    private String securityAnswerHash; // One-way transformation

    public LibraryMember() {
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String id) {
        // Write-once property requirement
        if (this.membershipId == null) {
            this.membershipId = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPremiumMember() {
        return premiumMember;
    }

    public void setPremiumMember(boolean premium) {
        this.premiumMember = premium;
    }

    public void setSecurityAnswer(String answer) {
        if (answer != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(answer.getBytes(StandardCharsets.UTF_8));
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                this.securityAnswerHash = hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                this.securityAnswerHash = String.valueOf(answer.hashCode());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Problem 4 Test ---");
        LibraryMember m = new LibraryMember();
        m.setMembershipId("LIB-8841");
        m.setName("Priya Nair");
        m.setPremiumMember(true);

        System.out.println("Membership ID: " + m.getMembershipId());
        System.out.println("Name: " + m.getName());
        System.out.println("Is Premium: " + m.isPremiumMember());

        // Second set call should be ignored
        m.setMembershipId("FAKE-0000");
        System.out.println("Membership ID after re-set attempt: " + m.getMembershipId()); // LIB-8841

        m.setSecurityAnswer("BlueMountain");
        System.out.println("Security Answer set successfully (Write-Only).");
    }
}