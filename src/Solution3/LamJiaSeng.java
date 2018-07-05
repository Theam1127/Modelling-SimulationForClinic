/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solution3;

import OutputDataAnalysis.PointOfEstimation;
import IndependenceAndUniformityTest.ChiSquareTest;
import IndependenceAndUniformityTest.TestForAutocorrelation;
import RandomValues.RandomValuesSet;
import RandomValues.LinearConguentialGenerator;
import static java.lang.Math.pow;


/**
 *
 * @author Jia Seng
 */

class Event {

    String type;
    int clockTime;
    int patient;

    public Event(String type, int time, int patient) {
        this.type = type;
        this.clockTime = time;
        this.patient = patient;
    }
    

    public void display() {
        System.out.println("(" + type + ", " + clockTime + ", patient" + patient + ")"); //event notices
    }
}

class FutureEventList {

    Event e[] = new Event[1000];
    int size;

    
    
    public FutureEventList() {
        size = 0;
    }

    public void add(Event a) {
        if (size < 1000) {
            e[size] = a;
            size++;
        } else {
            System.out.println("Future Event List Full");
        }
    }
    
    

    public Event remove() {
        int minimumClockTime = 0;
        int positionInArray = 0;
        for (int i = 0; i < size; i++) {
            if (e[i] != null) {
                minimumClockTime = e[i].clockTime;
                break;
            }
        }
        for (int i = 0; i < size; i++) {
            if (e[i] != null && e[i].clockTime <= minimumClockTime) {
                minimumClockTime = e[i].clockTime;
                positionInArray = i;
            }
        }
        Event nextEvent = new Event(e[positionInArray].type, e[positionInArray].clockTime, e[positionInArray].patient);
        e[positionInArray] = null; // remove it from array
        return nextEvent;
        
    }
    
    public void leaveClinic(){
        for(int a=0;a<e.length;a++)
            if(e[a].type.equals("DepartureNurse"))
                e[a] = null;
    }

    public void display() {
        for (int i = 0; i < 1000; i++) {
            if (e[i] != null) {
                e[i].display(); //display current events list
            }
        }
    }
    
    public boolean findSameClockTime(){
        for(int i=0;i<1000;i++){
            if(e[i]!=null && e[i].clockTime == State.clockTime){
                return true;
            }
        }
        return false;
    }
}

class LCGValues{
    static int[] a = {7, 25, 55, 75, 105};
    static int[] c = {1, 11, 21, 31, 41};
    static int m = (int)pow(2,16);
    static int[] z = {1, 51, 71, 151, 201};
}


class Queue {

    int q[]; //queue array
    int size;
    int arrivalTime[]; //arrival time of delayed patient
    int emergency[];
    
    public Queue() {
        q = new int[1000];
        arrivalTime = new int[1000];
        size = 0;
        emergency = new int[1000];
    }

    public void add(int patient, int emergency) {//patient no.
        q[size] = patient;
        arrivalTime[size] = State.clockTime;
        this.emergency[size] = emergency;
        size++;
    }
    
    public int removeArrivalTime(){
        for (int i = 0; i < 1000; i++) {
            if (arrivalTime[i] != 0) { //remove 1st patient in queue
                int temp = arrivalTime[i];
                arrivalTime[i] = 0;
                return temp;
            }
        }
        return 0; 
    }
    
    public int removeEmergencyStatus(){
        for(int i=0;i<1000;i++){
            if(emergency[i]!=0){
                int temp = emergency[i];
                emergency[i] = 0;
                return temp;
            }
        }
        return 0;
    }

    public int remove() {
        for (int i = 0; i < 1000; i++) {
            if (q[i] != 0) { //remove 1st patient in queue
                int temp = q[i];
                q[i] = 0;
                return temp;
            }
        }
        return 0; 
    }

    public void display() {
        for (int i = 0; i < 1000; i++) {
            if (q[i] != 0) {
                System.out.print(" Patient " + q[i]); //display all patient in queue
            }
        }
    }
}

class State {
    static int doctorQueue, doctorStatus, medicineQueue, nurseStatus, clockTime, doctorExist,priorityQueue;
}

class Activities{
    int[] activities;
    int size;
    public void add(int num){
        activities[size] = num;
        size++;
    }
    Activities(){
        activities = new int[1000];
        size = 0;
    }
    
}

