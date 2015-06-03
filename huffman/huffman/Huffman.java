/*
 * Huffman.java
 *
 * Created on May 21, 2007, 1:01 PM
 */

package huffman;
import java.io.*;
import java.lang.*;
import java.lang.reflect.Array;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author pbladek
 */
public class Huffman
{  
    public static final int CHARMAX = 128;
    public static final byte CHARBITS = 7;
    public static final short CHARBITMAX = 128;
    private HuffmanTree<Character> theTree;
    private byte[] byteArray;
    private SortedMap<Character, String> keyMap;
    private SortedMap<String, Character> codeMap;
    HuffmanChar[] charCountArray;
    byte[] saveDataArray;
    
    /**
     * Creates a new instance of Main
     */
    public Huffman() {}
    
    /**
     * main
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
//----------------------------------------------------
// used for debugging encoding
//----------------------------------------------------
//        args = new String[1];
//        args[0] = "alice.txt";
//----------------------------------------------------
// used for debugging encoding
//----------------------------------------------------
//        args = new String[2];
//        args[0] = "-d";
//        args[1] = "alice.txt";  
//----------------------------------------------------        
        boolean decode = false;
        String textFileName = "";
        if(args.length > 0)
        {
            if(args[0].substring(0,2).toLowerCase().equals("-d"))
            {
                decode = true;
                if(args.length > 1)
                    textFileName = args[1];
            }
            else
                textFileName = args[0];
        }
        Huffman coder = new Huffman();
        if(!decode)
            coder.encode(textFileName);
        else
            coder.decode(textFileName);
    }

    /*
     * encode
     * @param fileName the file to encode
     */
    public void encode(String fileName)
    {
        File inputFile = new File(fileName);
            if (!((inputFile.exists()) && (inputFile.canRead()))) {
                inputFile = getFile();
                fileName =inputFile.getName();
            }
            else {
            inputFile = getFile();
            fileName = inputFile.getName();  
        }
        int[] counter = new int[CHARMAX];  
        Scanner in = null;
        List<HuffmanChar> countList = new ArrayList<>();
        try
        {
          in = new Scanner(inputFile);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File open error");
            return;
        }
        while(in.hasNextLine())
        {
            String line = in.nextLine();
            line += "\n";
            for (int i = 0; i < line.length(); i++){
                Character a = line.charAt(i);
                counter[a]++;
            }
            
        }
        for(int i = 0; i < counter.length; i ++)
        {
            if(counter[i] > 0)
            {
                countList.add(new HuffmanChar((char)i, counter[i]));
            }
        }
        
        charCountArray = (HuffmanChar[]) countList.toArray();
        countList = null;
        Arrays.sort(charCountArray);
        theTree = new HuffmanTree<>(charCountArray);
        keyMap = theTree.getCodeMap();
        
        try
        {
          in = new Scanner(inputFile);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File open error");
        }
        List<Byte> arrayList = new ArrayList<>();
        Character character;
        String output = null;
        String outputByte = null;
        int byteCounter = 0;
        while(in.hasNextLine())
        {
             String line = in.nextLine();
             line += "\n";
             for(int i = 0; i < line.length(); i++)
             {
                 character = line.charAt(i);
                 try{
                     output += keyMap.get(character);
                 }
                 catch(NullPointerException e)
                 {
                     System.out.println("The value not exist");
                     return;
                 }
                 while(output.length() > CHARBITS)
                 {
                     outputByte = output.substring(0,8);
                     if(output.length() > 8)
                         output = output.substring(8);
                     else
                         output = "";
                     arrayList.add((byte)Integer.parseInt(outputByte));
                 }
             }
            
        }
        while(output.length() != 0 && output.length() < 8)
            output += "0";
        arrayList.add((byte)Integer.parseInt(outputByte));
        byteArray = toArray(arrayList);
        arrayList = null;
        
        
        


        writeEncodedFile(byteArray, fileName);
        writeKeyFile(fileName);
    } 
    /**
     *Takes List and converts it to primitive byte array
     * @param arrayList of Byte to convert
     * @return byte[] array
     */
    public byte[] toArray(List<Byte> arrayList)
    {
        byte[] bytesArray = new byte[arrayList.size()];
        for( int i = 0; i < arrayList.size(); i++ )
        {
            bytesArray[i] = arrayList.get(i);
        }
        return bytesArray;
    }
    /*
     * decode
     * @param inFileName the file to decode
     */   
    public void decode(String inFileName)
    { 
     
    }
      
    /**
     * writeEncodedFile
     * @param bytes bytes for file
     * @param fileName file input
     */ 
    public void writeEncodedFile(byte[] bytes, String fileName)
    {
      

    }
   
    /**
     * writeKeyFile
     * @param fileName the name of the file to write to
     */
    public void writeKeyFile(String fileName)
    {
        
    }
         /**
     * The method to get the file.
     * @return the selected file
     */
    private static File getFile(){
         String inputFileName = "x";
        File inputFile = new File(inputFileName);
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter exten = new FileNameExtensionFilter("Text Document", "txt");
        chooser.setFileFilter(exten);
        chooser.setCurrentDirectory(inputFile.getAbsoluteFile()
                .getParentFile());
        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION){
            inputFile = chooser.getSelectedFile();
            if(!inputFile.exists() || !inputFile.canRead()){
                JOptionPane.showMessageDialog(null,"Can not read this file,"
                        + " please try another");
                getFile();
            }
        }
        return inputFile;
    }
 
}


