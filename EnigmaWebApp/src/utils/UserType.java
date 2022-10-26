package utils;

public enum UserType {
    UBOAT, ALLY, AGENT;

    public static UserType strToUserType(String freeText){
        switch(freeText){
            case "uboat":
                return UBOAT;
            case "ally":
                return ALLY;
            case "agent":
                return AGENT;
            default:
                throw new RuntimeException("cant convert string to user type, invalid input given");
        }
    }
}
