import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
/**
 * Nth order integral calculator
 *
 * @author Lorenzo Wolzcko and Ryan Sundermeyer
 * @version 5/24/2022
 */
public class Eval
{
    private String expr; // input expression
    private ArrayList<String> arr = new ArrayList<String>(); // string arraylist for integral function
    
    /*
     * @param bounds input string
     * @return bounds 2D arraylist to be used by integral function
     */
    public static ArrayList<ArrayList<String>> getBoundsArray(String str)
    {
        String [] array = str.split(", ");
        ArrayList<ArrayList<String>> bounds = new ArrayList<ArrayList<String>>();
        for (int i=0; i<array.length; i++)
        {
            bounds.add(i, convertToArrayList(array[i]));
        }
        return bounds;
    }
    
    /*
     * @param double input value
     * @param int decimal places to round to
     * @return rounded version of input double
     */
    public static double round(double in, int dec)
    {
        final double DECIMALS = Math.pow(10, dec) * 1.0;
        return (Math.round((float)in*DECIMALS) / DECIMALS);
    }
    
    /*
     * @param String[] arr array of strings to be converted
     * @return ArrayList<String> arrayList version of input string array
     */
    public static ArrayList<String> convertToArrayList (String[] arr)
    {
        ArrayList<String> val = new ArrayList<String>();
        for (String s : arr)
            val.add(s);
        return val;
    }
    
    /*
     * @param initial input string
     * @return converted to arraylist version of input string which is converted to an array first
     */
    public static ArrayList<String> convertToArrayList (String s)
    {
        return convertToArrayList(s.split(" "));
    }
    
    /*
     * @param ArrayList<String> expr expression in array list form
     * @return recursive call to Evaluate
     */
    public static ArrayList<String> Evaluate(ArrayList<String> expr)
    {
        return Evaluate(expr, 0.0, 0.0);
    }
    /*
     * @param initial string expression
     * @return recursive call to Evaluate
     */
    public static ArrayList<String> Evaluate(String str)
    {
        return Evaluate(convertToArrayList(str), 0.0, 0.0);
    }
    /*
     * @param ArrayList<String> expr input expression in array list form
     * @param double x x-value
     * @param double y y-value
     * @return expr expression in readable form to be used by integral function
     */
    public static ArrayList<String> Evaluate(ArrayList<String> expr, double x, double y)
    {
        if (expr.size() == 1)
            return expr;
        while (expr.indexOf("pi") >= 0)
        {
            int index = expr.indexOf("pi");
            expr.set(index, "" + Math.PI);
        }
        while (expr.indexOf("(") >= 0)
        {
            String lastParen = "(";
            int firstIndex = expr.indexOf("(");
            int lastIndex = 0;
            for (int i = firstIndex+1; i < expr.size(); i++)
            {
                if (expr.get(i).equals(")"))
                    if (lastParen.equals("("))
                    {
                        lastIndex = i;
                        break;
                    }
                    else
                    {
                        firstIndex = i;
                        lastParen = ")";
                    }
                if (expr.get(i).equals("("))
                {
                    lastParen = "(";
                    firstIndex = i;
                }
            }
            ArrayList<String> parenExpr = new ArrayList<String>();
            int i = firstIndex + 1;
            while (i != lastIndex)
            {
                parenExpr.add(expr.remove(i));
                lastIndex--;
            }
            expr.remove(firstIndex+1);
            expr.set(firstIndex, Evaluate(parenExpr, x, y).get(0));
        }
        while (expr.indexOf("sin") >= 0 || expr.indexOf("cos") >= 0)
        {
            int index = Math.max(expr.lastIndexOf("sin"), expr.lastIndexOf("cos"));
            double arg;
            String op = expr.get(index);
            if (expr.get(index+1).equals("x"))
                arg = x;
            else if (expr.get(index+1).equals("y"))
                arg = y;
            else
                arg = Double.parseDouble(expr.get(index+1));
            if (op.equals("sin"))
                expr.set(index, "" + Math.sin(arg));
            if (op.equals("cos"))
                expr.set(index, "" + Math.cos(arg));
            expr.remove(index+1);
        }
        while (expr.indexOf("^") >= 0)
        {
            int index = expr.lastIndexOf("^");
            double first;
            double second;
            if (expr.get(index-1).equals("x"))
                first = x;
            else if (expr.get(index-1).equals("y"))
                first = y;
            else
                first = Double.parseDouble(expr.get(index-1));
            
            if (expr.get(index+1).equals("x"))
                second = x;
            else if (expr.get(index+1).equals("y"))
                second = y;
            else
                second = Double.parseDouble(expr.get(index+1));
            
            expr.set(index, "" + Math.pow(first, second));
            expr.remove(index-1);
            expr.remove(index);
        }
        while (expr.indexOf("*") >= 0 || expr.indexOf("/") >= 0)
        {
            int index;
            if (expr.indexOf("*") == -1)
                index = expr.indexOf("/");
            else if (expr.indexOf("/") == -1)
                index = expr.indexOf("*");
            else
                index = Math.min(expr.lastIndexOf("*"), expr.lastIndexOf("/"));
            String op = expr.get(index);
            double first;
            double second;
            if (expr.get(index-1).equals("x"))
                first = x;
            else if (expr.get(index-1).equals("y"))
                first = y;
            else
                first = Double.parseDouble(expr.get(index-1));
            
            if (expr.get(index+1).equals("x"))
                second = x;
            else if (expr.get(index+1).equals("y"))
                second = y;
            else
                second = Double.parseDouble(expr.get(index+1));
            if (op.equals("*"))
                expr.set(index, "" + first * second);
            if (op.equals("/"))
                expr.set(index, "" + first / second);
            expr.remove(index-1);
            expr.remove(index);
        }
        while (expr.indexOf("+") >= 0 || expr.indexOf("-") >= 0)
        {
            int index;
            if (expr.indexOf("+") == -1)
                index = expr.indexOf("-");
            else if (expr.indexOf("-") == -1)
                index = expr.indexOf("+");
            else
                index = Math.min(expr.lastIndexOf("+"), expr.lastIndexOf("-"));
            String op = expr.get(index);
            double first;
            double second;
            if (expr.get(index-1).equals("x"))
                first = x;
            else if (expr.get(index-1).equals("y"))
                first = y;
            else
                first = Double.parseDouble(expr.get(index-1));
            
            if (expr.get(index+1).equals("x"))
                second = x;
            else if (expr.get(index+1).equals("y"))
                second = y;
            else
                second = Double.parseDouble(expr.get(index+1));
            if (op.equals("+"))
                expr.set(index, "" + (first + second));
            if (op.equals("-"))
                expr.set(index, "" + (first - second));
            expr.remove(index-1);
            expr.remove(index);
        }
        return expr;
    }
    
