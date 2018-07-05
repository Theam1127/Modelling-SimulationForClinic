/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solution4;

import IndependenceAndUniformityTest.ChiSquareTest;
import OutputDataAnalysis.PointOfEstimation;
import IndependenceAndUniformityTest.TestForAutocorrelation;
import RandomValues.RandomValuesSet;
import RandomValues.LinearConguentialGenerator;
import static java.lang.Math.pow;


/**
 *
 * @author Yeap Theam
 */

class Event {

    String type; //event type eg. arrival, departure
    int clockTime; //clock time of the event occur
    int patient; //patient number, 0 means not related to patient

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

    Event e[] = new Event[1000]; // maximum 1000 events in future event list
    int size;

    
    
    public FutureEventList() {
        size = 0; //initialize
    }

    public void add(Event a) {
        if (size < 1000) { //add new event
            e[size] = a;
            size++;
        } else {
            System.out.println("Future Event List Full");
        }
    }
    
    

    public Event remove() {//remove the event with time closest to current clock time
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
    

    public void display() {
        for (int i = 0; i < 1000; i++) {
            if (e[i] != null) {
                e[i].display(); //display current events list
            }
        }
    }
    
    public boolean findSameClockTime(){ //2 event occur at same clock time
        for(int i=0;i<1000;i++){
            if(e[i]!=null && e[i].clockTime == State.clockTime){
                return true;
            }
        }
        return false;
    }
}

class LCGValues{ //5 sets of LCG values
    static int[] a = {7, 25, 55, 75, 105};
    static int[] c = {1, 11, 21, 31, 41};
    static int m = (int)pow(2,16);
    static int[] z = {1, 51, 71, 151, 201};
}


class Queue {

    int q[]; //queue array
    int size;
    int arrivalTime[]; //arrival time of delayed patient
    int emergency[]; //whether a patient need emergency treatment
    
    public Queue() {
        q = new int[1000];
        arrivalTime = new int[1000];
        size = 0;
        emergency = new int[1000];
    }

    public void add(int patient, int emergency) {//patient no. , emergency status
        q[size] = patient;
        arrivalTime[size] = State.clockTime;
        this.emergency[size] = emergency;
        size++;
    }
    
    public int removeArrivalTime(){
        for (int i = 0; i < 1000; i++) {
            if (arrivalTime[i] != 0) { //remove 1st patient's arrival time in queue
                int temp = arrivalTime[i];
                arrivalTime[i] = 0;
                return temp;
            }
        }
        return 0; 
    }
    
