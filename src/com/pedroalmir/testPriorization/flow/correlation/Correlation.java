package com.pedroalmir.testPriorization.flow.correlation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import au.com.bytecode.opencsv.CSVWriter;

public class Correlation {
	private final static String BASE_FOLDER = "C:/Eclipse/junojee/mestrado/regressionTestingPriorization/experimentos/";
    
    public static void main(String[] args) throws IOException {
            int contClass;
            
            //1000-20
            contClass = 1000;
            generetaFiles(contClass, contClass/50);
            
            //1000-100
            contClass = 1000;
            //generetaFiles(contClass, contClass/10);
            
            //5000-500
            contClass = 5000;
           // generetaFiles(contClass, contClass/10);
            
            //10000-200
            contClass = 1000;
           // generetaFiles(contClass, contClass/50);
            
            //to see more about gaussian number generated, open this link: http://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
           //Correlation.testGaussianNumberGenerate(0,15,15,30,45);      
    }
    
    public static void generetaFiles(int contClass, int reqCount) throws IOException{
            generateSQFD(contClass, reqCount, 5, BASE_FOLDER + "SQFD-cl-"+contClass+"-req-"+reqCount+".csv");
            generateTest(contClass, reqCount, BASE_FOLDER + "TestCoverage-cl-"+contClass+"-req-"+reqCount+".csv");
            generateCoupling(contClass, BASE_FOLDER + "Coupling-cl-"+contClass+".csv");
    }
    
    private static void generateCoupling(int numberOfClass, String addressFile) throws IOException {
            Random random = new Random();
            
            List<String[]> data = new ArrayList<String[]>();
            CSVWriter writer = new CSVWriter(new FileWriter(addressFile), ';');
            /* Header */
            String[] header = new String[numberOfClass];
            for(int i = 0; i < numberOfClass; i++){
                    header[i] = "Class["+(i+1)+"]";
            }
            data.add(header);
            /* Body */
            String[] body = new String[numberOfClass];
            for(int i = 0; i < numberOfClass; i++){
                    body[i] = random.nextInt(11) + "";
            }
            data.add(body);
            writer.writeAll(data);
            writer.close();
    }

    public static void generateSQFD(int numberOfClass, int numberOfRequirement, int countClient, String addressFile) throws IOException{
            
    	int cont9 = 220, cont3 = 170, cont1 = 90;
    	double gaussian;
    	
        Random random = new Random();
        String[] stringLine;
        List<String[]> data = new ArrayList<String[]>();
        
        CSVWriter writer = new CSVWriter(new FileWriter(addressFile), ';');
        
        //add 2 to generate client priority column and description
        numberOfClass += 1 + countClient;
        //add 1 to generate description
        numberOfRequirement++;
        
        //each line
        for(int req = 0; req < numberOfRequirement; req++){
                stringLine = new String[numberOfClass];
                //each column
                for(int clas = 0; clas < numberOfClass; clas++){
                        //title of first row
                        if(req==0){
                                if(clas==0){
                                        stringLine[clas] = "Requirement\\Class";
                                }else if(clas < numberOfClass-countClient){
                                        stringLine[clas] = "Class["+clas+"]";
                                }else{
                                        stringLine[clas] = "Client Priority";
                                }
                        }else{
                                //title of row
                                if(clas==0){
                                        stringLine[clas] = "Requirement["+req+"]";
                                }else if(clas < numberOfClass-countClient){
                                	gaussian = generateGaussianNumber(0, 100);
                                        stringLine[clas] = gaussian >= cont9 ? "9" : (gaussian >= cont3 ? "3" : (gaussian >= cont1 ? "1" : "0"));
                                }else{
                                        stringLine[clas] = new Integer(random.nextInt(11)).toString();
                                }
                                
                        }
                }
                data.add(stringLine);
        }
        
        writer.writeAll(data);
        writer.close();
    }
    
    public static void generateTest(int numberOfClass, int numberOfTests, String addressFile) throws IOException{
            Random random = new Random();
            String[] stringLine;
            List<String[]> data = new ArrayList<String[]>();
            
            CSVWriter writer = new CSVWriter(new FileWriter(addressFile), ';');
            
            //add 2 to generate client priority column and description
            numberOfClass += 2;
            //add 1 to generate description
            numberOfTests++;
            
            double countCoverage, coverage;
            double minTemp = 1;
            double maxTemp = 4;
            
            //each line
            for(int test = 0; test < numberOfTests; test++){
                    stringLine = new String[numberOfClass];
                    countCoverage = 0;
                    //each column
                    for(int clas = 0; clas < numberOfClass; clas++){
                            //title of first row
                            if(test==0){
                                    if(clas==0){
                                            stringLine[clas] = "Test\\Class";
                                    }else if(clas < numberOfClass-1){
                                            stringLine[clas] = "Class["+clas+"]";
                                    }else{
                                            stringLine[clas] = "Temp";
                                    }
                            }else{
                                    //title of row
                                    if(clas==0){
                                            stringLine[clas] = "Test["+test+"]";
                                    }else{
                                    	if(clas < numberOfClass-1){
                                    		coverage = generateGaussianNumber(0, 30);
                                        	if(coverage >= 60){
                                        		coverage = coverage < 100 ? coverage : 100; 
                                        		stringLine[clas] = String.format("%.4f", coverage);
                                                countCoverage += coverage;
                                        	}else{
                                        		stringLine[clas] = "0";
                                        	}
                                    	}else{
                                    		stringLine[clas] = String.format("%.4f", (new Double(minTemp + (maxTemp - minTemp) * random.nextDouble())) * countCoverage );
                                    	}
                                    }
                            }
                    }
                    data.add(stringLine);
            }
            
            writer.writeAll(data);
            writer.close();
    }
    
    public static void testGaussianNumberGenerate(int centerValue, int standardDeviation, int x1, int x2, int x3){
        int inside15 = 0, inside30 = 0, inside45 = 0, count = 1000;
        double maxval = 0, val;
        for(int i = 0; i < count; i++){
        	val = generateGaussianNumber(centerValue, standardDeviation);
        	if(val <= x1){
        		inside15++;
        	}
        	if(val <= x2){
        		inside30++;
        	}
        	if(val <= x3){
        		inside45++;
        	}
        	if(maxval<val){
        		maxval = val;
        	}
        	System.out.println("val: "+val);
        }
        System.out.println("maxval: "+maxval+" in: "+count);
        System.out.println(x1+": "+inside15+" - "+new Double(inside15)/(count/100));
        System.out.println(x2+": "+inside30+" - "+new Double(inside30)/(count/100));
        System.out.println(x3+": "+inside45+" - "+new Double(inside45)/(count/100));
    }
    
    public static double generateGaussianNumber(int centerValue, int standardDeviation){
    	Random random = new Random();
    	double val, dif;
    	val = random.nextGaussian() * standardDeviation + centerValue;
    	dif = (centerValue-val);
    	//generate values larger than centerValue
    	dif = dif < 0 ? -dif : dif;
    	val = centerValue + dif;
    	return val;
    }
}
