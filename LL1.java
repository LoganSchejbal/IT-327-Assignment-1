
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

class LL1{
    public static void main(String[] args){

        //This is the input, saved as "string"
        Scanner input = new Scanner(System.in);
        String string = input.nextLine();
        input.close();

        //String is converted to tokens
        String exp = string+"$";
        StringTokenizer tokenString = new StringTokenizer(exp,"()+-*/$",true);

        //tokens are given to an array
        ArrayList<String> tokens = new ArrayList<>();
        while(tokenString.hasMoreTokens()){
            tokens.add(tokenString.nextToken());
        }

        //tokens are backed up so they can be used to compute the expression
        ArrayList<String> tokensSecondary = new ArrayList<>(tokens);

        //Calls parsing functions
        //Start with E
        //E turns into T, EP
        tokens = parseE(tokens);

 

        //Now that the list has been reduced as far as we can, we check that the terminal symbol is the final symbol
        if(!tokens.isEmpty()){
            if(tokens.get(0).equals("$")){
                System.out.print("Valid: ");
                
                //Reverts tokens to original with the back up copy
                //then we pass into parentheses to start calculating a total
                tokens = new ArrayList<>(tokensSecondary);
                tokens = parenthese(tokens);
                
                //End of program assuming the expression was valid. prints total
                System.out.print(tokens.get(0));
            }

            //These elses detect if something wrong happened and exits the program with an invalid expression
            else{
                System.out.print("Invalid Expression");
                System.exit(0);
            }
        }
        else{
            System.out.print("Invalid Expression");
            System.exit(0);
        }
    }

    public static ArrayList<String> parseE(ArrayList<String> tokens){
        //Just calls other functions and it works its way down through recursion
        tokens = parseT(tokens);
        tokens = parseEP(tokens);
        return tokens;
    }

    public static ArrayList<String> parseT(ArrayList<String> tokens){
        //functionally the same as above
        tokens = parseF(tokens);
        tokens = parseTP(tokens);

        return tokens;
    }

    public static ArrayList<String> parseF(ArrayList<String> tokens){
        //We must check if the array is empty first so we don't get out of bounds exceptions
        if(!tokens.isEmpty()){
            //decides if we need a parenthese set or a double. if neither, expression is invalid
            if (tokens.get(0).equals("(")){
                tokens.remove(0);
                tokens = parseE(tokens);
                tokens.remove(0);
            }
            else if (Character.isDigit(tokens.get(0).charAt(0))){
                tokens.remove(0);
            }
            else{
                System.out.print("Invalid Expression");
                System.exit(0);
            }
        }
        else{
            System.out.print("Invalid Expression");
            System.exit(0);
        }

        return tokens;
    }

    public static ArrayList<String> parseEP(ArrayList<String> tokens){
        //Again check if we don't have anything
        if(!tokens.isEmpty()){
            //Checks if we are doing an operation
            //doesnt matter which one as we don't evaluate here
            //If its not, we assume a lambda character (Blank)
            if(tokens.get(0).equals("-") || tokens.get(0).equals("+")){
                tokens.remove(0);

                tokens = parseT(tokens);
                tokens = parseEP(tokens);
            }
            else{

            }
        }
        else{
            System.out.print("Invalid Expression");
            System.exit(0);
        }

        return tokens;
    }

    public static ArrayList<String> parseTP(ArrayList<String> tokens){
        if(!tokens.isEmpty()){
            //Same as above but different operations
            if(tokens.get(0).equals("*") || tokens.get(0).equals("/")){
                tokens.remove(0);
                tokens = parseF(tokens);
                tokens = parseTP(tokens);
            }
            else{

            }
        }
        else{
            System.out.print("Invalid Expression");
            System.exit(0);
        }

        return tokens;
    }

    public static ArrayList<String> parenthese(ArrayList<String> tokens){
        //Initialize to -1 so we can see if we found a parenthese in the equation
        int leftParentheseScope = -1, rightParentheseScope = -1;
        ArrayList<String> subString = tokens;
        // Find the first (
        for(int i = 0; i < tokens.size(); ++i){
            if (tokens.get(i).equals("(")){
                leftParentheseScope = i;
                break;
            }
        }

        // Find the last )
        if (leftParentheseScope != -1) {
            for(int i = leftParentheseScope + 1; i < tokens.size(); ++i){
                if (tokens.get(i).equals(")")){
                    rightParentheseScope = i;
                }
            }
        }

        //If statement to determine if we are done with checking for parentheses
        if (leftParentheseScope != -1 && rightParentheseScope != -1){
            //Creates a new array between the parentheses
            subString = new ArrayList<>(tokens.subList(leftParentheseScope, rightParentheseScope));
            //Removes the first parenthese as we don't want it anymore
            subString.remove(0);

            //Recurses again to check for more
            subString = parenthese(subString);

            //Puts result inside the parentheses inside tokens to further evaluate
            tokens.set(leftParentheseScope, subString.get(0));
            for(int i = rightParentheseScope; i > leftParentheseScope; --i){
                tokens.remove(i);
            }

            //Actual evaluating part of the function
            //Goes to addition and subtraction after to follow PEMDAS
            tokens = multiplyAndDivide(tokens);
        }
        else {
            
        }
        
        //Evaluates inside a set of parentheses. 
        subString = multiplyAndDivide(subString);
        
        return tokens;
    }

    public static ArrayList<String> multiplyAndDivide(ArrayList<String> tokens){
        //Basically just checks each spot to see if we need to do an operation here
        //If we fo it goes to the double before and the double after and does it
        for (int i = 0; i < tokens.size(); ++i){
            if (tokens.get(i).equals("*")){
                double left = Double.parseDouble(tokens.get(i-1));
                double right = Double.parseDouble(tokens.get(i+1));
                double result = left * right;
                tokens.set(i-1, String.valueOf(result));
                tokens.remove(i);
                tokens.remove(i);
                --i;
            }

            if (tokens.get(i).equals("/")){
                double left = Double.parseDouble(tokens.get(i-1));
                double right = Double.parseDouble(tokens.get(i+1));
                double result = left / right;
                tokens.set(i-1, String.valueOf(result));
                tokens.remove(i);
                tokens.remove(i);
                --i;
            }
        }

        //now we add and subtrct
        tokens = addAndSubtract(tokens);

        return tokens;
    }

    public static ArrayList<String> addAndSubtract(ArrayList<String> tokens){
        //Same thing here too, just different signs
        for (int i = 0; i < tokens.size(); ++i){
            if (tokens.get(i).equals("+")){
                double left = Double.parseDouble(tokens.get(i-1));
                double right = Double.parseDouble(tokens.get(i+1));
                double result = left + right;
                tokens.set(i-1, String.valueOf(result));
                tokens.remove(i);
                tokens.remove(i);
                --i;
            }

            if (tokens.get(i).equals("-")){
                double left = Double.parseDouble(tokens.get(i-1));
                double right = Double.parseDouble(tokens.get(i+1));
                double result = left - right;
                tokens.set(i-1, String.valueOf(result));
                tokens.remove(i);
                tokens.remove(i);
                --i;
            }
        }
        return tokens;
    }
}