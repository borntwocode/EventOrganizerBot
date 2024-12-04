package uz.pdp.eventorganizerbot.entity.enums;

public enum RSVPStatus {

    YES,
    NO,
    MAYBE;

    public static RSVPStatus getFromString(String status) {
        return switch (status){
            case "+" -> YES;
            case "-" -> NO;
            default -> MAYBE;
        };
    }

    public static Object getRsvpResponse(RSVPStatus status) {
        if(status.equals(RSVPStatus.YES)){
            return "✅";
        } else if (status.equals(RSVPStatus.NO)) {
            return "❌";
        }
        return "❓";
    }

}
