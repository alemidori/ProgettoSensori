package gestore;

import com.google.gson.Gson;
import reteSensori.simulatori.Misurazione;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Alessandra on 07/06/15.
 */
public class ReadMisThread implements Runnable {
    private Gson gson = new Gson();

    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String res = br.readLine();
                System.out.println("Nuove misurazioni: \n" + res);
                System.out.println("***************************************");

                Type listType = new TypeToken<ArrayList<Misurazione>>() {
                }.getType();
                ArrayList<Misurazione> listaAll = gson.fromJson(res, listType);

                ArrayList<Misurazione> listaTemp = new ArrayList<>();
                ArrayList<Misurazione> listaLum = new ArrayList<>();
                ArrayList<Misurazione> listaPIR1 = new ArrayList<>();
                ArrayList<Misurazione> listaPIR2 = new ArrayList<>();

                for (int i = 0; i < listaAll.size() - 1; i++) {

                    if (Objects.equals(listaAll.get(i).getType(), "Temperature")) {
                        listaTemp.add(listaAll.get(i));
                    }
                    if (Objects.equals(listaAll.get(i).getType(), "Light")) {
                        listaLum.add(listaAll.get(i));
                    }
                    if (Objects.equals(listaAll.get(i).getType(), "PIR1")) {
                        listaPIR1.add(listaAll.get(i));
                    }
                    if (Objects.equals(listaAll.get(i).getType(), "PIR2")) {
                        listaPIR2.add(listaAll.get(i));

                    }
                }

                System.out.println("Prendo il primo elemento di ogni lista");
                System.out.println("Tipo: "+listaTemp.get(0).getType()+ " Valore: "+listaTemp.get(0).getValue()+ " Timestamp "+listaTemp.get(0).getTimestamp());
                System.out.println("Tipo: "+listaLum.get(0).getType()+ " Valore: "+listaLum.get(0).getValue()+ " Timestamp "+listaLum.get(0).getTimestamp());
                System.out.println("Tipo: "+listaPIR1.get(0).getType()+ " Valore: "+listaPIR2.get(0).getValue()+ " Timestamp "+listaPIR2.get(0).getTimestamp());
                System.out.println("Tipo: "+listaPIR2.get(0).getType()+ " Valore: "+listaPIR2.get(0).getValue()+ " Timestamp "+listaPIR2.get(0).getTimestamp());


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
