import java.util.LinkedHashMap;
import java.util.Map;

class LibraryMember {
    private String membershipPin;
    String branchCode;
    protected double finesOwed;
    public String displayName;
}

public class AccessChecker {

    public static String classifyAccess(String fieldModifier, String accessorContext) {
        if (fieldModifier == null || accessorContext == null) {
            return "DENIED";
        }

        switch (fieldModifier) {
            case "public":
                return "ALLOWED";

            case "protected":
                switch (accessorContext) {
                    case "SAME_CLASS":
                    case "SAME_PACKAGE":
                    case "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE":
                        return "ALLOWED";
                    case "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE":
                    case "DIFFERENT_PACKAGE":
                    default:
                        return "DENIED";
                }

            case "default":
                if ("SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext)) {
                    return "ALLOWED";
                }
                return "DENIED";

            case "private":
                if ("SAME_CLASS".equals(accessorContext)) {
                    return "ALLOWED";
                }
                return "DENIED";

            default:
                return "DENIED";
        }
    }

    public static String summarizeByModifier(String[][] attempts) {
        Map<String, int[]> stats = new LinkedHashMap<>();
        stats.put("private", new int[]{0, 0});
        stats.put("default", new int[]{0, 0});
        stats.put("protected", new int[]{0, 0});
        stats.put("public", new int[]{0, 0});

        if (attempts != null) {
            for (String[] attempt : attempts) {
                if (attempt != null && attempt.length >= 2) {
                    String modifier = attempt[0];
                    String context = attempt[1];

                    if (stats.containsKey(modifier)) {
                        String result = classifyAccess(modifier, context);
                        if ("ALLOWED".equals(result)) {
                            stats.get(modifier)[0]++;
                        } else {
                            stats.get(modifier)[1]++;
                        }
                    }
                }
            }
        }

        StringBuilder summary = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, int[]> entry : stats.entrySet()) {
            summary.append(entry.getKey())
                   .append(": ")
                   .append(entry.getValue()[0])
                   .append(" allowed / ")
                   .append(entry.getValue()[1])
                   .append(" denied");
            if (++count < stats.size()) {
                summary.append("\n");
            }
        }

        return summary.toString();
    }

    public static String firstDeniedAttempt(String[][] attempts) {
        if (attempts != null) {
            for (int i = 0; i < attempts.length; i++) {
                String[] attempt = attempts[i];
                if (attempt != null && attempt.length >= 2) {
                    String modifier = attempt[0];
                    String context = attempt[1];
                    String result = classifyAccess(modifier, context);

                    if ("DENIED".equals(result)) {
                        return modifier + " via " + context + " (attempt #" + (i + 1) + ")";
                    }
                }
            }
        }
        return "None Denied";
    }

    public static void main(String[] args) {
        System.out.println("--- Problem 1 Batch Test ---");
        String[][] batch1 = {
            {"private", "SAME_CLASS"},
            {"private", "SAME_PACKAGE"},
            {"default", "SAME_PACKAGE"},
            {"default", "DIFFERENT_PACKAGE"},
            {"protected", "SAME_PACKAGE"},
            {"protected", "SAME_CLASS"},
            {"public", "DIFFERENT_PACKAGE"}
        };
        System.out.println(summarizeByModifier(batch1));

        System.out.println("\n--- Problem 2 First Denied Test ---");
        String[][] batch2 = {
            {"public", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"},
            {"protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"},
            {"protected", "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"}
        };
        System.out.println(firstDeniedAttempt(batch2));
    }
}