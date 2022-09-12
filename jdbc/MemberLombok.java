package jdbc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberLombok {
    private int id;
    private String username;
    private String password;
}