public class LamJiaSeng {
    /**
     * @param args the command line arguments
     */
    static int patient = 1, arrivalTime = 0, totalNumberOfPatientDoctor = 0, totalNumberOfPatientNurse = 0, 
            delayedPatientDoctor = 0, delayedPatientNurse = 0,
            totalDelayTimeDoctor = 0, totalDelayTimeNurse = 0,
            totalServiceTimeDoctor = 0, totalServiceTimeNurse = 0,
            totalSpentDoctor = 0, totalSpentNurse = 0, countRunTime,
            totalWaitingTimeEmergency = 0, totalNumberOfEmergencyPatient = 0;
    final static int stoppingSimulation = 360;
    static double[] averageWaitingTimeArrayDoctor = new double[5], averageWaitingTimeArrayNurse = new double[5], averageWaitingTimeEmergencyArray = new double[5], averageWaitingTimeNormalArray = new double[5];
    static double[] random;
    static Activities doctorService;
    static Activities nurseService;
    
    public static void main(String[] args) {
        for(countRunTime = 0;countRunTime<5;countRunTime++){
            System.out.println("Simulation Start");
            Queue doctorQueue = new Queue();
            Queue medicineQueue = new Queue();
            Queue priorityQueue = new Queue();
            FutureEventList list = new FutureEventList();
            initialise(list, doctorQueue, medicineQueue,priorityQueue);
            System.out.println("End of Simulation");
            System.out.println("");
            report();
    }
        for(int a=0;a<5;a++){
            System.out.println("-----------------Run "+(a+1)+"------------------------------");
            System.out.printf("Average waiting time of doctor queue: %.2f\n", averageWaitingTimeArrayDoctor[a]);
            System.out.printf("Average waiting time of nurse queue: %.2f\n", averageWaitingTimeArrayNurse[a]);
            System.out.printf("Average waiting time of emergency patient in doctor queue: %.2f\n",averageWaitingTimeEmergencyArray[a]);
            System.out.printf("Average waiting time of normal patient in doctor queue %.2f\n",averageWaitingTimeNormalArray[a]);
        }
        System.out.println("------------------------Doctor Average Waiting Time------------------------------");
        PointOfEstimation.PointOfEstimation(averageWaitingTimeArrayDoctor);
        System.out.println("------------------------Emergency Patient Waiting Time-------------------------------");
        PointOfEstimation.PointOfEstimation(averageWaitingTimeEmergencyArray);
        System.out.println("------------------------Normal Patient Waiting Time----------------------------------");
        PointOfEstimation.PointOfEstimation(averageWaitingTimeNormalArray);
        System.out.println("-----------------------Nurse Average Waiting Time---------------------------------");
        PointOfEstimation.PointOfEstimation(averageWaitingTimeArrayNurse);
    }
    public static void report(){
        double avgEmergencyWaitingTime = 0;
        System.out.println("Total delayed patient in doctor queue : "+delayedPatientDoctor);
        System.out.println("Total deleyed patient in nurse queue : "+delayedPatientNurse);
        System.out.println("Total deleyed time of patient in doctor queue : "+totalDelayTimeDoctor);
        System.out.println("Total delayed time of patient in nurse queue : "+totalDelayTimeNurse);
        System.out.println("Total delayed time of emergency patient in doctor queue : " + totalWaitingTimeEmergency);
        System.out.println("Total delayed emergency patient in doctor queue : "+totalNumberOfEmergencyPatient);
        System.out.println("Total delayed normal patient in doctor queue : "+(totalNumberOfPatientDoctor - totalNumberOfEmergencyPatient));
        System.out.println("Total delay time of normal patient in doctor queue : "+(totalDelayTimeDoctor - totalWaitingTimeEmergency));
        System.out.println("Time patient spent in doctor queue : "+totalSpentDoctor);
        System.out.println("Time patient spent in nurse queue : "+totalSpentNurse);
        for(int a=0;a<doctorService.activities.length;a++)
            totalServiceTimeDoctor += doctorService.activities[a];
        for(int a=0;a<nurseService.activities.length;a++)
            totalServiceTimeNurse += nurseService.activities[a];
        if(totalNumberOfEmergencyPatient!=0)
            avgEmergencyWaitingTime = (double)totalWaitingTimeEmergency/totalNumberOfEmergencyPatient;
        averageWaitingTimeNormalArray[countRunTime] = ((totalDelayTimeDoctor - totalWaitingTimeEmergency)/ (double)(totalNumberOfPatientDoctor - totalNumberOfEmergencyPatient) );
        System.out.println("\n-----------------------------------Doctor------------------------------------");
        System.out.printf(" Average waiting time for a patient in Doctor queue : %.2f minutes\n", ((double)totalDelayTimeDoctor / totalNumberOfPatientDoctor));
        System.out.printf(" Probability that a patient has to wait in Doctor queue : %.2f\n", ((double)delayedPatientDoctor / totalNumberOfPatientDoctor));
        System.out.printf(" Average waiting time for patient who waits in Doctor queue : %.2f minutes\n", ((double)totalDelayTimeDoctor / delayedPatientDoctor));
        System.out.printf(" Average waiting time for emergency patient : %.2f minutes\n",avgEmergencyWaitingTime);
        System.out.printf(" Average waiting time for normal patient : %.2f minutes\n", averageWaitingTimeNormalArray[countRunTime]);
        System.out.printf(" Average time customer spends in the system : %.2f minutes\n", ((double)totalSpentDoctor / totalNumberOfPatientDoctor));
        averageWaitingTimeArrayDoctor[countRunTime] = ((double)totalDelayTimeDoctor / totalNumberOfPatientDoctor);
        averageWaitingTimeEmergencyArray[countRunTime] = avgEmergencyWaitingTime;
        System.out.println("\n----------------------------------Nurse----------------------------------------");
        System.out.printf(" Average waiting time for a patient in Nurse queue : %.2f minutes\n", ((double)totalDelayTimeNurse / totalNumberOfPatientNurse));
        System.out.printf(" Probability that a patient has to wait in Nurse queue : %.2f\n", ((double)delayedPatientNurse / totalNumberOfPatientNurse));
        System.out.printf(" Average waiting time for patient who waits in Nurse queue : %.2f minutes\n", ((double)totalDelayTimeNurse / delayedPatientNurse));
        System.out.printf(" Average time customer spends in the system : %.2f minutes\n", ((double)totalSpentNurse / totalNumberOfPatientNurse));
        averageWaitingTimeArrayNurse[countRunTime] = ((double)totalDelayTimeNurse / totalNumberOfPatientNurse);
    }
    
