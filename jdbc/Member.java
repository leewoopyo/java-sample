package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Member {
    private int id;
    private String username;
    private String password;

    /**
     * Generator
     */
    public Member() {
    }

    public Member(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    //SQL의 ResultSet을 인자로 constructor 구현
    public Member(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt("id");
            this.username = resultSet.getString("username");
            this.password = resultSet.getString("password");
        } catch (SQLException se) {

        }
    }

    /**
     * Getter, Setter
     */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member id(int id) {
        setId(id);
        return this;
    }

    public Member username(String username) {
        setUsername(username);
        return this;
    }

    public Member password(String password) {
        setPassword(password);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Member)) {
            return false;
        }
        Member member = (Member) o;
        return id == member.id && Objects.equals(username, member.username) && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }

    /**
     * Builder
     */
    public Member (MemberBuilder memberBuilder) {
        id = memberBuilder.id;
        username = memberBuilder.username;
        password = memberBuilder.password;
    }

    public static class MemberBuilder {
        private int id;
        private String username;
        private String password;

        public MemberBuilder () {

        }        

        public MemberBuilder id(int id) {
            this.id = id;
            return this;
        }

        public MemberBuilder username(String username) {
            this.username = username;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }


}
