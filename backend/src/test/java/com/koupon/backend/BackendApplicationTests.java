package koupon.backend.src.test.java.com.koupon.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers          // ① Testcontainers JUnit5 확장 활성화
@SpringBootTest
class BackendApplicationTests {

    // ② MySQL 8.3 컨테이너 정의
    @Container
    static MySQLContainer<?> mysql =
        new MySQLContainer<>("mysql:8.3")
          .withDatabaseName("coupon")
          .withUsername("user")
          .withPassword("password");

    // ③ Spring Datasource 프로퍼티를 컨테이너 값으로 바인딩
    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",      mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    void contextLoads() {
        // 컨텍스트 기동만 확인
    }
}
