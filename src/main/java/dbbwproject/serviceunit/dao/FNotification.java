package dbbwproject.serviceunit.dao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FNotification {
    public static String key = "notifications";
    private String notificationId;
    private long createdDate;
    private String message;
    private boolean isRead;
}
