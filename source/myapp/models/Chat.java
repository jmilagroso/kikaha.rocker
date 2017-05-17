package myapp.models;

/**
 * Created by jay on 5/16/17.
 */

public class Chat  {

    public String id;
    public String action;
    public String userSender;
    public String message;
    public String createdAt;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("action=" + action)
        .append("user_sender=" + userSender + " ")
                .append("message=" + message + " ")
                .append("created_at=" + createdAt + " ").toString();
    }
}