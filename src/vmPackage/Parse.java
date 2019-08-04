package vmPackage;

//Imports the functions from my Lexical Analyzer
//The 'main' function was placed in there
import java.util.Scanner;

import static vmPackage.LexicalAnalyzer.*;
import static vmPackage.VirtualMachine.*;

public class Parse {

    /*
        INT_LIT
        IDENT
        COMMENT_OP
        STRING_OP
    */

    public static void decodeInstruction(){
        switch (getNextToken()){
            case ADD_OP:
                additionOperation();
                break;
            case SUB_OP:
                subtractionOperation();
                break;
            case MULT_OP:
                multiplicationOperation();
                break;
            case DIV_OP:
                divisionOperation();
                break;
            case BREAK_POSITIVE:
                breakPosOperation();
                break;
            case BREAK_NEGATIVE:
                breakNegOperation();
                break;
            case BREAK_ZERO:
                breakZeroOperation();
                break;
            case BREAK_ZERO_NEGATIVE:
                breakNegZeroOperation();
                break;
            case BREAK_ZERO_POSITIVE:
                breakPosZeroOperation();
                break;
            case JUMP_OP:
                jumpOperation();
                break;
            case INPUT_OP:
                inputOperation();
                break;
            case OUTPUT_OP:
                outputOperation();
                break;
            case ASSIGN_OP:
                assignOperation();
                break;
            case HALT_OP:
                break;
            default:
                //System.out.println("Ignoring");
                break;
        }
    }

    //ADD Destination Source1 Source2
    private static void additionOperation() {
        String Destination;
        int Source1, Source2;

        try {
            lex();
            Destination = (new String(lexeme)).trim();

            lex();
            if (getNextToken() == INT_LIT)
                Source1 = Integer.parseInt(new String(lexeme));
            else
                Source1 = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);

            lex();
            if (getNextToken() == INT_LIT)
                Source2 = Integer.parseInt(new String(lexeme));
            else
                Source2 = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);

            int sum = Source1 + Source2;
            VirtualMachine.writeToMemory(Destination, sum);
        }catch (Exception e){
            System.out.println("Unexpected Error: " + e);
        }
    }
    //SUB Destination Source1 Source2
    private static void subtractionOperation() {
        String Destination;
        int Source1, Source2;

        try {
            lex();
            Destination = (new String(lexeme)).trim();

            lex();
            if (getNextToken() == INT_LIT)
                Source1 = Integer.parseInt(new String(lexeme));
            else
                Source1 = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);

            lex();
            if (getNextToken() == INT_LIT)
                Source2 = Integer.parseInt((new String(lexeme)).trim());
            else
                Source2 = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);

            int sub = Source1 - Source2;
            VirtualMachine.writeToMemory(Destination, sub);
        }catch (Exception e){
            System.out.println("Unexpected Error: " + e);
        }

    }
    //ADD Destination Source1 Source2
    private static void divisionOperation() {
        String Destination;
        int Source1, Source2;

        try {
            lex();
            Destination = (new String(lexeme)).trim();

            lex();
            if (getNextToken() == INT_LIT)
                Source1 = Integer.parseInt(new String(lexeme));
            else
                Source1 = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);

            lex();
            if (getNextToken() == INT_LIT)
                Source2 = Integer.parseInt(new String(lexeme));
            else
                Source2 = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);

            int sum = Source1 / Source2;
            VirtualMachine.writeToMemory(Destination, sum);
        }catch (Exception e){
            System.out.println("Unexpected Error: " + e);
        }
    }
    private static void multiplicationOperation() {
        String Destination;
        int Source1, Source2;

        try {
            lex();
            Destination = (new String(lexeme)).trim();

            lex();
            if (getNextToken() == INT_LIT)
                Source1 = Integer.parseInt(new String(lexeme));
            else
                Source1 = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);

            lex();
            if (getNextToken() == INT_LIT)
                Source2 = Integer.parseInt(new String(lexeme));
            else
                Source2 = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);

            int sum = Source1 * Source2;
            VirtualMachine.writeToMemory(Destination, sum);
        }catch (Exception e){
            System.out.println("Unexpected Error: " + e);
        }
    }

    //OUT Value
    private static void outputOperation(){
        String Value;
        lex();
        Value = print;
        System.out.println(Value);
        print="";
    }

    //IN Variable
    private static void inputOperation(){
        String Variable;
        int input;

        lex();
        Variable = (new String(lexeme)).trim();
        Scanner sc = new Scanner(System.in);
        input = sc.nextInt();

        VirtualMachine.writeToMemory(Variable, input);
    }

    //STO Destination Source
    private static void assignOperation(){
        String Destination;
        int Source = 0;

        lex();
        Destination = (new String(lexeme).trim());

        lex();
        if(getNextToken() == INT_LIT)
            Source = Integer.parseInt((new String(lexeme)).trim());
        else if (getNextToken() == IDENT){
            Source = Integer.parseInt(VirtualMachine.readMemory((new String(lexeme)).trim())[1]);
        }
        else
            System.out.println("No integer given." +
                    "");

        VirtualMachine.writeToMemory(Destination, Source);
    }

    //JMP Label
    private static void jumpOperation(){
        lex();
        PC = Integer.parseInt(readMemory((new String(lexeme)))[0]);
    }

    //BRn Variable Label
    private static void breakNegOperation(){
        int Variable;
        String Label;

        lex();
        Variable = Integer.parseInt(readMemory((new String(lexeme)).trim())[1]);

        lex();
        Label = new String(lexeme);

        if(Variable < 0){
            PC = Integer.parseInt(readMemory(Label)[0]);
        }
    }

    //BRnz Variable Label
    private static void breakNegZeroOperation(){
        int Variable;
        String Label;

        lex();
        Variable = Integer.parseInt(readMemory((new String(lexeme)))[1]);

        lex();
        Label = new String(lexeme);

        if(Variable <= 0){
            PC = Integer.parseInt(readMemory(Label)[0]);
        }
    }

    //BRp Variable Label
    private static void breakPosOperation(){
        int Variable;
        String Label;

        lex();
        Variable = Integer.parseInt(readMemory((new String(lexeme)))[1]);

        lex();
        Label = new String(lexeme);

        if(Variable > 0){
            PC = Integer.parseInt(readMemory(Label)[0]);
        }
    }
    //BRpz Variable Label
    private static void breakPosZeroOperation(){
        int Variable;
        String Label;

        lex();
        Variable = Integer.parseInt(readMemory((new String(lexeme)))[1]);

        lex();
        Label = new String(lexeme);

        if(Variable >= 0){
            PC = Integer.parseInt(readMemory(Label)[0]);
        }
    }
    //BRpz Variable Label
    private static void breakZeroOperation(){
        int Variable;
        String Label;

        lex();
        Variable = Integer.parseInt(readMemory((new String(lexeme)))[1]);

        lex();
        Label = new String(lexeme);

        if(Variable == 0){
            PC = Integer.parseInt(readMemory(Label)[0]);
        }
    }

}
