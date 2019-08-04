package vmPackage;

import java.io.*;
import java.lang.*;

public class LexicalAnalyzer{

    /*Global Declarations*/
    /*Constants*/
    private static final int LEXLEN = 100;
    private static final int LINELENGTH = 80;


    /*Variables*/
    private static String charClass;

    private static String nextToken;
    public static String getNextToken() {
        return nextToken;
    }
    public static String print;
    private static int cIndex = 0;
    private static int lexLen;
    public static boolean stringOP = false;

    public static char[] lexeme = new char[LEXLEN];
    private static char nextChar;

    //Character Classes
    private static final String LETTER = "LETTER";
    private static final String DIGIT = "DIGIT";
    private static final String UNKNOWN = "";

    //Token Codes
    public static final String INT_LIT = "INT_LIT";
    public static final String IDENT = "IDENT";
    public static final String ASSIGN_OP = "ASSIGN_OP";
    public static final String ADD_OP = "ADD_OP";
    public static final String SUB_OP = "SUB_OP";
    public static final String MULT_OP = "MULT_OP";
    public static final String DIV_OP = "DIV_OP";
    public static final String COMMENT_OP = "COMMENT_OP";
    public static final String BREAK_NEGATIVE = "BREAK_NEGATIVE";
    public static final String BREAK_POSITIVE = "BREAK_POSITIVE";
    public static final String BREAK_ZERO = "BREAK_ZERO";
    public static final String BREAK_ZERO_NEGATIVE = "BREAK_ZERO_NEGATIVE";
    public static final String BREAK_ZERO_POSITIVE = "BREAK_ZERO_POSITIVE";
    public static final String INPUT_OP = "INPUT_OP";
    public static final String JUMP_OP = "JUMP_OP";
    public static final String OUTPUT_OP = "OUTPUT_OP";
    public static final String HALT_OP = "HALT_OP";
    public static final String STRING_OP = "STRING_OP";

    public static final String EOL = "END_OF_LINE";

    /*Begin Controllers*/

    //Manages output and lookup dispatching
    public static int lex(){
        lexLen=0;
        lexeme = new char[LINELENGTH];
        getNonBlank();
        switch (charClass){
            case LETTER:
                addChar();
                getChar();
                while(charClass == LETTER || charClass == DIGIT){
                    addChar();
                    getChar();
                }
                nextToken = LookupCommand(lexeme);
                break;
            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT){
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;
            case UNKNOWN:
                lookupToken(nextChar);
                getChar();
                break;
            case COMMENT_OP:
                nextToken = EOL;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'L';
                lexeme[3] = 0;
                break;
        }
        //outputLine();
        if (getNextToken() == STRING_OP) {
            nextToken = "STRING_LIT";
            buildString();
        }
        if (getNextToken() == IDENT){
            try{
                print = VirtualMachine.readMemory((new String(lexeme)).trim())[1];
            }catch(Exception e){}
        }
        return 0;
    }

    public static String LookupCommand(char[] token){

        String str = (new String(token)).trim();
        switch (str) {
            case "ADD":
                nextToken = ADD_OP;
                break;
            case "SUB":
                nextToken = SUB_OP;
                break;
            case "DIV":
                nextToken = DIV_OP;
                break;
            case "MUL":
                nextToken = MULT_OP;
                break;
            case "BRn":
                nextToken = BREAK_NEGATIVE;
                break;
            case "BRp":
                nextToken = BREAK_POSITIVE;
                break;
            case "BRz":
                nextToken = BREAK_ZERO;
                break;
            case "BRzn":
                nextToken = BREAK_ZERO_NEGATIVE;
                break;
            case "BRzp":
                nextToken = BREAK_ZERO_POSITIVE;
                break;
            case "IN":
                nextToken = INPUT_OP;
                break;
            case "JMP":
                nextToken = JUMP_OP;
                break;
            case "OUT":
                nextToken = OUTPUT_OP;
                break;
            case "STO":
                nextToken = ASSIGN_OP;
                break;
            case "HALT":
                nextToken = HALT_OP;
                VirtualMachine.HALT = true;
                break;
            default:
                nextToken = IDENT;
        }

        return nextToken;
    }


    /*Begin Models*/

    //adds next character to lexeme
    private static void addChar(){
        if (lexLen <= 98){
            lexeme[lexLen++]=nextChar;
            lexeme[lexLen]=0;
        }
        else{
            System.out.println("Error - lexeme is too long");
        }
    }
    //grabs next character and assigns character class
    public static void getChar(){
        if (checkExist()){
            if (isAlpha(nextChar)){
                charClass = LETTER;
            }
            else if (isDigit(nextChar)){
                charClass = DIGIT;
            }
            else{
                charClass = UNKNOWN;
            }
        }
        else {
            charClass = EOL;
        }
    }
    //Returns the index of the next non-blank character in array
    private static void getNonBlank() {
        while (isWhitespace(nextChar))
        {getChar();}
    }

    //Fetches and assigns token identifier
    private static String lookupToken(char ch){
        switch (ch) {
            case ';':
                addChar();
                nextToken = COMMENT_OP;
                break;
            case '"':
                stringOP = !stringOP;
                addChar();
                nextToken = STRING_OP;
                break;
            default:
                addChar();
                nextToken = IDENT;
        }
        return nextToken;
    }

    //checks if Alpha, Digit, or whitespace respectively
    private static boolean isAlpha(char ch){
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }
    private static boolean isDigit(char ch){
        return ch >= '0' && ch <= '9';
    }
    private static boolean isWhitespace(char ch){
        return (ch == ' ') || (ch == '\n') || (ch == '\t');
    }

    //Checks the existence of the character at the next index.
    private static boolean checkExist() throws java.lang.StringIndexOutOfBoundsException{
        try {
            nextChar = VirtualMachine.line.charAt(cIndex++);
            return true;
        }
        catch(java.lang.StringIndexOutOfBoundsException e){
            return false;
        }
    }

    //Used to reset line variables in between reads
    public static void resetLine(){
        cIndex = 0;
        lexeme = new char[LEXLEN];
    }

    //Builds string until stringOP lock is released
    public static void buildString(){
        StringBuilder build = new StringBuilder();
        cIndex--;
        stringOP = true;
        while (stringOP) {
            build.append(VirtualMachine.line.charAt(cIndex++));
            if (VirtualMachine.line.charAt(cIndex) == '"') {
                stringOP = false;
            }
        }
        print = build.toString();
        stringOP = true;
    }

    /*Begin Views*/

    //Just prints a line of * with length equal to LINELENGTH
    public static void printNewLine(){
        String rtn ="";
        for (int i=0;i<LINELENGTH; i++)
            rtn += "*";
        System.out.println(rtn);
    }

    //outputs a formatted line
    public static void outputLine(){
        if (nextToken == EOL){
            /* Uncomment this block to add an end of line Token
            String output = "Next token is: " + nextToken;
            output += padRight(output);
            output += "Next lexeme is ";
            for (char t :
                    lexeme) {
                output += t;
            }
            System.out.println(output);*/
        }


        else {
            String output = "Next token is: " + nextToken;
            output += padRight(output);
            output += "Next lexeme is ";
            for (char t :
                    lexeme) {
                output += t;
            }
            System.out.println(output);
        }
    }

    //provides padding to the outputed lines
    private static String padRight(String line){
        String rtn = "";
        while ((line.length()+rtn.length()) <= LINELENGTH/2){
            rtn+=" ";
        }
        return rtn;
    }
}