    public static void initialise(FutureEventList list, Queue doctorQueue, Queue medicineQueue,Queue priorityQueue){
        Event stoppingTime = new Event("StopSimulation", stoppingSimulation, 0);
        list.add(stoppingTime);
        doctorService = new Activities();
        nurseService = new Activities();
        patient = 1; //next customer number is 1
        totalNumberOfPatientDoctor = 0;
        totalNumberOfPatientNurse = 0;
        delayedPatientDoctor = 0;
        delayedPatientNurse = 0;
        totalDelayTimeDoctor = 0; 
        totalDelayTimeNurse = 0;
        totalServiceTimeDoctor = 0; 
        totalServiceTimeNurse = 0;
        totalSpentDoctor = 0;
        totalSpentNurse = 0;
        totalWaitingTimeEmergency = 0;
        totalNumberOfEmergencyPatient = 0;
        State.clockTime=0;
        State.doctorQueue=0;
        State.priorityQueue=0;
        State.doctorStatus=0;//1 = busy
        State.medicineQueue=0;
        State.nurseStatus=0; //1 = busy
        State.doctorExist = 1; //2 = doctor not here
        LinearConguentialGenerator generator = new LinearConguentialGenerator();
        generator.initialise(LCGValues.a[countRunTime], LCGValues.c[countRunTime], LCGValues.m, LCGValues.z[countRunTime]);
        random = LinearConguentialGenerator.generate();
        System.out.println("---------------------------------------Chi-Square Test-------------------------------------------");
        ChiSquareTest.chisquare(random);
        System.out.print("-----------------------------------------Test for Autocorrelation-----------------------------------");
        TestForAutocorrelation.testForAutocorrelation(random);
        System.out.println("------------------------------------------Start of Simulation--------------------------------------");
        RandomValuesSet.initialiseRandom(random);
        int interarrivalTime = RandomValuesSet.getRandom(RandomValuesSet.interarrivalSet);
        System.out.println("DQ(t) = Doctor Queue\n"
                + "MQ(t) = Medicine Queue\n"
                + "D(t) = Doctor Status\n"
                + "N(t) = Nurse Status\n"
                + "PQ(t) = Priority Queue"); // definition
        arrivalTime = State.clockTime + interarrivalTime;
        Event event = new Event("ArrivalDoctorQueue", arrivalTime , patient);
        list.add(event);
        start(list,doctorQueue,medicineQueue,priorityQueue);
        
    }
    
