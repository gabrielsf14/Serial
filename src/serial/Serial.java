/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author i546
 */
public class Serial {

    private SerialPort portaSerial;
    private OutputStream saida;
    private InputStream entrada;
    private String dadosLidos;

    public Serial(String com, int baudrate, int db, int sb, int parity) {
        CommPortIdentifier idPorta;

        try {
            idPorta = CommPortIdentifier.getPortIdentifier(com);
            try {
                portaSerial = (SerialPort) idPorta.open("Placa", 0);
            } catch (PortInUseException e) {
                JOptionPane.showMessageDialog(null, "Porta ja usada");
            }
        } catch (NoSuchPortException e1) {
            JOptionPane.showMessageDialog(null, "Porta nao encontrada");
        }
        try {
            portaSerial.setSerialPortParams(baudrate, db, sb, parity);
        } catch (UnsupportedCommOperationException e1) {
            JOptionPane.showMessageDialog(null, "Parametros incorretos");
        }
        try {
            entrada = portaSerial.getInputStream();
            saida = portaSerial.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> portasDisponiveis() {
        List<String> portas = new ArrayList<String>();
        CommPortIdentifier cpi;

        Enumeration pList = CommPortIdentifier.getPortIdentifiers();
        while (pList.hasMoreElements()) {
            cpi = (CommPortIdentifier) pList.nextElement();
            portas.add(cpi.getName());
            System.out.println(cpi.getName());
        }
        return portas;
    }

    public void setBit(byte bitASetar) {
        byte[] writeBuf = new byte[52];

        writeBuf[0] = '$';
        writeBuf[1] = 'X';
        writeBuf[2] = '2';
        writeBuf[3] = 'X';
        writeBuf[4] = '2';
        writeBuf[5] = (byte) (bitASetar + 128);
        writeBuf[6] = (byte) 0;
        writeBuf[7] = (byte) 0;
        for (int i = 8; i < writeBuf.length; i++) {
            writeBuf[i] = (byte) 0;
        }
        
        try {
            saida.write(writeBuf);
            saida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tratarResposta();
    }

    private void tratarResposta() {
        byte[] bufferLeitura = new byte[7];

        try {
            entrada.read(bufferLeitura);
            dadosLidos = new String(bufferLeitura);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String obterDadosLidos() {
        return dadosLidos;
    }
}
