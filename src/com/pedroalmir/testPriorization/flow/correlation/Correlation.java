package com.pedroalmir.testPriorization.flow.correlation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import au.com.bytecode.opencsv.CSVWriter;

public class Correlation {
	private final static String BASE_FOLDER = "E:/Matheus/dadosGerados/";
    
    public static void main(String[] args) throws IOException {
            int contClass;
            
            //1000-20
            contClass = 1000;
            //generetaFiles(contClass, contClass/50);
            
            //1000-100
            contClass = 1000;
            //generetaFiles(contClass, contClass/10);
            
            //5000-500
            contClass = 5000;
            generetaFiles(contClass, contClass/10);
            
            //10000-200
            contClass = 1000;
            //generetaFiles(contClass, contClass/50);
    }
    
    public static void generetaFiles(int contClass, int reqCount) throws IOException{
            generateSQFD(contClass, reqCount, 5, BASE_FOLDER + "New-SQFD-cl-"+contClass+"-req-"+reqCount+".csv");
            generateTest(contClass, reqCount, BASE_FOLDER + "New-TestCoverage-cl-"+contClass+"-req-"+reqCount+".csv");
            generateCoupling(contClass, BASE_FOLDER + "New-Coupling-cl-"+contClass+".csv");
    }
    
    private static void generateCoupling(int numberOfClass, String addressFile) throws IOException {
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
            List<Double> allImportanceValue = getAllNormalizedValues(numberOfClass, 10, 50, 15);
            for(int i = 0; i < numberOfClass; i++){
            	body[i] = String.format("%.0f", allImportanceValue.get(i));
            }
            data.add(body);
            writer.writeAll(data);
            writer.close();
    }

    public static void generateSQFD(int numberOfClass, int numberOfRequirement, int countClient, String addressFile) throws IOException{
            
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
                List<Double> allImportanceValue = getAllNormalizedValues(numberOfClass, 10, 50, 15);
            	List<Double> allCorrelationValues = getAllNormalizedValues(numberOfClass, 4, 50, 14);
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
                                	stringLine[clas] = getCorrelationValue(allCorrelationValues.get(clas));
                                }else{
                                	stringLine[clas] = String.format("%.0f", allImportanceValue.get(clas));
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
                                    		coverage = getTestValue(50, 20);
                                    		countCoverage+=coverage;
                                    		stringLine[clas] = String.format("%.4f", coverage);
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
    
    /**
     * Retorna todas as importancias normalizadas a partir da quantidade passada, do valor central e do desvio padrão 
     * @param cont
     * @param ctr
     * @param std
     * @return
     */
    public static List<Double> getAllNormalizedValues(int cont, int maximum, int ctr, int std){
    	List<Double> list = new ArrayList<Double>();
    	double max = Integer.MIN_VALUE, atual;
    	for(int i = 0; i < cont; i++){
    		atual = generateGaussianNumber(ctr, std, false);
    		if(atual > max){
    			max = atual;
    		}
    		list.add(atual);
    	}
    	
    	List<Double> finalList = new ArrayList<Double>();
    	for (Double d : list) {
    		finalList.add((d/max) * maximum);
		}
    	return finalList;
    }
    
    /**
     * Retorna o valor da cobertura do teste
     * @param ctr
     * @param std
     * @return
     */
    public static Double getTestValue(int ctr, int std){
    	double coverage = generateGaussianNumber(ctr, std, false);
		coverage = coverage < 0 ? 0 : coverage;
		coverage = coverage > 100 ? 100 : coverage;
		return coverage;
    }
    
    public static String getCorrelationValue(double normalized){
    	String retorno = "";
    	if(normalized <= 1){
    		retorno = "3";
    	}else if(normalized > 1 && normalized <= 2){
    		retorno = "0";
    	}else if(normalized > 2 && normalized <= 3){
    		retorno = "1";
    	}else if(normalized > 3 && normalized <= 4){
    		retorno = "9";
    	}
    	return retorno;
    }
    
    /**
     * Retorn o valor da correlação a partir do valor central e do desvio padrão.
     * @param ctr
     * @param std
     * @return
     */
    @Deprecated
    public static String getCorrelationValue(int ctr, int std){
    	double val = generateGaussianNumber(ctr, std, false);
    	String retorno;
    	if(val > ctr - std && val < ctr + std){
    		retorno = "0";
    	}else if(val >= ctr + std && val < ctr + (2*std)){
    		retorno = "1";
    	}else if(val <= ctr - std && val > ctr - (2*std)){
    		retorno = "3";
    	}else{
    		retorno = "9";
    	}
    	return retorno;
    }
    
    
    public static double generateGaussianNumber(int centerValue, int standardDeviation, boolean invert){
    	Random random = new Random();
    	double val, dif;
    	val = random.nextGaussian() * standardDeviation + centerValue;
    	dif = (centerValue-val);
    	//generate values larger than centerValue
    	if(invert){
    		dif = dif < 0 ? -dif : dif;
    	}
    	val = centerValue + dif;
    	return val;
    }
    
    public static int getPoisson(double lambda) {
    	  double L = Math.exp(-lambda);
    	  double p = 1.0;
    	  int k = 0;

    	  do {
    	    k++;
    	    p *= Math.random();
    	  } while (p > L);

    	  return k - 1;
    	}
    
    public static int getBinomial(int n, double p) {
		  int x = 0;
		  for(int i = 0; i < n; i++) {
		    if(Math.random() < p)
		      x++;
		  }
		  return x;
		}
}
