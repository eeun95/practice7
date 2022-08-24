package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/*
@SpringBootApplication 에서 자동으로 데이터베이스 값을 설정하려고 시도하지만 사용자는 데이터베이스 값을 입력을 안했기에 발생하는 에러
DB를 사용하지 않는다는 속성 넣어주고 MemberConfig의 dataSource 사용하도록 함
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SpringProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringProjectApplication.class, args);
    }
}
