/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IndependenceAndUniformityTest;

import static java.lang.Math.sqrt;
import java.util.Arrays;

/**
 *
 * @author Yeap Theam
 */
public class TestForAutocorrelation {
    static int m, N, l, i;
    static double[] randomNumbers;
    static double[] zTable={1.645, 1.96, 2.326, 2.576};
    static double sumOfRandxRand, P, O, Z, Zalpha, alpha = 0.05;
    static int lastRandom, maximumRand;
    public static void initialize(double[] random){
        i = 2;
        N=100;
        l = 6;
        m = ((N-i)/l)-1;
        maximumRand = (N-i) / l;
        lastRandom = maximumRand*(l);
        randomNumbers = Arrays.copyOf(random, random.length);
        sumOfRandxRand = 0;
    }
    
    public static void testForAutocorrelation(double[] random){
        initialize(random);
        for(int a=i-1;a<lastRandom;a+=l){
            sumOfRandxRand+=randomNumbers[a]*randomNumbers[a+l];
        }
        P = ((1/(double)(m+1))*sumOfRandxRand) - 0.25;
        O = (sqrt(13*m + 7)) / (12*(m+1)); 
        Z = P / O;
        if(alpha == 0.1)
            Zalpha = zTable[0];
        else if(alpha == 0.05)
            Zalpha = zTable[1];
        else if(alpha == 0.02)
            Zalpha = zTable[2];
        else if(alpha == 0.01)
            Zalpha = zTable[3];
        if(Z>=-Zalpha && Z <= Zalpha){
            System.out.printf("\n%.2f <= %.4f <= %.2f is true\n", -Zalpha, Z, Zalpha);
            System.out.println("Accept hypothesis at Z = "+Z);
            System.out.println("The set of random numbers passed independence test.\n");
        }
        else{
            System.out.printf("\n%.2f <= %.4f <= %.2f is false\n", -Zalpha, Z, Zalpha);
            System.out.println("Reject hypothteis at Z = "+Z);
            System.out.println("The set of random numbers did not pass independence test.\n");
        }
    }
}
