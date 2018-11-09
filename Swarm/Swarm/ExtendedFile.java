package Swarm;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by Adrian Szymowiat on 14.08.2018.
 * Klasa bedaca rozszerzeniem klasy File.
 */
public class ExtendedFile extends File{
    /**
     * Sciezka do pliku.
     */
    public String filePath;
    /**
     * Nazwa pliku.
     */
    public String fileName;
    /**
     * Konstruktor klasy.
     * @param pathname - scieżka do pliku.
     */
    public ExtendedFile(@NotNull String pathname) {
        super(pathname);
        filePath = this.getPath();
        fileName = this.getName();
    }
    /**
     * Metoda zwracajaca rozszerzenie pliku.
     * Nie dziala dla podwojnych rozszerzen
     * @return - rozszerzenie pliku w postaci String'a, np. txt
     */
    public String getExtension(){
        String fileExtension = null;
        boolean enoughFileLength = false;
        if(fileName.length() > 7){
            enoughFileLength = true;
        }
        if(enoughFileLength){
            if(fileName.substring(fileName.length()-7).equals(".tar.gz")) { //.tar.gz
                fileExtension = "tar.gz";
            }
        }else {
            int index;
            index = fileName.lastIndexOf('.');
            if (index > 0) {
                fileExtension = fileName.substring(index + 1);
            }
        }
        return fileExtension;
    }
    /**
     * Metoda sprawdzajaca, czy podane jako argument rozszerzenie jest takie samo jak dla pliku,
     * na rzecz ktorego jest wywolywana ta metoda.
     * @param extension - rozszerzenie w formie tekstu
     * @return - true/false; true - rozszerzenia zgodne, false - rozszerzenia niezgodne
     */
    public boolean equalExtension(String extension){
        boolean areExtensionsEqual = false;
        String fileExtension = this.getExtension();
        if(extension.equals(fileExtension)){
            areExtensionsEqual = true;
        }
        return areExtensionsEqual;
    }
    /**
     * Metoda sprawdzajaca, czy podany jako argument plik ma takie samo rozszerzenie jak plik,
     * na rzecz ktorego jest wywolywana ta metoda.
     * @param extendedFile - plik, ktorego rozszerzenie jest sprawdzane
     * @return - true/false; true - rozszerzenia zgodne, false - rozszerzenia niezgodne
     */
    public boolean equalExtension(ExtendedFile extendedFile){
        boolean areExtensionsEqual = false;
        String fileExtension1 = this.getExtension();
        String fileExtension2 = extendedFile.getExtension();
        if(fileExtension1.equals(fileExtension2)){
            areExtensionsEqual = true;
        }
        return areExtensionsEqual;
    }
    /**
     * Metoda sprawdzajaca, czy rozszerzenia podanych jako argumenty plikow sa takie same.
     * @param extendedFile1 - pierwszy plik
     * @param extendedFile2 - drugi plik
     * @return - true/false; true - rozszerzenia zgodne, false - rozszerzenia niezgodne
     */
    public static boolean equalExtension(ExtendedFile extendedFile1, ExtendedFile extendedFile2){
        boolean areExtensionsEqual = false;
        String fileExtension1 = extendedFile1.getExtension();
        String fileExtension2 = extendedFile2.getExtension();
        if(fileExtension1.equals(fileExtension2)){
            areExtensionsEqual = true;
        }
        return areExtensionsEqual;
    }
    /**
     * Metoda sprawdzajaca, czy plik pusty
     * @return - true/false; true - plik pusty, false - plik niepusty
     */
    public boolean isFileEmpty(){
        boolean isEmpty = false;
        if(this.length() == 0){
            isEmpty = true;
        }
        return isEmpty;
    }
    /**
     * Metoda sprawdzajaca, czy plik pusty
     * @param extendedFile - sprawdzany plik
     * @return - true/false; true - plik pusty, false - plik niepusty
     */
    public static boolean isFileEmpty(ExtendedFile extendedFile){
        boolean isEmpty = false;
        if(extendedFile.length() == 0){
            isEmpty = true;
        }
        return isEmpty;
    }
}

