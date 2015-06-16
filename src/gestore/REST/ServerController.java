package gestore.REST;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reteSensori.simulatori.Misurazione;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

//con Spring è sempre necessario che ci sia una classe Controller contrassegnata con l'apposita annotazione RestController
//questa classe serve a mappare le richieste e le definizioni dei metodi da eseguire
@RestController
public class ServerController {
    private Socket toGateway;
    private Gson gson;
    private DataOutputStream out;
    private BufferedReader br;


    //RequestMapping indica il path in cui è possibile trovare lo specifico servizio che non è necessariamente GET
    //ma può essere anche POST o PUT. Se si vuole specificare il metodo HTTP usato si usa la forma RequestMapping(method=GET)
    //o POST o PUT a seconda della necessità


    private ArrayList<String> utenti = new ArrayList<>();

    @RequestMapping("/login/{utente}")
    public
    @ResponseBody
    ResponseEntity insertUser(@PathVariable String utente) {

        if (!utenti.isEmpty() && utenti.contains(utente)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } else {
            System.out.println("Nuovo utente: " + utente);
            utenti.add(utente);
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }

    @RequestMapping("/logout")
    public
    @ResponseBody
    ResponseEntity deleteUser() {
        utenti.remove(utenti.size()-1);
        return new ResponseEntity(HttpStatus.ACCEPTED);
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
                out.writeBytes("recenteTemp" + '\n');
                String recentString = br.readLine();
                recentMis = gson.fromJson(recentString, Misurazione.class);
            }
            if (Objects.equals(tipo, "luminosita")) {
                out.writeBytes("recenteLum" + '\n');
                String recentString = br.readLine();
                recentMis = gson.fromJson(recentString, Misurazione.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recentMis;
    }


}