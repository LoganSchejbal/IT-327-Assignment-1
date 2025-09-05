
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

class LL1{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        String string = input.nextLine();
        input.close();

        String exp = string+"$";
        StringTokenizer tokenString = new StringTokenizer(exp,"()+-*/$",true);

        ArrayList<String> tokens = new ArrayList<>();
        while(tokenString.hasMoreTokens()){
            tokens.add(tokenString.nextToken());
        }

        System.out.print("\n");
        for(int i = 0; i < tokens.size(); ++i){
            System.out.print(" | " + tokens.get(i));
        }

        //Start with E
        //E turns into T, EP
        tokens = parseE(tokens);

        //Now that the list has been reduced as far as we can, we check that the terminal symbol is the final symbol
        if(tokens.get(0).equals("$")){
            System.out.print("Valid: ");
            
            tokens = new ArrayList<>();
            while(tokenString.hasMoreTokens()){
                tokens.add(tokenString.nextToken());
            }

             tokens = parenthese(tokens);
        }
        else{
            System.out.print("Invalid");
        }
    }

    public static ArrayList<String> parseE(ArrayList<String> tokens){
        tokens = parseT(tokens);
        tokens = parseEP(tokens);
        return tokens;
    }

    public static ArrayList<String> parseT(ArrayList<String> tokens){
        tokens = parseF(tokens);
        tokens = parseTP(tokens);

        return tokens;
    }

    public static ArrayList<String> parseF(ArrayList<String> tokens){
        if (tokens.get(0).equals("(")){
            tokens.remove(0);
            tokens = parseE(tokens);
            tokens.remove(0);
        }
        else if (Character.isDigit(tokens.get(0).charAt(0))){
            tokens.remove(0);
        }
        else{
            System.out.print("Invalid");
            System.exit(0);
        }

        return tokens;
    }

    public static ArrayList<String> parseEP(ArrayList<String> tokens){
        if(tokens.get(0).equals("-") || tokens.get(0).equals("+")){
            tokens.remove(0);

            tokens = parseT(tokens);
            tokens = parseEP(tokens);
        }
        else{

        }

        return tokens;
    }

    public static ArrayList<String> parseTP(ArrayList<String> tokens){
        if(tokens.get(0).equals("*") || tokens.get(0).equals("/")){
            tokens.remove(0);
            tokens = parseF(tokens);
            tokens = parseTP(tokens);
        }
        else{

        }

        return tokens;
    }

    public static ArrayList<String> parenthese(ArrayList<String> tokens){
        int leftParentheseScope = -1, rightParentheseScope = -1;
        ArrayList<String> subString = tokens; // Initialize subString to tokens by default
        // Find the first '('
        for(int i = 0; i < tokens.size(); ++i){
            if (tokens.get(i).equals("(")){
                leftParentheseScope = i;
                break;
            }
        }

        // Find the first ')' after the first '('
        if (leftParentheseScope != -1) {
            for(int i = leftParentheseScope + 1; i < tokens.size(); ++i){
                if (tokens.get(i).equals(")")){
                    rightParentheseScope = i;
                    break;
                }
            }
        }

        if (leftParentheseScope != -1 && rightParentheseScope != -1){
            subString = new ArrayList<>(tokens.subList(leftParentheseScope, rightParentheseScope));

            System.out.print("\n");
            for (int i = 0; i < subString.size(); ++i){
                System.out.print(subString.get(i) + " | ");
            }
            
            subString = parenthese(subString);
        }
        else {
            //subString = multiplyAndDivide();
        }
        

        return subString;
    }

}