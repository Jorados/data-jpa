package study.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //스프링 부트에 이 설정을 해야 Auditing 가능
@SpringBootTest
class DataJpaApplicationTests {

	@Test
	void contextLoads() {
	}

}
