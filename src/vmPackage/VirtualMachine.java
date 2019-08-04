package vmPackage;

import java.io.*;
import java.lang.*;

import static vmPackage.LexicalAnalyzer.*;
import static vmPackage.Parse.*;

public class VirtualMachine {

    private static final int MAX_MEMORY_SIZE = 500;
    private static final String FILEPATH = "src\\vmPackage\\mySubLC3.txt";

    private static String[] MEMORY = new String[MAX_MEMORY_SIZE];
    private static File f;

    public static String line;

    public static int PC;
    public static int MEM_COUNT;

    public static boolean HALT = false;


    public static void main (String args[]){
        //Output of Class Information
        System.out.println("Kevin Mitchell\n" +
                            "Programming Languages 8:00 A.M.\n" +
                            "SubLC3VM Project");
        printNewLine();
        System.out.println(FILEPATH);
        printNewLine();

        //Begin Load Phase
        loadProgramIntoMemory();
        //End Load Phase

        //Begin fetch-execute-cycle
        PC=0;
        while(!HALT)
        {
            //Fetch Instruction and increment PC
            line = MEMORY[PC];
            PC++;

            //Decode and execute Instructions
            //System.out.println("Parsing the Statement: " + line + "\n");
            decodeStatement();
        }
        //debugDumpMemory();
    }

    private static void loadProgramIntoMemory(){
        try {
            /* Check File */
            if ((f = new File(FILEPATH)) == null)
                System.out.println("Error - cannot open file 'mySubLC3.txt'");
            else {
                BufferedReader b = new BufferedReader(new FileReader(f));
                //Begin looping Lines
                MEM_COUNT =0;
                while ((line = b.readLine()) != null) {
                    MEMORY[MEM_COUNT] = line;
                    MEM_COUNT++;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }

    }
    private static void decodeStatement(){
            resetLine();
            getChar();
            lex(); //In LexicalAnalyzer.java
            decodeInstruction(); //In Parse.java
    }

    public static void writeToMemory(String identifier, Integer store){
        String[] memCheck = readMemory(identifier);
        if(memCheck[1] == null) {
            MEMORY[MEM_COUNT] = identifier + " " + store;
            MEM_COUNT++;
        }
        else{
            MEMORY[Integer.parseInt(memCheck[0])] = identifier + " " + store;
        }
    }

    public static String[] readMemory(String toFind){
        String[] rtn= new String[3];
        for (int i = 0; i<=MEMORY.length; i++) {
            try {
                if (MEMORY[i].startsWith(toFind.trim())) {
                    rtn[0] = Integer.toString(i);
                    rtn[1] = (MEMORY[i].split(" "))[1];
                }
            }
            catch(Exception e){}
        }
        return rtn;
    }
    private static void debugDumpMemory(){
        for (int a=0; a<=MEM_COUNT; a++) {
            System.out.println(MEMORY[a]);
        }
    }
}
