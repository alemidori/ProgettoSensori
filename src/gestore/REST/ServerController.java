package gestore.REST;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reteSensori.classi.Messaggi;

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

    private DataOutputStream out;
    private BufferedReader br;

    private ArrayList<String[]> indirizziUtenti = new ArrayList<>();

    @RequestMapping(value = "/login/{utente}/{ip}/{porta}", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity insertUser(@PathVariable(value = "utente") String utente,
                              @PathVariable(value = "ip") String ip,
                              @PathVariable(value = "porta") String porta) {

        int flag = 0;
        if (!indirizziUtenti.isEmpty()) {
            for (String[] i : indirizziUtenti) {
                if (Objects.equals(i[0].toLowerCase(), utente.toLowerCase())) {
                    flag = 1;
                }
            }
            if (flag == 1) return new ResponseEntity(HttpStatus.CONFLICT);
            else {
                if (Objects.equals(ip, "localhost") || Objects.equals(ip, "127.0.0.1")) {
                    for (String[] i : indirizziUtenti) {
                        if (Objects.equals(i[2], porta)) {
                            flag = 2;
                        }
                    }
                    if (flag == 2) return new ResponseEntity(HttpStatus.ALREADY_REPORTED);
                    else {
                        String[] user = {utente, ip, porta};
                        indirizziUtenti.add(user);
                        System.out.println("Nuovo utente: " + user[0] + "-" + user[1] + "-" + user[2]);
                        return new ResponseEntity(HttpStatus.CREATED);
                    }

                } else {
                    return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
                }
            }
        } else {
            if (Objects.equals(ip, "localhost") || Objects.equals(ip, "127.0.0.1")) {
                String[] user = {utente, ip, porta};
                indirizziUtenti.add(user);
                System.out.println("Nuovo utente: " + user[0] + "-" + user[1] + "-" + user[2]);
                return new ResponseEntity(HttpStatus.CREATED);
            } else {
                return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @RequestMapping(value = "/logout/{utente}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    ResponseEntity deleteUser(@PathVariable String utente) {
        for (String[] i : indirizziUtenti) {
            if (Objects.equals(i[0], utente)) {
                indirizziUtenti.remove(i);
                return new ResponseEntity(HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/misurazione/recente/{tipo}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getMisurazioneRecente(@PathVariable String tipo) {

        String recentString = null;
        try {
            toGateway = new Socket("localhost", 5555);

            out = new DataOutputStream(toGateway.getOutputStream());
            br = new BufferedReader(new InputStreamReader(toGateway.getInputStream()));


            if (Objects.equals(tipo, "temperatura")) {
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "recenteTemp" + '\n');
                recentString = br.readLine();

            }
            else if (Objects.equals(tipo, "luminosita")) {
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "recenteLum" + '\n');
                recentString = br.readLine();

            }
            else{
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "sconosciuto" + '\n');
                recentString = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recentString;
    }

    @RequestMapping(value = "/misurazione/media/{tipo}/{tempoInizio}/{tempoFine}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getMediaMisurazioni(@PathVariable(value = "tipo") String tipo,
                               @PathVariable(value = "tempoInizio") String tempoInizio,
                               @PathVariable(value = "tempoFine") String tempoFine) {
        String media = null;

        try {
            toGateway = new Socket("localhost", 5555);

            out = new DataOutputStream(toGateway.getOutputStream());
            br = new BufferedReader(new InputStreamReader(toGateway.getInputStream()));


            if (Objects.equals(tipo, "temperatura")) {
                System.out.println(tempoInizio + "-" + tempoFine);
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "mediaTemp" + "-" + tempoInizio + "-" + tempoFine + '\n');
                media = br.readLine();

            }
            else if (Objects.equals(tipo, "luminosita")) {
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "mediaLum" + "-" + tempoInizio + "-" + tempoFine + '\n');
                media = br.readLine();

            }
            else{
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "sconosciuto" + '\n');
                media = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return media;
    }

    @RequestMapping(value = "/misurazione/min_max/{tipo}/{tempoInizio}/{tempoFine}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getMinMaxMisurazioni(@PathVariable(value = "tipo") String tipo,
                                @PathVariable(value = "tempoInizio") String tempoInizio,
                                @PathVariable(value = "tempoFine") String tempoFine) {
        String minMax = null;
        try {
            toGateway = new Socket("localhost", 5555);

            out = new DataOutputStream(toGateway.getOutputStream());
            br = new BufferedReader(new InputStreamReader(toGateway.getInputStream()));

            if (Objects.equals(tipo, "temperatura")) {
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "minMaxTemp" + "-" + tempoInizio + "-" + tempoFine + '\n');
                minMax = br.readLine();

            }
            else if (Objects.equals(tipo, "luminosita")) {
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "minMaxLum" + "-" + tempoInizio + "-" + tempoFine + '\n');
                minMax = br.readLine();
            }
            else{
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "sconosciuto" + '\n');
                minMax = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return minMax;
    }

    @RequestMapping(value = "/misurazione/presenza/{tipo}/{tempoInizio}/{tempoFine}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getRilevazionePresenza(@PathVariable(value = "tipo") String tipo,
                                  @PathVariable(value = "tempoInizio") String tempoInizio,
                                  @PathVariable(value = "tempoFine") String tempoFine) {

        String presenze = null;
        try {
            toGateway = new Socket("localhost", 5555);

            out = new DataOutputStream(toGateway.getOutputStream());
            br = new BufferedReader(new InputStreamReader(toGateway.getInputStream()));

            if (Objects.equals(tipo, "pir1")) {
                System.out.println(tempoInizio + "-" + tempoFine);
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "presPir1" + "-" + tempoInizio + "-" + tempoFine + '\n');
                presenze = br.readLine();

            }
            else if (Objects.equals(tipo, "pir2")) {
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "presPir2" + "-" + tempoInizio + "-" + tempoFine + '\n');
                presenze = br.readLine();
            }
            else{
                out.writeBytes(Messaggi.USER_REQUEST + "-" + "sconosciuto" + '\n');
                presenze = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return presenze;
    }

    @RequestMapping(value = "/misurazione/media_presenza/{tempoInizio}/{tempoFine}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getMediaPres(@PathVariable(value = "tempoInizio") String tempoInizio,
                        @PathVariable(value = "tempoFine") String tempoFine) {

        String mediaPres = null;
        try {
            toGateway = new Socket("localhost", 5555);

            out = new DataOutputStream(toGateway.getOutputStream());
            out.writeBytes(Messaggi.USER_REQUEST + "-" + "mediaPres" + "-" + tempoInizio + "-" + tempoFine + '\n');
            br = new BufferedReader(new InputStreamReader(toGateway.getInputStream()));
            mediaPres = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPres;
    }

}