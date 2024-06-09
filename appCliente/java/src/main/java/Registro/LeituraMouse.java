package Registro;

import log.Log;
import log.LogLevel;
import log.LogManager;
import modelo.Computador;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LeituraMouse extends Leitura {
    private Point ultimaPosicao;
    private Boolean medicoOnline;
    private Computador computador;
    public LeituraMouse(Computador computador) {
        super(computador);
        this.computador = computador;
        if (GraphicsEnvironment.isHeadless()) {
            LogManager.salvarLog(new Log(LeituraMouse.class, "A interface não possuí mouse", LogLevel.INFO));
            ultimaPosicao = new Point(0 , 0);
        }else{
            ultimaPosicao = MouseInfo.getPointerInfo().getLocation();
        }
        medicoOnline = false;
        visualizarMouse();
    }

    @Override
    public void inserirLeitura() throws InterruptedException, IOException {

    }

    @Override
    public void realizarLeitura() throws IOException, InterruptedException {

    }

    private void visualizarMouse() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Point posicaoAtual = new Point(0 , 0);
                try{
                    posicaoAtual = MouseInfo.getPointerInfo().getLocation();
                }catch (Exception e ){
                    LogManager.salvarLog(new Log(LeituraMouse.class, "A interface não possuí mouse", LogLevel.INFO));
                }

                if (!posicaoAtual.equals(ultimaPosicao)) {
                    System.out.println("O mouse foi movido!");
                    ultimaPosicao.setLocation(posicaoAtual);
                    if(!medicoOnline){

                        String querry = "" +
                                "INSERT INTO logAtividade(atividade, fkComputador, fkDepartamento, fkHospital) values " +
                                "(1, "+ computador.getIdComputador() +", "+ computador.getFkDepartamento()+", "+ computador.getFkHospital() +")";
                        System.out.println("EXECUTANDO: "+querry);

                        executarQuery(connSQL, querry, LeituraMouse.class);
                        executarQuery(conn,querry, LeituraMouse.class);

                        medicoOnline = true;
                    }
                } else {
                    System.out.println("O mouse não foi movido!");

                    for(int i = 0; posicaoAtual.equals(ultimaPosicao); i++){
                        posicaoAtual = MouseInfo.getPointerInfo().getLocation();

                        if(i % 10 == 0){
                            if(i / 10 == 1){
                                System.out.println("Médico sem mexer o mouse há " + i/10 + " minuto");
                            } else {
                                System.out.println("Médico sem mexer o mouse há " + i/10 + " minutos");
                            }
                        }

                        if(i == 150){
                            medicoOnline = false;
                            System.out.println("Médico a 15 minutos sem mexer no mouse");

                            String querry = "" +
                                    "INSERT INTO logAtividade(atividade, fkComputador, fkDepartamento, fkHospital) values " +
                                    "(0, "+ computador.getIdComputador() +", "+ computador.getFkDepartamento()+", "+ computador.getFkHospital() +")";
                            System.out.println("executando: "+ querry);
                            executarQuery(connSQL, querry, LeituraMouse.class);
                            executarQuery(conn,querry, LeituraMouse.class);
                            break;
                        }

                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }

                }
            }
        }, 0, 2000);
    }

}
