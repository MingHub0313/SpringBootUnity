package info.xiaomo.mybatis.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author : xiaomo
 */
@Data
@ToString(callSuper = false)
@NoArgsConstructor
public class User {

    private Long id;

    private String avatar;

    private String email;

    private String name;

    private String username;

    public User(Long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }

}