    /*
     * @param int order order of the integral
     * @param ArrayList<String> expr expression to be evaluated
     * @param ArrayList<ArrayList<String>> bounds bounds of array in readable order (can include expressions)
     * @param int slices number of riemman sum rectangles
     * @param ArrayList<Double> prevValues previous values used for recursion
     * @return nth order integral solution
     */
    public static double integral (int order, ArrayList<String> expr, ArrayList<ArrayList<String>> bounds, int slices, ArrayList<Double> prevValues)
    {
        if (order == 0)
            return Double.parseDouble(Evaluate(expr).get(0));
        else
        {
            double ans = 0.0;
            
            //domain = the domain of all subintegrals of order (order-1)
            ArrayList<ArrayList<String>> domain;
            domain = (ArrayList<ArrayList<String>>) bounds.clone();
            for (int i = 0; i < domain.size(); i++)
                domain.set(i, (ArrayList<String>) domain.get(i).clone());
            domain.remove(domain.size()-1);
            domain.remove(domain.size()-1);
            
            //calculate the lower bound
            ArrayList<String> lowerBoundExpr = (ArrayList<String>) bounds.get(bounds.size()-2).clone();
            for (int i = 0; i < lowerBoundExpr.size(); i++)
            {
                if (lowerBoundExpr.get(i).indexOf("x")==0)
                {
                    int xNum = Integer.parseInt(lowerBoundExpr.get(i).substring(1));
                    double val = prevValues.get(xNum-order-1);
                    lowerBoundExpr.set(i, "" + val);
                }
            }
            double lowerBound = Double.parseDouble(Evaluate(lowerBoundExpr).get(0));
            
            //calculate the upper bound
            ArrayList<String> upperBoundExpr = (ArrayList<String>) bounds.get(bounds.size()-1).clone();
            for (int i = 0; i < upperBoundExpr.size(); i++)
            {
                if (upperBoundExpr.get(i).indexOf("x")==0)
                {
                    int xNum = Integer.parseInt(upperBoundExpr.get(i).substring(1));
                    double val = prevValues.get(xNum-order-1);
                    upperBoundExpr.set(i, "" + val);
                }
            }
            double upperBound = Double.parseDouble(Evaluate(upperBoundExpr).get(0));
            
            //iterate through the domain, calculating subintegrals of order (order-1) and summing to find the answer.
            double inc = (upperBound-lowerBound)*1.0/slices; //increment
            for (double val = lowerBound + inc / 2; val < upperBound; val += inc)
            {
                ArrayList<String> exprNew = (ArrayList<String>) expr.clone();
                for (int index = 0; index < exprNew.size(); index++)
                    if (exprNew.get(index).equals("x" + order))
                        exprNew.set(index, "" + (val));
                ArrayList<Double> newVals = (ArrayList<Double>) prevValues.clone();
                newVals.add((val));
                ans += inc * integral(order-1, exprNew, domain, slices, newVals);
            }
            
            return ans;
        }
    }
}
