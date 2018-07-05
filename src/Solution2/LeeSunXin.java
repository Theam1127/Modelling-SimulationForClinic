package Solution2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import OutputDataAnalysis.PointOfEstimation;
import IndependenceAndUniformityTest.ChiSquareTest;
import IndependenceAndUniformityTest.TestForAutocorrelation;
import RandomValues.LinearConguentialGenerator;
import RandomValues.RandomValuesSet;
import static java.lang.Math.pow;


/**
 *
 * @author sx
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
    static int doctorQueue, doctorStatus, medicineQueue, nurseStatus1, nurseStatus2, clockTime, doctorExist;
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

public class LeeSunXin {
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
    static Activities interarrivalDoctor;
    static Activities arrivalNurse;
    static Activities doctorService;
    static Activities nurseService;
    
    public static void main(String[] args) {
        for(countRunTime = 0;countRunTime<5;countRunTime++){
            System.out.println("Simulation Start");
            Queue doctorQueue = new Queue();
            Queue medicineQueue = new Queue();
            FutureEventList list = new FutureEventList();
            initialise(list, doctorQueue, medicineQueue);
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
    
    public static void initialise(FutureEventList list, Queue doctorQueue, Queue medicineQueue){
        Event stoppingTime = new Event("StopSimulation", stoppingSimulation, 0);
        list.add(stoppingTime);
        interarrivalDoctor = new Activities();
        arrivalNurse = new Activities();
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
        State.doctorStatus=0;//1 = busy
        State.medicineQueue=0;
        State.nurseStatus1=0; //1 = busy, Status for nurse no.1
        State.nurseStatus2=0; //          Status for nurse no.2
        State.doctorExist = 1; //0 = doctor not here
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
        interarrivalDoctor.add(interarrivalTime);
        System.out.println("DQ(t) = Doctor Queue\n"
                + "MQ(t) = Medicine Queue\n"
                + "D(t) = Doctor Status\n"
                + "N1(t) = Nurse 1 Status\n"
                + "N2(t) = Nurse 2 Status");
        arrivalTime = State.clockTime + interarrivalTime;
        Event event = new Event("ArrivalDoctorQueue", arrivalTime , patient);
        list.add(event);
        start(list,doctorQueue,medicineQueue);
        
    }
    
    public static void start(FutureEventList list, Queue doctorQueue, Queue medicineQueue){
        int interarrivalTime;
        System.out.println("\n ------------------------------------------------- \n Clock " + State.clockTime);
        if(State.doctorExist == 2)
            System.out.println("Doctor is currently not in the clinic");
        else if(State.doctorExist==1)
            System.out.println("Doctor is here");
        
        System.out.println("System states");
        System.out.println("DQ(t) " + State.doctorQueue + " MQ(t) " + State.medicineQueue + " D(t) " + State.doctorStatus + " N1(t) " + State.nurseStatus1+" N2(t) "+State.nurseStatus2);
        System.out.print(" Doctor Queue: ");
        doctorQueue.display();
        System.out.print("\n Medicine Queue: ");
        medicineQueue.display();
        System.out.print("\n Future Event List \n");
        list.display();
        Event nextClockTimeEvent = list.remove();
        State.clockTime = nextClockTimeEvent.clockTime;
        Event sameClockTimeEvent;
        if(list.findSameClockTime()==true){
            sameClockTimeEvent = list.remove();
            if(sameClockTimeEvent.type.equals("ArrivalDoctorQueue")){
                totalNumberOfPatientDoctor++;
                arrivalDoctor(list,sameClockTimeEvent,doctorQueue,medicineQueue);
            }
            if(sameClockTimeEvent.type.equals("DepartureDoctor")){
                totalNumberOfPatientNurse++;
                departureDoctor(list,sameClockTimeEvent,doctorQueue,medicineQueue);
            }
            if(sameClockTimeEvent.type.equals("DepartureNurse"))
                departureNurse(list,sameClockTimeEvent,doctorQueue,medicineQueue);//Schedule Departure event for nurse 1
            if(sameClockTimeEvent.type.equals("DepartureNurse2"))
                departureNurse2(list,sameClockTimeEvent,doctorQueue,medicineQueue);//Schedule Departure event for nurse 2
            if(sameClockTimeEvent.type.equals("DoctorComeBack"))
                doctorComeBack(list,sameClockTimeEvent,doctorQueue,medicineQueue);
            
            if(sameClockTimeEvent.clockTime == stoppingSimulation)
                return;
        }
        if(nextClockTimeEvent.type.equals("ArrivalDoctorQueue")){
            totalNumberOfPatientDoctor++;
            arrivalDoctor(list,nextClockTimeEvent,doctorQueue,medicineQueue);
        }
        if(nextClockTimeEvent.type.equals("DepartureDoctor")){
            totalNumberOfPatientNurse++;
            departureDoctor(list,nextClockTimeEvent,doctorQueue,medicineQueue);
        }
        if(nextClockTimeEvent.type.equals("DepartureNurse"))
            departureNurse(list,nextClockTimeEvent,doctorQueue,medicineQueue);//Schedule Departure event for nurse 1
        if(nextClockTimeEvent.type.equals("DepartureNurse2"))
            departureNurse2(list,nextClockTimeEvent,doctorQueue,medicineQueue);//Schedule Departure event for nurse 2
        
        if(nextClockTimeEvent.type.equals("DoctorComeBack")){
            doctorComeBack(list, nextClockTimeEvent, doctorQueue, medicineQueue);
        }
        if(nextClockTimeEvent.type.equals("StopSimulation"))
            return;
        
        
        
        
        interarrivalTime = RandomValuesSet.getRandom(RandomValuesSet.interarrivalSet);
        interarrivalDoctor.add(interarrivalTime);
        arrivalTime += interarrivalTime;
        Event event = new Event("ArrivalDoctorQueue", arrivalTime, ++patient);
        list.add(event);
        start(list,doctorQueue,medicineQueue);
    }

    private static void arrivalDoctor(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue) {
        int departureTime, doctorServiceTime, emergency = RandomValuesSet.getRandom(RandomValuesSet.emergencySet);
        if(emergency==2)
                totalNumberOfEmergencyPatient++;
        if(State.doctorStatus == 0 && State.doctorExist==1){
            State.doctorStatus = 1;
            doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);
            doctorService.add(doctorServiceTime);
            departureTime = State.clockTime + doctorServiceTime;
            totalSpentDoctor += departureTime - State.clockTime;
            list.add(new Event("DepartureDoctor", departureTime, nextClockTimeEvent.patient));
        }
        else if(State.doctorStatus==1 || State.doctorExist == 2){
            State.doctorQueue++;
            delayedPatientDoctor++;
            doctorQueue.add(nextClockTimeEvent.patient, emergency);
        }
    }
    
    private static void departureDoctor(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue){
        int nurseServiceTime, departureTimeNurse;
        if(State.nurseStatus1 == 0){//set nurse 1 to busy if currently is free
            State.nurseStatus1 = 1;
            nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
            nurseService.add(nurseServiceTime);
            arrivalNurse.add(State.clockTime);
            departureTimeNurse = State.clockTime + nurseServiceTime;
            totalSpentNurse += departureTimeNurse - State.clockTime;
            list.add(new Event("DepartureNurse", departureTimeNurse, nextClockTimeEvent.patient));
        }
        else if(State.nurseStatus2==0){//set nurse 2 to busy if currently is free
            State.nurseStatus2 = 1;
            nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
            nurseService.add(nurseServiceTime);
            arrivalNurse.add(State.clockTime);
            departureTimeNurse = State.clockTime + nurseServiceTime;
            totalSpentNurse += departureTimeNurse - State.clockTime;
            list.add(new Event("DepartureNurse2", departureTimeNurse, nextClockTimeEvent.patient));
        }
        else if(State.nurseStatus1 == 1&&State.nurseStatus2==1){//add 1 to medicine queue if both nurse are busy
            State.medicineQueue++;
            delayedPatientNurse++;
            medicineQueue.add(nextClockTimeEvent.patient, 0);
        }
        State.doctorStatus = 0;
        State.doctorExist = RandomValuesSet.getRandom(RandomValuesSet.doctorExist);
        int departureTime, doctorServiceTime;
        if(State.doctorExist==1){
            if (State.doctorQueue != 0) {
            State.doctorStatus = 1;
            State.doctorQueue--;
            doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);
            doctorService.add(doctorServiceTime);
            int arrivalTime = doctorQueue.removeArrivalTime();
            int emergency = doctorQueue.removeEmergencyStatus();
            if(emergency == 2){
                totalWaitingTimeEmergency += (State.clockTime-arrivalTime);
            }
            totalDelayTimeDoctor += (State.clockTime-arrivalTime);
            departureTime = State.clockTime + doctorServiceTime;
            totalSpentDoctor += departureTime - arrivalTime;
            list.add(new Event("DepartureDoctor", departureTime, doctorQueue.remove()));
            }
        }
        else{
            int doctorLeaveTime = RandomValuesSet.getRandom(RandomValuesSet.doctorLeaveTime);
            list.add(new Event("DoctorComeBack", State.clockTime+doctorLeaveTime, 0));
        }
    }
    
    private static void departureNurse(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue){
        int departureTime, nurseServiceTime;
        State.nurseStatus1 = 0;//set nurse 1 status to free
        if(State.medicineQueue!=0){
            State.nurseStatus1 = 1;
            State.medicineQueue--;
            nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
            nurseService.add(nurseServiceTime);
            int arrivalTime = medicineQueue.removeArrivalTime();
            arrivalNurse.add(arrivalTime);
            totalDelayTimeNurse += (State.clockTime-arrivalTime);
            departureTime = State.clockTime + nurseServiceTime;
            totalSpentNurse += departureTime - arrivalTime;
            list.add(new Event("DepartureNurse", departureTime, medicineQueue.remove()));
        }
    }
    private static void departureNurse2(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue){
        int departureTime, nurseServiceTime;
        State.nurseStatus2 = 0;//set nurse 2 status to free
        if(State.medicineQueue!=0){
            State.nurseStatus2 = 1;
            State.medicineQueue--;
            nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
            nurseService.add(nurseServiceTime);
            int arrivalTime = medicineQueue.removeArrivalTime();
            arrivalNurse.add(arrivalTime);
            totalDelayTimeNurse += (State.clockTime-arrivalTime);
            departureTime = State.clockTime + nurseServiceTime;
            totalSpentNurse += departureTime - arrivalTime;
            list.add(new Event("DepartureNurse2", departureTime, medicineQueue.remove()));
        }
    }

    private static void doctorComeBack(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue) {
        if (State.doctorQueue != 0) {
                    State.doctorStatus = 1;
                    State.doctorQueue--;
                    int doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);
                    doctorService.add(doctorServiceTime);
                    int departureTime = State.clockTime + doctorServiceTime;
                    list.add(new Event("DepartureDoctor", departureTime, doctorQueue.remove()));    
        }
          State.doctorExist=1;
    }
}