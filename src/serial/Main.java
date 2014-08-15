/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serial;

import java.util.Scanner;

/**
 *
 * @author i546
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("teste");
        
        Serial.portasDisponiveis();
        Serial s = new Serial("COM32",19200,8,1,0);
        Scanner scan = new Scanner(System.in);
        while(true){
            s.setBit(scan.nextByte());
            System.out.print(s.obterDadosLidos());
        }
    }
}