    public static void start(FutureEventList list, Queue doctorQueue, Queue medicineQueue,Queue priorityQueue){
        int interarrivalTime;
        System.out.println("\n ------------------------------------------------- \n Clock " + State.clockTime);
        if(State.doctorExist == 2)
            System.out.println("Doctor is currently not in the clinic");
        else if(State.doctorExist==1)
            System.out.println("Doctor is here");
        
        System.out.println("System states");
        System.out.println("DQ(t) " + State.doctorQueue + " MQ(t) " + State.medicineQueue + " D(t) " + State.doctorStatus + " N(t) " + State.nurseStatus +" PQ(t) "+State.priorityQueue);
        System.out.print(" Doctor Queue: ");
        doctorQueue.display();
        System.out.print("\n Medicine Queue: ");
        medicineQueue.display();
        System.out.print("\n Priority Queue: ");
        priorityQueue.display();
        System.out.print("\n Future Event List \n");
        list.display();
        Event nextClockTimeEvent = list.remove();
        State.clockTime = nextClockTimeEvent.clockTime;
        Event sameClockTimeEvent;
        if(list.findSameClockTime()==true){
            sameClockTimeEvent = list.remove();
            if(sameClockTimeEvent.type.equals("ArrivalDoctorQueue")){
                totalNumberOfPatientDoctor++;
                arrivalDoctor(list,sameClockTimeEvent,doctorQueue,medicineQueue,priorityQueue);
            }
            if(sameClockTimeEvent.type.equals("DepartureDoctor")){
                totalNumberOfPatientNurse++;
                departureDoctor(list,sameClockTimeEvent,doctorQueue,medicineQueue,priorityQueue);
            }
            if(sameClockTimeEvent.type.equals("DepartureNurse"))
                departureNurse(list,sameClockTimeEvent,doctorQueue,medicineQueue);
            if(sameClockTimeEvent.type.equals("DoctorComeBack"))
                doctorComeBack(list,sameClockTimeEvent,doctorQueue,medicineQueue,priorityQueue);
            
            if(sameClockTimeEvent.clockTime == stoppingSimulation)
                return;
        }
        if(nextClockTimeEvent.type.equals("ArrivalDoctorQueue")){
            totalNumberOfPatientDoctor++;
            arrivalDoctor(list,nextClockTimeEvent,doctorQueue,medicineQueue,priorityQueue);
        }
        if(nextClockTimeEvent.type.equals("DepartureDoctor")){
            totalNumberOfPatientNurse++;
            departureDoctor(list,nextClockTimeEvent,doctorQueue,medicineQueue,priorityQueue);
        }
        if(nextClockTimeEvent.type.equals("DepartureNurse"))
            departureNurse(list,nextClockTimeEvent,doctorQueue,medicineQueue);
        
        if(nextClockTimeEvent.type.equals("DoctorComeBack")){
            doctorComeBack(list, nextClockTimeEvent, doctorQueue, medicineQueue,priorityQueue);
        }
        if(nextClockTimeEvent.type.equals("StopSimulation"))
            return;
        
        
        
        
        interarrivalTime = RandomValuesSet.getRandom(RandomValuesSet.interarrivalSet);
        arrivalTime += interarrivalTime;
        Event event = new Event("ArrivalDoctorQueue", arrivalTime, ++patient);
        list.add(event); //adding to future list if >1000 stop simulation
        start(list,doctorQueue,medicineQueue,priorityQueue);
    }

