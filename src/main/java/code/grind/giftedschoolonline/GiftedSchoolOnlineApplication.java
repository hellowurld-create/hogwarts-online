package code.grind.giftedschoolonline;


import code.grind.giftedschoolonline.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GiftedSchoolOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftedSchoolOnlineApplication.class, args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }

}
