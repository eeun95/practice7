package spring;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// Member를 위한 RowMapper 구현 클래스를 이용하여 코드 중복 제거
public class MemberRowMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member(
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                rs.getString("NAME"),
                rs.getTimestamp("REGDATE").toLocalDateTime()
        );
        member.setId(rs.getLong("ID"));
        return member;
    }
}