    public int removeEmergencyStatus(){
        for(int i=0;i<1000;i++){ //remove 1st patient's emergency status in queue
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
    static int doctorQueue, doctorStatus, medicineQueue, nurseStatus, clockTime, doctorExist, nurseIsDoctor, nurseIsNurse;
}

class Activities{ //for calculating service times [own reference]
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

public class LeeYeapTheam {
    /**
     * @param args the command line arguments
     */
    static int patient = 1, arrivalTime = 0, totalNumberOfPatientDoctor = 0, totalNumberOfPatientNurse = 0, 
            delayedPatientDoctor = 0, delayedPatientNurse = 0,
            totalDelayTimeDoctor = 0, totalDelayTimeNurse = 0,
            totalServiceTimeDoctor = 0, totalServiceTimeNurse = 0,
            totalSpentDoctor = 0, totalSpentNurse = 0, countRunTime,
            totalWaitingTimeEmergency = 0, totalNumberOfEmergencyPatient = 0;
     final static int stoppingSimulation = 360; //stop simulation at clock time 360
    static double[] averageWaitingTimeArrayDoctor = new double[5], averageWaitingTimeArrayNurse = new double[5], averageWaitingTimeEmergencyArray = new double[5], averageWaitingTimeNormalArray = new double[5]; //for calculate point of estimation
    static double[] random;
    static Activities doctorService;//doctor service time
    static Activities nurseService;//nurse service time
    
    public static void main(String[] args) {
        for(countRunTime = 0;countRunTime<5;countRunTime++){//run 5 times
            System.out.println("Simulation Start");
            Queue doctorQueue = new Queue();//initialize a new doctor queue
            Queue medicineQueue = new Queue();//initialize a new doctor queue
            FutureEventList list = new FutureEventList();//initialize a new future event list
            initialise(list, doctorQueue, medicineQueue);
            System.out.println("End of Simulation");
            System.out.println("");
            report();
    }
        for(int a=0;a<5;a++){//display average waiting times for 5 replication
            System.out.println("-----------------Run "+(a+1)+"------------------------------");
            System.out.printf("Average waiting time of doctor queue: %.2f\n", averageWaitingTimeArrayDoctor[a]);
            System.out.printf("Average waiting time of nurse queue: %.2f\n", averageWaitingTimeArrayNurse[a]);
            System.out.printf("Average waiting time of emergency patient in doctor queue: %.2f\n",averageWaitingTimeEmergencyArray[a]);
            System.out.printf("Average waiting time of normal patient in doctor queue %.2f\n",averageWaitingTimeNormalArray[a]);
        }
        System.out.println("------------------------Doctor Average Waiting Time------------------------------");
        PointOfEstimation.PointOfEstimation(averageWaitingTimeArrayDoctor);//calculate point of estimation for average doctor waiting time for all patients
        System.out.println("------------------------Emergency Patient Waiting Time-------------------------------");
        PointOfEstimation.PointOfEstimation(averageWaitingTimeEmergencyArray);//calculate point of estimation for average doctor waiting time for all emergency patients
        System.out.println("------------------------Normal Patient Waiting Time----------------------------------");
        PointOfEstimation.PointOfEstimation(averageWaitingTimeNormalArray);//calculate point of estimation for average doctor waiting time for all normal patients
        System.out.println("-----------------------Nurse Average Waiting Time---------------------------------");
        PointOfEstimation.PointOfEstimation(averageWaitingTimeArrayNurse);//calculate point of estimation for average nurse waiting time for all patients
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
            totalServiceTimeDoctor += doctorService.activities[a];//sum up all doctor service time
        for(int a=0;a<nurseService.activities.length;a++)
            totalServiceTimeNurse += nurseService.activities[a];//sum up all nurse service time
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
        doctorService = new Activities();
        nurseService = new Activities();
        patient = 1; //next customer number is 1
        totalNumberOfPatientDoctor = 0;//total number of patients in doctor
        totalNumberOfPatientNurse = 0;//total number of patients in nurse
        delayedPatientDoctor = 0;//total number of delayed patients in doctor queue
        delayedPatientNurse = 0;//total number of delayed patients in nurse queue
        totalDelayTimeDoctor = 0; //total waiting time of patients in doctor queue
        totalDelayTimeNurse = 0; //total waiting time of patients in nurse queue
        totalServiceTimeDoctor = 0;  //total service time of doctor
        totalServiceTimeNurse = 0; //total service time of nurse
        totalSpentDoctor = 0;//total time patient spent for doctor
        totalSpentNurse = 0; //total time patient spent to claim medicine
        totalWaitingTimeEmergency = 0;//total waiting time of emergency patients in doctor queue
        totalNumberOfEmergencyPatient = 0; //total number of emergency patients in doctor
        State.clockTime=0;//start clock time at 0
        State.doctorQueue=0;//initialize queue to 0
        State.doctorStatus=0;//1 = busy
        State.medicineQueue=0;//initialize queue to 0
        State.nurseStatus=0; //1 = busy
        State.doctorExist = 1; //2 = doctor not here, 1 = doctor is here
        State.nurseIsDoctor=0; //nurse status when nurse is taking doctor role, 0 is idle, 1 is busy
        State.nurseIsNurse=0; //nurse status when nurse is not taking doctor role, 0 is idle, 1 is busy
        LinearConguentialGenerator generator = new LinearConguentialGenerator();
        generator.initialise(LCGValues.a[countRunTime], LCGValues.c[countRunTime], LCGValues.m, LCGValues.z[countRunTime]);
        random = LinearConguentialGenerator.generate();//generate random numbers using LinearCongruentialGenerator
        System.out.println("---------------------------------------Chi-Square Test-------------------------------------------");
        ChiSquareTest.chisquare(random);//throw in random values and test for uniformity
        System.out.print("-----------------------------------------Test for Autocorrelation-----------------------------------");
        TestForAutocorrelation.testForAutocorrelation(random);//throw in random values and test for independence
        System.out.println("------------------------------------------Start of Simulation--------------------------------------");
        RandomValuesSet.initialiseRandom(random);//initialize random variables 
        int interarrivalTime = RandomValuesSet.getRandom(RandomValuesSet.interarrivalSet);//get 1st patient interarrival time 
        System.out.println("DQ(t) = Doctor Queue\n"
                + "MQ(t) = Medicine Queue\n"
                + "D(t) = Doctor Status\n"
                + "N(t) = Nurse Status\n"
                + "NN(t) = Second Nurse Status When She is not Taking Doctor Role"
                + "ND(t) = Second Nurse Status When She is Taking Doctor Role");
        arrivalTime = State.clockTime + interarrivalTime;//initialize one arrival event
        Event event = new Event("ArrivalDoctorQueue", arrivalTime , patient);
        list.add(event);//add event to future event list
        start(list,doctorQueue,medicineQueue);
        
    }
    
    public static void start(FutureEventList list, Queue doctorQueue, Queue medicineQueue){
        int interarrivalTime;
        System.out.println("\n ------------------------------------------------- \n Clock " + State.clockTime);//display current clock time
        if(State.doctorExist == 2)//if doctor is not here
            System.out.println("Doctor is currently not in the clinic");
        else if(State.doctorExist==1)//if doctor is here
            System.out.println("Doctor is here");
        
        System.out.println("System states");//print states of current clock time
        System.out.println("DQ(t) " + State.doctorQueue + " MQ(t) " + State.medicineQueue + " D(t) " + State.doctorStatus + " NN(t) " + State.nurseIsNurse + " ND(t) " + State.nurseIsDoctor + " N(t) " + State.nurseStatus);
        System.out.print(" Doctor Queue: ");
         doctorQueue.display();//display patients that are in queue
        System.out.print("\n Medicine Queue: ");
        medicineQueue.display();
        System.out.print("\n Future Event List \n");
        list.display();//display future event list
         Event nextClockTimeEvent = list.remove();//remove and get the event which time closest to current clock time
        State.clockTime = nextClockTimeEvent.clockTime; //set next clock time to the event's time
        Event sameClockTimeEvent;//to find whether there is another event that will be occured at the clock time also
        if(list.findSameClockTime()==true){//if yes
            sameClockTimeEvent = list.remove();//remove it out and store in sameClockTimeEvent
            if(sameClockTimeEvent.type.equals("ArrivalDoctorQueue")){
                totalNumberOfPatientDoctor++;//new arrival, add total number of customers
                arrivalDoctor(list,sameClockTimeEvent,doctorQueue,medicineQueue);//handle patient's arrival
            }
            if(sameClockTimeEvent.type.equals("DepartureNurseWithDoctorRole")){
                totalNumberOfPatientNurse++;//departure of doctor, patients enter nurse to claim medicine, total number of patients in nurse plus one
                departureNurseWithDoctorRole(list,sameClockTimeEvent,doctorQueue,medicineQueue);//handle departure event of patient
            }
            if(sameClockTimeEvent.type.equals("DepartureNurseWithoutDoctorRole")){
                departureNurseWithoutDoctorRole(list, sameClockTimeEvent, doctorQueue, medicineQueue);//departure of nurse
            }
            if(sameClockTimeEvent.type.equals("DepartureDoctor")){//new departure
                totalNumberOfPatientNurse++;//patients directly enter nurse service, so total number ++
                departureDoctor(list,sameClockTimeEvent,doctorQueue,medicineQueue);//handle departure event of the patient
            }
            if(sameClockTimeEvent.type.equals("DepartureNurse"))//patient finish claiming medicine
                departureNurse(list,sameClockTimeEvent,doctorQueue,medicineQueue);//handle departure event
            if(sameClockTimeEvent.type.equals("DoctorComeBack"))//if clock time equals to doctoro come back time
                doctorComeBack(list,sameClockTimeEvent,doctorQueue,medicineQueue);//handle doctor come back event
            
            if(sameClockTimeEvent.clockTime == stoppingSimulation)//if clock time reach 360, stop simulation
                return;
        }//same as above
        if(nextClockTimeEvent.type.equals("ArrivalDoctorQueue")){
            totalNumberOfPatientDoctor++;
            arrivalDoctor(list,nextClockTimeEvent,doctorQueue,medicineQueue);
        }
        if(nextClockTimeEvent.type.equals("DepartureNurseWithDoctorRole")){
                totalNumberOfPatientNurse++;
                departureNurseWithDoctorRole(list,nextClockTimeEvent,doctorQueue,medicineQueue);
            }
            if(nextClockTimeEvent.type.equals("DepartureNurseWithoutDoctorRole")){
                departureNurseWithoutDoctorRole(list, nextClockTimeEvent, doctorQueue, medicineQueue);
            }
        if(nextClockTimeEvent.type.equals("DepartureDoctor")){
            totalNumberOfPatientNurse++;
            departureDoctor(list,nextClockTimeEvent,doctorQueue,medicineQueue);
        }
        if(nextClockTimeEvent.type.equals("DepartureNurse"))
            departureNurse(list,nextClockTimeEvent,doctorQueue,medicineQueue);
        
        if(nextClockTimeEvent.type.equals("DoctorComeBack")){
            doctorComeBack(list, nextClockTimeEvent, doctorQueue, medicineQueue);
        }
        if(nextClockTimeEvent.type.equals("StopSimulation"))
            return;
        
        
        
        
        interarrivalTime = RandomValuesSet.getRandom(RandomValuesSet.interarrivalSet);//after events are handled, schedule new arrival event
        arrivalTime += interarrivalTime;
        Event event = new Event("ArrivalDoctorQueue", arrivalTime, ++patient);
        list.add(event);
        start(list,doctorQueue,medicineQueue);//loop and handle next event
    }

    private static void arrivalDoctor(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue) {
        int departureTime, doctorServiceTime, emergency = RandomValuesSet.getRandom(RandomValuesSet.emergencySet);
        if(emergency==2)//if patient need emergency treatment
                totalNumberOfEmergencyPatient++;//add total emergency patients
        if(State.doctorStatus == 0 && State.doctorExist==1){//if doctor is here and doctor is not busy
            State.doctorStatus = 1;//make him busy!
            doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);//schedule doctor service time
            doctorService.add(doctorServiceTime);//add into service time array
            departureTime = State.clockTime + doctorServiceTime;//schedule departure time of the patient
            totalSpentDoctor += departureTime - State.clockTime;//calculate total time spent in the system for the patient
            list.add(new Event("DepartureDoctor", departureTime, nextClockTimeEvent.patient));//add in future event list
        }
        else if(State.doctorStatus==1 && State.doctorExist==1){//doctor is here and doctor is busy
            State.doctorQueue++;//queue plus 1
            delayedPatientDoctor++;//delayed patient plus one
            doctorQueue.add(nextClockTimeEvent.patient, emergency);//add patient in Queue
        }
        else if(State.doctorExist == 2 && State.nurseIsDoctor==0 && State.nurseIsNurse==0){//doctor is not here, nurse with doctor capability is free
            if(emergency==2)//if patient need emergency treatment
                totalNumberOfEmergencyPatient++;//add total emergency patients
            State.nurseIsDoctor=1;//make nurse take doctor role and status = busy
            doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);//schedule service time for the patient
            doctorService.add(doctorServiceTime);//add doctor service time array
            departureTime = State.clockTime + doctorServiceTime;//schedule departure time for the patient
            totalSpentDoctor+=departureTime - State.clockTime;//calculate total spent time of the patient
            list.add(new Event("DepartureNurseWithDoctorRole", departureTime, nextClockTimeEvent.patient));//add departure doctor event(Handled by nurse with doctor capability) to future event list
        }
        else if(State.doctorExist == 2 && (State.nurseIsDoctor==1 || State.nurseIsNurse==1)){//doctor is not here and nurse with doctor capability is busy
            State.doctorQueue++;//doctor queue plus one
            delayedPatientDoctor++;//delayed patient plus one
            doctorQueue.add(nextClockTimeEvent.patient, emergency);//add patient in Queue
        }
    }
    
    private static void departureDoctor(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue){
        int nurseServiceTime, departureTimeNurse;
        if(State.doctorExist==1){//if doctor is here
            if(State.nurseStatus==0 || (State.nurseIsNurse==0 && State.nurseIsDoctor==0)){ //if either one nurse is free and nurse with doctor capability must not handling a patient
                nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);//nurse handle patient and schedule service time for him
                nurseService.add(nurseServiceTime);//add into nurse service time array
                departureTimeNurse = State.clockTime + nurseServiceTime;//schedule nurse departure time for the patient
                totalSpentNurse += departureTimeNurse - State.clockTime;//calculate total time spent by the patient
                if(State.nurseStatus==0){//if 1st nurse is free, let 1st nurse handle the patient
                    State.nurseStatus=1;
                    list.add(new Event("DepartureNurse", departureTimeNurse, nextClockTimeEvent.patient));//add into future event list
                }
                else{//else, handle by second nurse(with doctor capability)
                    State.nurseIsNurse=1;
                    list.add(new Event("DepartureNurseWithoutDoctorRole", departureTimeNurse,nextClockTimeEvent.patient));//add into future event list
                }
            }
            
            else if ((State.nurseIsNurse==1 && State.nurseStatus==1) || (State.nurseIsDoctor==1 && State.nurseStatus==1)){// if both nurses are busy
                State.medicineQueue++;//queue plus one
                delayedPatientNurse++;//delayed patient pluss 1
                medicineQueue.add(nextClockTimeEvent.patient, 0);//add patient into medicine queue with emergency 0(emergency play no role here)
            }
        }
        else if(State.doctorExist == 2){//if doctor is not here
            if(State.nurseStatus==1){//if first nurse is busy
                State.medicineQueue++;//queue plus one
                delayedPatientNurse++;//delayed patient plus 1
                medicineQueue.add(nextClockTimeEvent.patient, 0);//add patient in to medicine queue
            }
            else if(State.nurseStatus==0){//else if first nurse is here...
                State.nurseStatus=1;//make her busy!
                nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);//schedule service time for the patient
                nurseService.add(nurseServiceTime);//add service time into nurse service time array
                departureTimeNurse = State.clockTime + nurseServiceTime;//schedule departure time
                totalSpentNurse += departureTimeNurse - State.clockTime;//calculate total time spent
                list.add(new Event("DepartureNurse", departureTimeNurse, nextClockTimeEvent.patient));//add departure event of nurse in future event list
            }
        }
        State.doctorStatus = 0;//once patient depart from doctor, make doctor free.
        State.doctorExist = RandomValuesSet.getRandom(RandomValuesSet.doctorExist);//check whether doctor want to leave the clinic
        int departureTime, doctorServiceTime;
        if(State.doctorExist==1){//if no
            if (State.doctorQueue != 0) {//if doctor queue is not empty
            State.doctorStatus = 1;//make doctor busy
            State.doctorQueue--;//queue minus 1
            doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);//schedule service time for the patient
            doctorService.add(doctorServiceTime);//add srvice time array
            int arrivalTime = doctorQueue.removeArrivalTime();//get the arrival time of particular patient
            int emergency = doctorQueue.removeEmergencyStatus();//check whether the patient is emergency
            if(emergency == 2){//is yes
                totalWaitingTimeEmergency += (State.clockTime-arrivalTime);//calculate the waiting time and add into totalWaitingTimeEmergency
            }
            totalDelayTimeDoctor += (State.clockTime-arrivalTime);//calculate and add total waiting time of patient in doctor queue
            departureTime = State.clockTime + doctorServiceTime;//schedule departure time
            totalSpentDoctor += departureTime - arrivalTime;//calculate total time patient spent in the system
            list.add(new Event("DepartureDoctor", departureTime, doctorQueue.remove()));//add departure event in future event list
            }
        }
        else{ //doctor want to leave
            if(State.doctorQueue!=0 && State.nurseIsNurse == 0 && State.nurseIsDoctor != 1){//if there is patient in doctor queue and nurse with doctor capability is free
                State.nurseIsDoctor=1;//make nurse with doctor capability take doctor role
                State.doctorQueue--;//queue minus 1
                doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);//schedule service time
                doctorService.add(doctorServiceTime);//add service time array
                int arrivalTime = doctorQueue.removeArrivalTime();//get patient arrival time
                int emergency = doctorQueue.removeEmergencyStatus();//check patient emergency status
                if(emergency == 2){//if patient is emergency
                    totalWaitingTimeEmergency += (State.clockTime-arrivalTime);//calculate his waiting time and add into emergency patients waiting time
                }
                totalDelayTimeDoctor+=(State.clockTime-arrivalTime);//add into total waiting time of overall
                departureTime = State.clockTime+doctorServiceTime;//schedule departure
                totalSpentDoctor+=departureTime-arrivalTime;//calculate total spent
                list.add(new Event("DepartureNurseWithDoctorRole", departureTime, doctorQueue.remove()));//add departure event(Handle by nurse with doctor capability) into fututr event list
            }
            int doctorLeaveTime = RandomValuesSet.getRandom(RandomValuesSet.doctorLeaveTime);//schedule when will doctor come back
            list.add(new Event("DoctorComeBack", State.clockTime+doctorLeaveTime, 0));//add doctor come back event into future event list
            
        }
    }
    
    private static void departureNurseWithDoctorRole(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue){
        int nurseServiceTime, departureTime, departureTimeNurse, doctorServiceTime;
        State.nurseIsDoctor=0;//make nurse with doctor capability idle
        if(State.doctorExist == 2){//if doctor is still not here//patient go to first nurs
            if(State.nurseStatus==1){//if first nurse is busy
                State.medicineQueue++;//patient wait at queue
                delayedPatientNurse++;//plus delayed patient in nurse
                medicineQueue.add(nextClockTimeEvent.patient, 0);//add patient into medicine queue
            }
            else if(State.nurseStatus==0){//else
                State.nurseStatus=1;//make first nurse busy
                nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);//schedule service time
                nurseService.add(nurseServiceTime);//add nurse service time array
                departureTimeNurse = State.clockTime + nurseServiceTime;//schedule departure
                totalSpentNurse += departureTimeNurse - State.clockTime;//calculate total time spent
                list.add(new Event("DepartureNurse", departureTimeNurse, nextClockTimeEvent.patient));//add departure event in future event list
            }
            
            if(State.doctorQueue!=0){//if doctor queue is not empty
                State.nurseIsDoctor=1;//nurse with doctor capability continue take doctor role
                State.doctorQueue--;//queue minus 1
                doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);
                doctorService.add(doctorServiceTime);
                int arrivalTime = doctorQueue.removeArrivalTime();
                int emergency = doctorQueue.removeEmergencyStatus();
                if(emergency == 2){
                    totalWaitingTimeEmergency += (State.clockTime-arrivalTime);
                }
                totalDelayTimeDoctor+=(State.clockTime-arrivalTime);
                departureTime = State.clockTime+doctorServiceTime;
                totalSpentDoctor+=departureTime-arrivalTime;
                list.add(new Event("DepartureNurseWithDoctorRole", departureTime, doctorQueue.remove()));//add doctor departure event which handle by nurse with doctor capability
            }
        }
        else if(State.doctorExist==1){// else if doctor is here
            if(State.medicineQueue!=0){//if medicine queue is not empty
                State.nurseIsNurse = 1;//nurse with doctor capability remain to be nurse and serve patients in medicine queue
                State.medicineQueue--;
                nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);//schedule nurse service time
                nurseService.add(nurseServiceTime);
                int arrivalTime = medicineQueue.removeArrivalTime();
                totalDelayTimeNurse += (State.clockTime-arrivalTime);
                departureTime = State.clockTime + nurseServiceTime;
                totalSpentNurse += departureTime - arrivalTime;
                list.add(new Event("DepartureNurseWithoutDoctorRole", departureTime, medicineQueue.remove()));//add nurse departure event 
                State.medicineQueue++;//the patient served by nurse with capability just now will be enqueued to medicine queue
                medicineQueue.add(nextClockTimeEvent.patient, 0);//add the patient to medicine queue
                }
                else{//if medicine queue is empty then
                    State.nurseIsNurse=1;//nurse with doctor capability remain as nurse
                    nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
                    nurseService.add(nurseServiceTime);
                    departureTime = State.clockTime + nurseServiceTime;
                    totalSpentNurse += departureTime - State.clockTime;
                    list.add(new Event("DepartureNurseWithoutDoctorRole", departureTime, nextClockTimeEvent.patient));//the patient served by nurse with doctor capability just now will be served by the same nurse too
                }
            
        }
    }
    
    private static void departureNurseWithoutDoctorRole(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue){
        int departureTime, doctorServiceTime, nurseServiceTime;
        State.nurseIsNurse=0;//nurse with doctor capability idle
        if(State.doctorExist == 2){//if doctor is not here
            if(State.doctorQueue!=0){//doctor queue isnt empty
                State.nurseIsDoctor=1;//take doctor role
                State.doctorQueue--;
                doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);
                doctorService.add(doctorServiceTime);
                int arrivalTime = doctorQueue.removeArrivalTime();
                int emergency = doctorQueue.removeEmergencyStatus();
                if(emergency == 2){
                    totalWaitingTimeEmergency += (State.clockTime-arrivalTime);
                }
                totalDelayTimeDoctor+=(State.clockTime-arrivalTime);
                departureTime = State.clockTime+doctorServiceTime;
                totalSpentDoctor+=departureTime-arrivalTime;
                list.add(new Event("DepartureNurseWithDoctorRole", departureTime, doctorQueue.remove()));
            }
        }
        else if(State.doctorExist==1){//doctor is here
            if(State.medicineQueue!=0){//medicine queue not empty
                State.nurseIsNurse = 1;//remain as nurse
                State.medicineQueue--;//serve ppl in medicine queue
                nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
                nurseService.add(nurseServiceTime);
                int arrivalTime = medicineQueue.removeArrivalTime();
                totalDelayTimeNurse += (State.clockTime-arrivalTime);
                departureTime = State.clockTime + nurseServiceTime;
                totalSpentNurse += departureTime - arrivalTime;
                list.add(new Event("DepartureNurseWithoutDoctorRole", departureTime, medicineQueue.remove()));
        }
        }
    }
    
    private static void departureNurse(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue){
        int departureTime, nurseServiceTime;
        State.nurseStatus = 0;//patient claimed their medicine, nurse free
        if(State.medicineQueue!=0){//but if there are still patient in medicine queue
            State.nurseStatus = 1;//nurse become busy again
            State.medicineQueue--;//medicine queue minus 1
            nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);//schedule service time of the patient
            nurseService.add(nurseServiceTime);//add nurse service time array
            int arrivalTime = medicineQueue.removeArrivalTime();//get particular patient arrival time
            totalDelayTimeNurse += (State.clockTime-arrivalTime);//calculate his waiting time and add into total delay time of all patients
            departureTime = State.clockTime + nurseServiceTime;//scheule departure time of the patient
            totalSpentNurse += departureTime - arrivalTime;//calculate total time patient spent in nurse
            list.add(new Event("DepartureNurse", departureTime, medicineQueue.remove()));//add departure event in future event list
        }
    }

    private static void doctorComeBack(FutureEventList list, Event nextClockTimeEvent, Queue doctorQueue, Queue medicineQueue) {
        State.doctorExist=1;//doctor is here
        if (State.doctorQueue != 0) {//if queue is not empty
                    State.doctorStatus = 1;//make doctor busy
                    State.doctorQueue--;//minus one from the queue
                    int doctorServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeDoctor);//schedule service time for the patient
                    doctorService.add(doctorServiceTime);//add doctor service time array
                    int departureTime = State.clockTime + doctorServiceTime;//schedule departure time of the patient
                    list.add(new Event("DepartureDoctor", departureTime, doctorQueue.remove()));//add departure event in future event list
        }
        if(State.medicineQueue!=0 && State.nurseIsDoctor == 0){//since doctor is back, nurse dont have to take doctor role anymore, so if there is patients in the queue, nurse can serve them as well
            int nurseServiceTime, departureTime;
            State.nurseIsNurse = 1;
                State.medicineQueue--;
                nurseServiceTime = RandomValuesSet.getRandom(RandomValuesSet.serviceTimeNurse);
                nurseService.add(nurseServiceTime);
                int arrivalTime = medicineQueue.removeArrivalTime();
                totalDelayTimeNurse += (State.clockTime-arrivalTime);
                departureTime = State.clockTime + nurseServiceTime;
                totalSpentNurse += departureTime - arrivalTime;
                list.add(new Event("DepartureNurseWithoutDoctorRole", departureTime, medicineQueue.remove()));
        }
    }
}
