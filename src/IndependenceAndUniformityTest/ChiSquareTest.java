/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IndependenceAndUniformityTest;

import Solution4.LeeYeapTheam;


/**
 *
 * @author Yeap Theam
 */

public class ChiSquareTest {
    static double[][] criticalValues = {{6.63,3.84,2.71}
                                ,{9.21,5.99,4.61}
                                ,{11.34,7.81,6.25}
                                ,{13.28,9.49,7.78}
                                ,{15.1,11.1,9.2}
                                ,{16.8, 12.6, 10.6}
                                ,{18.5, 14.1, 12.0}
                                ,{20.1, 15.5, 13.4}
                                ,{21.7, 16.9, 14.7}
                                ,{23.2, 18.3, 16.0}
                                ,{37.6,31.4,28.4}
                                ,{50.9, 43.8, 40.3}
                                ,{63.7, 55.8, 51.8}
                                ,{76.2, 67.5, 63.2}
    };
    static int interval;
    static int[] Oi, OiMinusEi, OiMinusEiSquare;
    static double[] OiMinusEiSquareOverEi;
    static int Ei;
    final static double alpha = 0.05;
    static int n;
    static double totalOiMinusEiSquareOverEi;
    static int size;
    static double firstInterval, secondInterval;
    public static void initialize(double[] rand){
        interval = 10;
        Oi = new int[interval];
        OiMinusEi = new int[interval];
        OiMinusEiSquare = new int[interval];
        OiMinusEiSquareOverEi = new double[interval];
        totalOiMinusEiSquareOverEi = 0;
        Ei = rand.length / interval;
        n = interval - 1;
        firstInterval = 0;
        secondInterval = 0.1;
    }
    
    public static void chisquare(double[] rand){
        countOi(rand);
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("|\tOi\t|\tEi\t|     Oi-Ei\t|\t(Oi-Ei)^2\t|\t(Oi-Ei)^2/Ei\t|");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for(int a=0;a<Oi.length;a++){
            OiMinusEi[a] = Oi[a] - Ei;
            OiMinusEiSquare[a] = OiMinusEi[a]*OiMinusEi[a];
            OiMinusEiSquareOverEi[a] = (double)OiMinusEiSquare[a]/Ei;
            System.out.println("|\t"+Oi[a]+"\t|\t"+Ei+"\t|\t"+OiMinusEi[a]+"\t|\t   "+OiMinusEiSquare[a]+"\t\t|\t   "+OiMinusEiSquareOverEi[a]+"  \t|");
            totalOiMinusEiSquareOverEi+=OiMinusEiSquareOverEi[a];
            
        }
        System.out.printf("\t\t\t\t\t\t\t\t\t Total:    %.2f\n",totalOiMinusEiSquareOverEi);
        double criticalValue = 0;
        if(n>0 && n<11){
            if(alpha == 0.01)
                criticalValue = criticalValues[n-1][0];
            if(alpha==0.05)
                criticalValue = criticalValues[n-1][1];
            if(alpha==0.10)
                criticalValue = criticalValues[n-1][2];
                
        }
        else if(n==20){
            if(alpha == 0.01)
                criticalValue = criticalValues[10][0];
            if(alpha==0.05)
                criticalValue = criticalValues[10][1];
            if(alpha==0.10)
                criticalValue = criticalValues[10][2];
        }
        else if(n==30){
            if(alpha == 0.01)
                criticalValue = criticalValues[11][0];
            if(alpha==0.05)
                criticalValue = criticalValues[11][1];
            if(alpha==0.10)
                criticalValue = criticalValues[11][2];
        }
        
        else if(n==40){
            if(alpha == 0.01)
                criticalValue = criticalValues[12][0];
            if(alpha==0.05)
                criticalValue = criticalValues[12][1];
            if(alpha==0.10)
                criticalValue = criticalValues[12][2];
        }
        
        else if(n==50){
            if(alpha == 0.01)
                criticalValue = criticalValues[13][0];
            if(alpha==0.05)
                criticalValue = criticalValues[13][1];
            if(alpha==0.10)
                criticalValue = criticalValues[13][2];
        }
        
        if(criticalValue>totalOiMinusEiSquareOverEi){
            System.out.printf("Critical Value: %.2f > X^2 : %.2f, hypothesis is accepted\nThe set of random numbers passed uniformity test.\n", criticalValue, totalOiMinusEiSquareOverEi);
        }
        else
            System.out.printf("Critical Value: %.2f <= X^2 : %.2f, hypothesis is not accepted\nThe set of random numbers did not pass uniformity test.\n", criticalValue, totalOiMinusEiSquareOverEi);
    }
    

    public static void countOi(double[] rand){
        initialize(rand);
        for(int a=0;a<interval;a++){
            for(int b=0;b<rand.length;b++){
                if(rand[b]>firstInterval && rand[b]<= secondInterval)
                    Oi[a]++;
            }
        firstInterval += 0.1;
        secondInterval += 0.1;
        }
    }
}
