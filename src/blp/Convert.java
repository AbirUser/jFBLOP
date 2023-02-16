package blp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author marouen
 */
public class Convert {

    public static void main(String[] args) {
        String[] problems = {"A-n32-k5.vrp", "A-n33-k5.vrp", "A-n33-k6.vrp", "A-n34-k5.vrp", "A-n36-k5.vrp", "A-n37-k5.vrp", "A-n37-k6.vrp", "A-n38-k5.vrp", "A-n39-k5.vrp", "A-n39-k6.vrp", "A-n44-k7.vrp", "A-n45-k6.vrp", "A-n45-k7.vrp", "A-n46-k7.vrp", "A-n48-k7.vrp", "A-n53-k7.vrp", "A-n54-k7.vrp", "A-n55-k9.vrp", "A-n60-k9.vrp", "A-n61-k9.vrp", "A-n62-k8.vrp", "A-n63-k10.vrp", "A-n63-k9.vrp", "A-n64-k9.vrp", "A-n65-k9.vrp", "A-n69-k9.vrp", "A-n80-k10.vrp", "B-n31-k5.vrp", "B-n34-k5.vrp", "B-n35-k5.vrp", "B-n38-k6.vrp", "B-n39-k5.vrp", "B-n41-k6.vrp", "B-n43-k6.vrp", "B-n44-k7.vrp", "B-n45-k5.vrp", "B-n45-k6.vrp", "B-n50-k7.vrp", "B-n50-k8.vrp", "B-n51-k7.vrp", "B-n52-k7.vrp", "B-n56-k7.vrp", "B-n57-k7.vrp", "B-n57-k9.vrp", "B-n63-k10.vrp", "B-n64-k9.vrp", "B-n66-k9.vrp", "B-n67-k10.vrp", "B-n68-k9.vrp", "B-n78-k10.vrp", "E-n101-k14.vrp", "E-n101-k8.vrp", "E-n13-k4.vrp", "E-n22-k4.vrp", "E-n23-k3.vrp", "E-n30-k3.vrp", "E-n30-k4.vrp", "E-n31-k7.vrp", "E-n33-k4.vrp", "E-n51-k5.vrp", /*"E-n7.vrp",*/ "E-n76-k10.vrp", "E-n76-k14.vrp", "E-n76-k15.vrp", "E-n76-k7.vrp", "E-n76-k8.vrp"};
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "blp" + System.getProperty("file.separator") + "problems" + System.getProperty("file.separator") + "instances" + System.getProperty("file.separator") + "vrp" + System.getProperty("file.separator");
        for (String problem : problems) {
            try {
                //System.out.println(problem);
                InputStream ips = new FileInputStream(path + problem);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String ligne;
                int ligneNumber = 1;
                int numberOfVehicles = 0;
                int numberOfCustomers = 0;
                int capacity = 0;
                Customer[] customers = null;
                while ((ligne = br.readLine()) != null) {
                    if (ligneNumber == 1) {
                        numberOfVehicles = Integer.parseInt(ligne.substring(ligne.indexOf("-k") + 2, ligne.length()));
                    }
                    if (ligneNumber == 4) {
                        numberOfCustomers = Integer.parseInt(ligne.substring(ligne.indexOf("DIMENSION : ") + 12, ligne.length())) - 1;
                        customers = new Customer[numberOfCustomers + 1];
                    }
                    if (ligneNumber == 6) {
                        capacity = Integer.parseInt(ligne.substring(ligne.indexOf("CAPACITY : ") + 11, ligne.length()));
                    }
                    if (ligneNumber > 7 && ligneNumber < 9 + numberOfCustomers) {
                        ligne = ligne.trim();
                        int id = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                        ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                        ligne = ligne.trim();
                        int coordinateX = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                        ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                        ligne = ligne.trim();
                        int coordinateY = Integer.parseInt(ligne.substring(0, ligne.length()));
                        Customer customer = new Customer();
                        customer.setCoordinateX(coordinateX);
                        customer.setCoordinateY(coordinateY);
                        if (ligneNumber == 8) {
                            customer.setId(numberOfCustomers + 1);
                            customers[numberOfCustomers] = customer;
                        } else {
                            customer.setId(id - 1);
                            customers[id - 2] = customer;
                        }
                    }
                    if (ligneNumber > 9 + numberOfCustomers && ligneNumber < 9 + numberOfCustomers * 2) {
                        ligne = ligne.trim();
                        int id = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                        ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                        ligne = ligne.trim();
                        int demand = Integer.parseInt(ligne.substring(0, ligne.length()));
                        if (ligneNumber == 10 + numberOfCustomers) {
                            customers[numberOfCustomers].setDemand(demand);
                        } else {
                            customers[id - 2].setDemand(demand);
                        }
                    }
                    ligneNumber++;
                }
                System.out.print(problem + " => ");
                File file = new File(path + System.getProperty("file.separator") + "_" + problem);
                FileWriter writerFile = new FileWriter(file);
                writerFile.write("0 " + numberOfVehicles + " " + numberOfCustomers + " 1");
                writerFile.write(System.getProperty("line.separator"));
                writerFile.write("0 " + capacity);
                writerFile.write(System.getProperty("line.separator"));
                for (Customer customer : customers) {
                    if (customer.getId() == numberOfCustomers + 1) {
                        writerFile.write(customer.getId() + " " + customer.getCoordinateX() + " " + customer.getCoordinateY() + " 0 " + customer.getDemand() + " 0 0");
                        writerFile.write(System.getProperty("line.separator"));
                    } else {
                        writerFile.write(customer.getId() + " " + customer.getCoordinateX() + " " + customer.getCoordinateY() + " 0 " + customer.getDemand() + " 0 0 0 0 0 0");
                        writerFile.write(System.getProperty("line.separator"));
                    }
                }
                writerFile.close();
                System.out.println("done");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    private static class Customer {

        private int id;
        private String type;
        private int coordinateX;
        private int coordinateY;
        private int serviceDuration;
        private int demand;
        private int frequencyOfVisit;
        private int numberOfPossibleVisitCombinations;
        private int[] listOfPossibleVisitCombinations;
        private int beginningOfTime;
        private int endOfTime;

        public Customer() {

        }

        public Customer(int id, String type, int coordinateX, int coordinateY, int serviceDuration, int demand, int frequencyOfVisit, int numberOfPossibleVisitCombinations, int[] listOfPossibleVisitCombinations, int beginningOfTime, int endOfTime) {
            this.id = id;
            this.type = type;
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
            this.serviceDuration = serviceDuration;
            this.demand = demand;
            this.frequencyOfVisit = frequencyOfVisit;
            this.numberOfPossibleVisitCombinations = numberOfPossibleVisitCombinations;
            this.listOfPossibleVisitCombinations = listOfPossibleVisitCombinations;
            this.beginningOfTime = beginningOfTime;
            this.endOfTime = endOfTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCoordinateX() {
            return coordinateX;
        }

        public void setCoordinateX(int coordinateX) {
            this.coordinateX = coordinateX;
        }

        public int getCoordinateY() {
            return coordinateY;
        }

        public void setCoordinateY(int coordinateY) {
            this.coordinateY = coordinateY;
        }

        public int getServiceDuration() {
            return serviceDuration;
        }

        public void setServiceDuration(int serviceDuration) {
            this.serviceDuration = serviceDuration;
        }

        public int getDemand() {
            return demand;
        }

        public void setDemand(int demand) {
            this.demand = demand;
        }

        public int getFrequencyOfVisit() {
            return frequencyOfVisit;
        }

        public void setFrequencyOfVisit(int frequencyOfVisit) {
            this.frequencyOfVisit = frequencyOfVisit;
        }

        public int getNumberOfPossibleVisitCombinations() {
            return numberOfPossibleVisitCombinations;
        }

        public void setNumberOfPossibleVisitCombinations(int numberOfPossibleVisitCombinations) {
            this.numberOfPossibleVisitCombinations = numberOfPossibleVisitCombinations;
        }

        public int[] getListOfPossibleVisitCombinations() {
            return listOfPossibleVisitCombinations;
        }

        public void setListOfPossibleVisitCombinations(int[] listOfPossibleVisitCombinations) {
            this.listOfPossibleVisitCombinations = listOfPossibleVisitCombinations;
        }

        public int getBeginningOfTime() {
            return beginningOfTime;
        }

        public void setBeginningOfTime(int beginningOfTime) {
            this.beginningOfTime = beginningOfTime;
        }

        public int getEndOfTime() {
            return endOfTime;
        }

        public void setEndOfTime(int endOfTime) {
            this.endOfTime = endOfTime;
        }

        @Override
        public String toString() {
            return "Customer{" + "id=" + id + ", coordinateX=" + coordinateX + ", coordinateY=" + coordinateY + ", serviceDuration=" + serviceDuration + ", demand=" + demand + ", frequencyOfVisit=" + frequencyOfVisit + ", numberOfPossibleVisitCombinations=" + numberOfPossibleVisitCombinations + ", listOfPossibleVisitCombinations=" + listOfPossibleVisitCombinations + ", beginningOfTime=" + beginningOfTime + ", endOfTime=" + endOfTime + '}';
        }
    }

}
