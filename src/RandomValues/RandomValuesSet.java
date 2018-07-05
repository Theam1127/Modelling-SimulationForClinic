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
public class RandomValuesSet {
    public static int[] interarrivalSet;
    public static int[] serviceTimeDoctor;
    public static int[] serviceTimeNurse;
    public static int[] doctorExist;
    public static int[] doctorLeaveTime;
    public static int[] emergencySet;
    
    public static void initialiseRandom(double[] rand){
        interarrivalSet = new int[1000];
        serviceTimeDoctor = new int[1000];
        serviceTimeNurse = new int[1000];
        doctorExist = new int[1000];
        doctorLeaveTime = new int[1000];
        emergencySet = new int[1000];
        for(int a=0;a<rand.length;a++){
            interarrivalSet[a] = MonteCarlo.monteCarloInterarrival(rand[a]);
            serviceTimeDoctor[a] = MonteCarlo.monteCarloServiceTimeDoctor(rand[a]);
            serviceTimeNurse[a] = MonteCarlo.monteCarloServiceTimeNurse(rand[a]);
            doctorExist[a] = MonteCarlo.monteCarloDoctorExist(rand[a]);
            doctorLeaveTime[a] = MonteCarlo.monteCarloDoctorLeaveTime(rand[a]);
            emergencySet[a] = MonteCarlo.monteCarloEmergency(rand[a]);
        }
    }
    
    public static int getRandom(int[] randSet){
        int temp = 0;
        for(int a=0;a<randSet.length;a++){
            if(randSet[a]!=0){
                temp = randSet[a];
                randSet[a] = 0;
                break;
            }
        }
        return temp;
    }
}

class MonteCarlo{
    public static int monteCarloInterarrival(double r){
    if(0<=r && r<0.13559322)
        return 3;
    else if(0.13559322<=r && r<0.271186441)
        return 4;
    else if(0.271186441<=r && r<0.423728814)
        return 5;
    else if(0.423728814<=r && r<0.593220339)
        return 6;
    else if(0.593220339<=r && r<0.677966102)
        return 7;
    else if(0.677966102<=r && r<0.86440678)
        return 8;
    else if(0.86440678<=r && r<0.93220339)
        return 9;
    return 10;
    }
    
    public static int monteCarloServiceTimeDoctor(double r){
        if(0<=r && r<0.08181081081)
            return 5;
        else if(0.08181081081<=r && r< 0.297297297)
            return 7;
        else if(0.297297297<=r && r<0.378378378)
            return  8;
        else if(0.378378378<=r && r<0.432432432)
            return 9;
        else if(0.432432432<=r && r<0.621621622)
            return 10;
        else if(0.621621622<=r && r<0.702702703)
            return 11;
        else if(0.702702703<=r && r<0.783783784)
            return 12;
        else if(0.783783784<=r && r<0.918918919)
            return 13;
        else if(0.918918919<=r && r<0.972972973)
            return 14;
        return 15;
    }
    
    public static int monteCarloServiceTimeNurse(double r){
        if(0<=r && r<0.027027027)
            return 3;
        else if(0.027027027<=r && r<0.297297297)
            return 4;
        else if(0.297297297<=r && r<0.513513514)
            return 5;
        else if(0.513513514<=r && r<0.756756757)
            return 6;
        else if(0.756756757<=r && r<0.864864865)
            return 7;
        return 8;
    }
    
    public static int monteCarloDoctorExist(double r){
        if(0<=r && r<0.081081081)
            return 2;
        return 1;
    }
    
    public static int monteCarloDoctorLeaveTime(double r){
        if(0<=r && r<0.5)
            return 25;
        return 19;
    }
    
    public static int monteCarloEmergency(double r){
        if(0<=r && r<0.10)
            return 2;
        return 1;
    }
}
