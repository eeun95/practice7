package spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class MemberDao {

    private JdbcTemplate jdbcTemplate;

    public MemberDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Member selectByEmail(String email) {
        List<Member> results = jdbcTemplate.query(
                "select * from MEMBER where EMAIL = ?",
                /* RowMapper 구현 클래스를 만들어 해당 코드 삭제
                new RowMapper<Member>() {

                    @Override
                    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Member member = new Member(
                            rs.getString("EMAIL"),
                            rs.getString("PASSWORD"),
                            rs.getString("NAME"),
                            rs.getTimestamp("REGDATE").toLocalDateTime());
                        member.setId(rs.getLong("ID"));
                        return member;
                    }
                },
                 */
                new MemberRowMapper(),
                email);
        return results.isEmpty() ? null : results.get(0);
    }

    public void insert(Member member) {
        // GeneratedKeyHolder 객체를 생성(자동 생성된 키값을 구해주는 KeyHolder 구현클래스임)
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //PreparedStatementCreator 객체와 KeyHolder 객체를 파라미터로 가짐
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                // 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
                PreparedStatement pstmt = con.prepareStatement(
                        "insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) values (?,?,?,?)",
                        new String[]{"ID"}
                );

                // 인덱스 파라미터의 값 설정
                pstmt.setString(1, member.getEmail());
                pstmt.setString(2, member.getPassword());
                pstmt.setString(3, member.getName());
                pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterDateTime()));
                // 생성한 PreparedStatement 객체 리턴
                return pstmt;
            }
        }, keyHolder);
        Number keyValue = keyHolder.getKey();
        member.setId(keyValue.longValue());


        /* 람다식 사용
        jdbcTemplate.update((Connection con) -> {
            PreparedStatement pstmt = con.prepareStatement(
                    "insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE)" + "values (?,?,?,?)",
                    new String[]{"ID"});

            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getName());
            pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterDateTime()));

            return pstmt;

            }, keyHolder);

         */
    }

    public void update(Member member) {
        jdbcTemplate.update(
                "update Member set NAME=?, PASSWORD=? where EMAIL=?",
                member.getName(), member.getPassword(), member.getEmail()
        );
    }

    public List<Member> selectAll() {
        List<Member> results = jdbcTemplate.query("select * from MEMBER", new MemberRowMapper());
        return results;
    }

    public int count() {
        // 두번째 파라미터는 컬럼을 읽어올 때 사용할 타입 지정
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from MEMBER", Integer.class
        );
        return count;
    }
}
