import Swarm.ExtendedJSch;
import Swarm.ExtendedLog;
import com.jcraft.jsch.JSchException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by Adrian Szymowiat on 29.08.2018
 */
public class Main {
    /**
     * Funkcja glowna programu
     * @param args
     */
    public static void main (String[] args ) {
        try {
            System.out.println(ExtendedLog.getLog("Rozpoczęto działanie aplikacji."));

            ParseConfig parseConfig = new ParseConfig();
            parseConfig.getConfig();

            String fileName = ExtendedLog.getTimeAndDate();
            fileName = fileName.substring(1, fileName.length() - 2).replace(':', '_');
            OutputStream outputStream = new FileOutputStream(ParseConfig.pathToLogFolder + "/" + fileName + ".txt");

            ExtendedJSch extendedJSch = new ExtendedJSch(ParseConfig.hostIpAddress, ParseConfig.userName, ParseConfig.userPassword, ParseConfig.sshPort, ParseConfig.sessionTimeout);
            extendedJSch.setNoKeyChecking();
            extendedJSch.connect();
            extendedJSch.setShellChannel(0, "system");

            extendedJSch.sendCommand(ParseConfig.commandsBufor,500,true);
            if(extendedJSch.getByteArrayOutputStream().size() != 0) {
                extendedJSch.getByteArrayOutputStream().writeTo(outputStream);
                outputStream.close();
            }
            extendedJSch.close();
            System.out.println(ExtendedLog.getLog("Zakonczono działanie aplikacji."));
        }catch(ParserConfigurationException | SAXException | IOException e){
            System.out.println("Nastąpił niespodziewany błąd podczas wczytywania danych konfiguracyjnych: " + e);
        }catch(JSchException e){
            System.out.println("Nastąpił niespodziewany błąd podczas próby nawiązania połączenia SSH: " + e);
        }//catch(InterruptedException e){
            //System.out.println("Nastąpił niespodziewany błąd związany prawdopodbnie z usypianiem aplikacji (Thread.sleep()): " + e);
        //}
    }
}
/*
            System.out.println(ParseConfig.pathToLogFolder);
            System.out.println(ParseConfig.sessionTimeout);
            System.out.println(ParseConfig.channelTimeout);
            System.out.println(ParseConfig.sshPort);
            System.out.println(ParseConfig.hostIpAddress);
            System.out.println(ParseConfig.userName);
            System.out.println(ParseConfig.userPassword);
            for(int i = 0; i < ParseConfig.commandsBufor.size(); i++){
                System.out.println(ParseConfig.commandsBufor.get(i));
            }
            System.out.println(ParseConfig.sessionTime);
 */
