package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.apache.tomcat.jdbc.pool.DataSource;

import spring.AuthService;
import spring.ChangePasswordService;
import spring.MemberDao;
import spring.MemberRegisterService;

@Configuration
@EnableTransactionManagement
public class MemberConfig {
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        // 1.DataSource 객체 생성
        DataSource ds = new DataSource();
        // 2.JDBC 드라이버 클래스 지정(mysql 사용)
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // 3.JDBC URL 지정, 데이터베이스와 테이블 캐릭터 셋을 UTF-8로 설정했으므로 파라미터를 이용해 연결 시 사용할 캐릭터셋 지정
        ds.setUrl("jdbc:mysql://localhost/spring5fs?characterEncoding=utf8");
        // 4.DB 연결 시 사용할 계정과 암호 지정
        ds.setUsername("spring5");
        ds.setPassword("spring5");
        // 5.커넥션 풀을 초기화할 때 생성할 초기 커넥션 개수 지정(기본값은 10)
        ds.setInitialSize(2);
        // 6.커넥션 풀에서 가져올 수 있는 최대 커넥션 개수 지정(기본값은 100)
        ds.setMaxActive(10);
        // 7.유휴 커넥션 검사
        ds.setTestWhileIdle(true);
        // 8. 최소 유휴 시간 3분
        ds.setMinEvictableIdleTimeMillis(60000 * 3);
        // 9.10초 주음
        ds.setTimeBetweenEvictionRunsMillis(10 * 1000);
        return ds;
    }

    // 플랫폼 트랜잭션 매니저(PlatformTransactionManager) 빈 설정
    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    @Bean
    public MemberDao memberDao() {
        return new MemberDao(dataSource());
    }

    @Bean
    public MemberRegisterService memberRegisterService() {
        return new MemberRegisterService();
    }

    @Bean
    public ChangePasswordService changePasswordService() {
        ChangePasswordService pwdSvc = new ChangePasswordService();
        pwdSvc.setMemberDao(memberDao());
        return pwdSvc;
    }

    @Bean
    public AuthService authService() {
        AuthService authService = new AuthService();
        authService.setMemberDao(memberDao());
        return authService;
    }
}
