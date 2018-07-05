/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RandomValues;

/**
 *
 * @author Yeap Theam
 */
public class LinearConguentialGenerator {
    static int maxNumGenerate = 1000;
    static int a, c, m, z;
    static double[] randomNumbers;
    
    public LinearConguentialGenerator(){
        randomNumbers = new double[1000];
    };
    
    public static double[] generate() {
        /*(a)*/for(int i=0; i<maxNumGenerate; i++){
            int num = linearCongruentialGenerator(a,c,m,z);
            randomNumbers[i] = (double)num/m;
            z = num;
        }
        
        return randomNumbers;
    }
    
    public static int linearCongruentialGenerator(int a,int c,int m,int z){
        return ((a*z)+c) % m;
    }
    
    public void initialise(int a, int c, int m,int z){
        this.a=a;
        this.c=c;
        this.m=m;
        this.z=z;
    }
}