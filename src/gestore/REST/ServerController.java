package gestore.REST;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reteSensori.classi.Messaggi;
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


    private ArrayList<String[]> indirizziUtenti = new ArrayList<>();

    @RequestMapping(value = "/login/{utente}/{ip}/{porta}", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity insertUser(@PathVariable String utente, String ip, String porta) {
        int flag = 0;
        if (!indirizziUtenti.isEmpty()) {
            for (String[] i : indirizziUtenti) {
                if (Objects.equals(i[0], utente)) {
                    flag = 1;
                }
            }
            if (flag == 1) return new ResponseEntity(HttpStatus.CONFLICT);
            else {
                String[] user = {utente, ip, porta};
                indirizziUtenti.add(user);
                System.out.println("Nuovo utente: " + user[0]);
                return new ResponseEntity(HttpStatus.CREATED);
            }
        } else {
            String[] user = {utente, ip, porta};
            indirizziUtenti.add(user);
            System.out.println("Nuovo utente: " + user[0]);
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/logout/{utente}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    ResponseEntity deleteUser(@PathVariable String utente) {
        for (String[] i : indirizziUtenti) {
            if (Objects.equals(i[0], utente)) {
                indirizziUtenti.remove(i);
            }
        }

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping("/misurazione/recente/{tipo}")
    public
    @ResponseBody
    Misurazione getMisurazioneRecente(@PathVariable String tipo) {

        Misurazione recentMis = null;
        gson = new Gson();
        try {
            toGateway = new Socket("localhost", 5555);

            out = new DataOutputStream(toGateway.getOutputStream());
            br = new BufferedReader(new InputStreamReader(toGateway.getInputStream()));


            if (Objects.equals(tipo, "temperatura")) {
                out.writeBytes(Messaggi.USER_REQUEST+"-"+"recenteTemp" + '\n');
                String recentString = br.readLine();
                recentMis = gson.fromJson(recentString, Misurazione.class);
            }
            if (Objects.equals(tipo, "luminosita")) {
                out.writeBytes(Messaggi.USER_REQUEST+"-"+"recenteLum" + '\n');
                String recentString = br.readLine();
                recentMis = gson.fromJson(recentString, Misurazione.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recentMis;
    }


}