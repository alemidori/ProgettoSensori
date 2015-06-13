package gestore.serverREST;

import org.springframework.web.bind.annotation.*;
import reteSensori.simulatori.Buffer;
import reteSensori.simulatori.Misurazione;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

//con Spring è sempre necessario che ci sia una classe Controller contrassegnata con l'apposita annotazione RestController
//questa classe serve a mappare le richieste e le definizioni dei metodi da eseguire
@RestController
public class ServerController {
    Socket toGateway;
    Gson gson;
    DataOutputStream out;
    BufferedReader br;


    //RequestMapping indica il path in cui è possibile trovare lo specifico servizio che non è necessariamente GET
    //ma può essere anche POST o PUT. Se si vuole specificare il metodo HTTP usato si usa la forma RequestMapping(method=GET)
    //o POST o PUT a seconda della necessità

    @RequestMapping
    public void postUser() {
//registrazione di un client
    }

    @RequestMapping("/misurazione/recente/{tipo}")
    public
    @ResponseBody
    Misurazione getMisurazioneRecente(@PathVariable String tipo) {

        Misurazione recentMis = null;
        gson = new Gson();
        try {
            toGateway = new Socket("localhost", 7777);

            out = new DataOutputStream(toGateway.getOutputStream());
            br = new BufferedReader(new InputStreamReader(toGateway.getInputStream()));
            

            if (Objects.equals(tipo, "temperatura")) {
                out.writeBytes("recenteTemp"+'\n');
                String recentString = br.readLine();
                recentMis = gson.fromJson(recentString, Misurazione.class);
            }
            if (Objects.equals(tipo, "luminosita")) {
                out.writeBytes("recenteLum"+'\n');
                String recentString = br.readLine();
                recentMis = gson.fromJson(recentString, Misurazione.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recentMis;
    }


}