    private static void arrivalDoctor(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue,Queue priorityQueue) {
        int departureTime, doctorServiceTime, emergency = RandomValuesSet.getRandom(RandomValuesSet.emergencySet);
        
        if(emergency==2)
                totalNumberOfEmergencyPatient++; // customer come in with pariority
        if(State.doctorStatus == 0 && State.doctorExist==1){ //doc free and exist in clinic
            State.doctorStatus = 1; //make doc busy
            doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor); //assign doc service time to customer
            doctorService.add(doctorServiceTime); // add to total service time
            departureTime = State.clockTime + doctorServiceTime; // calculate time end
            totalSpentDoctor += departureTime - State.clockTime;
            list.add(new Event("DepartureDoctor", departureTime, nextClockTimeEvent.patient));
        }
        else if((State.doctorStatus==1 || State.doctorExist == 2)&& emergency==2){
            State.priorityQueue++;
            delayedPatientDoctor++;
            priorityQueue.add(nextClockTimeEvent.patient, emergency);
        }
        else if(State.doctorStatus==1 || State.doctorExist == 2 ){  //doc nt here or doc busy
            State.doctorQueue++; //add customer to the normal queue
            delayedPatientDoctor++;
            doctorQueue.add(nextClockTimeEvent.patient, emergency);
        }
    }
    
    private static void departureDoctor(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue,Queue priorityQueue){
        int nurseServiceTime, departureTimeNurse;
        if(State.nurseStatus == 0){
            State.nurseStatus = 1;
            nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
            nurseService.add(nurseServiceTime);
            departureTimeNurse = State.clockTime + nurseServiceTime;
            totalSpentNurse += departureTimeNurse - State.clockTime;
            list.add(new Event("DepartureNurse", departureTimeNurse, nextClockTimeEvent.patient));
        }
        else if(State.nurseStatus == 1){
            State.medicineQueue++;
            delayedPatientNurse++;
            medicineQueue.add(nextClockTimeEvent.patient, 0);
        }
        State.doctorStatus = 0; // make doc back to idle
        State.doctorExist = RandomValuesSet.getRandom(RandomValuesSet.doctorExist); //check doc want to leave or nt
        int departureTime, doctorServiceTime;
        if(State.doctorExist==1){ //if doc is here
            if (State.doctorQueue != 0 || State.priorityQueue !=0) { //if normal queue  or priority queue got customer
            State.doctorStatus = 1; //make doc busy
            if(State.priorityQueue!=0){
                State.priorityQueue--; // minus customer from priority queue
                int arrivalTime = priorityQueue.removeArrivalTime();
                totalNumberOfEmergencyPatient++;
                totalWaitingTimeEmergency += (State.clockTime-arrivalTime);
                doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor); //particular doc service time for customer
                doctorService.add(doctorServiceTime); // adding to total doc service time
                //int emergency = doctorQueue.removeEmergencyStatus(); // get the emergency status
                totalDelayTimeDoctor += (State.clockTime-arrivalTime);
                departureTime = State.clockTime + doctorServiceTime;
                totalSpentDoctor += departureTime - arrivalTime;
                list.add(new Event("DepartureDoctor", departureTime, priorityQueue.remove()));
            }
            else{
                State.doctorQueue--; //minus customer from normal queue
                int arrivalTime = doctorQueue.removeArrivalTime(); // get the arrival time 
                doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);//particular doc service time for customer
                doctorService.add(doctorServiceTime); // adding to total doc service time
                //int emergency = doctorQueue.removeEmergencyStatus(); // get the emergency status
                totalDelayTimeDoctor += (State.clockTime-arrivalTime);
                departureTime = State.clockTime + doctorServiceTime;
                totalSpentDoctor += departureTime - arrivalTime;
                list.add(new Event("DepartureDoctor", departureTime, doctorQueue.remove()));
            }
            
            }
        }
        else{
            int doctorLeaveTime = RandomValuesSet.getRandom(RandomValuesSet.doctorLeaveTime);
            list.add(new Event("DoctorComeBack", State.clockTime+doctorLeaveTime, 0));
        }
    }
    
    private static void departureNurse(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue){
        int departureTime, nurseServiceTime;
        State.nurseStatus = 0;
        if(State.medicineQueue!=0){
            State.nurseStatus = 1;
            State.medicineQueue--;
            nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
            nurseService.add(nurseServiceTime);
            int arrivalTime = medicineQueue.removeArrivalTime();
            totalDelayTimeNurse += (State.clockTime-arrivalTime);
            departureTime = State.clockTime + nurseServiceTime;
            totalSpentNurse += departureTime - arrivalTime;
            list.add(new Event("DepartureNurse", departureTime, medicineQueue.remove()));
        }
    }

    private static void doctorComeBack(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue,Queue priorityQueue) {
        State.doctorExist=1;
        if(State.priorityQueue != 0){
                    State.doctorStatus = 1;
                    State.priorityQueue--;
                    int doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);
                    doctorService.add(doctorServiceTime);
                    int departureTime = State.clockTime + doctorServiceTime;
                    list.add(new Event("DepartureDoctor", departureTime, priorityQueue.remove()));
                    
        }
        else if (State.doctorQueue != 0) {
                    State.doctorStatus = 1;
                    State.doctorQueue--;
                    int doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);
                    doctorService.add(doctorServiceTime);
                    int departureTime = State.clockTime + doctorServiceTime;
                    list.add(new Event("DepartureDoctor", departureTime, doctorQueue.remove()));
        }
    }
}

