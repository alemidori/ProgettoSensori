package gestore.serverREST;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//contrassegna la classe che contiene il main, cioè la componente eseguibile dell'applicazione
//la quale contiene la chiamata al metodo run di SpringApplication
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}