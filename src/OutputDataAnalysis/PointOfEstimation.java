/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputDataAnalysis;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author Yeap Theam
 */
public class PointOfEstimation {
    static double SsquareN, Xn, pointOfEstimationPositive, pointOfEstimationNegative, quantile1, quantile2, gamma;
    static int n, t, a;
    static double[][] criticalPoints = new double[][]{{3.078, 6.314, 12.706},
                                                    {1.886, 2.920, 4.303},
                                                    {1.638, 2.353, 3.182},
                                                    {1.533,2.132, 2.776},
                                                    {1.476, 2.015, 2.571},
                                                    {1.440, 1.943, 2.447},
                                                    {1.415, 1.895, 2.365},
                                                    {1.397, 1.860, 2.306},
                                                    {1.383, 1.833, 2.262},
                                                    {1.372, 1.812, 2.228}};
    public static void PointOfEstimation(double[] averageWaitingTime){
        initialize(averageWaitingTime.length);
        for(int a=0;a<averageWaitingTime.length;a++)
            Xn+=averageWaitingTime[a];
        Xn /= n;
        for(int a=0;a<averageWaitingTime.length;a++)
            SsquareN+=(pow((averageWaitingTime[a]-Xn),2)/(n-1));
        quantile2 = sqrt(SsquareN/n);
        gamma = (double)(100 - a)/100;
        if(gamma == 0.9)
            quantile1 = criticalPoints[t-1][0];
        else if(gamma == 0.95)
            quantile1 = criticalPoints[t-1][1];
        else if(gamma == 0.975)
            quantile1 = criticalPoints[t-1][2];
        pointOfEstimationPositive = Xn+(quantile1*quantile2);
        pointOfEstimationNegative = Xn-(quantile1*quantile2);
        System.out.printf("Point of estimation: %.4f+(%.4f)(%.4f) = %.4f\n", Xn, quantile1,quantile2,pointOfEstimationPositive);
        System.out.printf("Point of estimation: %.4f-(%.4f)(%.4f) = %.4f\n", Xn, quantile1,quantile2,pointOfEstimationNegative);
        
    }
    
    public static void initialize(int length){
        n = length;
        SsquareN = 0;
        Xn = 0;
        a = 5;
        t = n-1;
    }
}
