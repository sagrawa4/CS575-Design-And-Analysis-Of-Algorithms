import java.io.*;
import java.util.*;
import java.lang.*;
//import java.util.Random;

class ChromosomeFitness
{
    public ChromosomeFitness(String chromosome, int fitness)
    {
        this.chromosome = chromosome;
        this.fitness = fitness;
    }

    public boolean smallerThan(ChromosomeFitness obj)
    {
        //System.out.printf("fitness of old chromosome %d\n",this.fitness);
        //System.out.printf("fitness of new chromosome %d\n",obj.fitness);
        if( this.fitness <= obj.fitness) 
        {
            return true;
        }
        
        return false;
    }

    public String chromosome;
    public int fitness;
}

public class gp
{   
    public static int getRandomInteger(int maximum, int minimum)
    { 
        return ((int) (Math.random()*(maximum - minimum))) + minimum; 
    }

    public static ChromosomeFitness newGeneration(ChromosomeFitness object, int[][] distance_matrix, int V)
    {
        ChromosomeFitness output = object;
        // We try for 100 iterations to find the new optimized chromosome
        // if we cannot find it we will just return input object
        // we have not made things better but also not worse.
        for(int i = 0 ; i < 100; ++i)
        {
            String new_mutated_chromosome = mutatedGeneration(object.chromosome);
            int new_fitness_chromosome = calculateFitness(distance_matrix,V,new_mutated_chromosome);
            ChromosomeFitness new_chromosome_fitness = new ChromosomeFitness(new_mutated_chromosome, new_fitness_chromosome);
            boolean comparision_result = object.smallerThan(new_chromosome_fitness);
            // if old < new: return true
            // if old > new return false
            if(comparision_result == false)
            {
                output = new_chromosome_fitness;
                break;
            }
        }
        return output;
    }


    public static String mutatedGeneration(String chromosome)
    {
        String mutated_generation = chromosome;

        while (true)
        {            
            int r = getRandomInteger(chromosome.length()-1,1); 
            //System.out.printf("value of r %d\n",r);
            
            int r1 = getRandomInteger(chromosome.length()-1,1); 
            //System.out.printf("value of r1 %d\n",r1);
            if (r1 != r) 
            { 
                char ch[] = chromosome.toCharArray(); 
                char temp = ch[r];
                ch[r]=ch[r1];
                ch[r1]=temp;                    
                mutated_generation = String.valueOf(ch);
                //System.out.printf("mutated generation for chromosome 01230 is %s\n", mutated_generation);
                break;
            } 
        } 

        return mutated_generation; 
    }

    public String createChromosome(int V)
    {
        String chromosome = "0";
        while(true)
        {
            if(chromosome.length() == V)// Once the string created is equal to V
            {
                char ch[] = chromosome.toCharArray();//creates a string
                chromosome += ch[0];//once last element of string is 0,it breaks
                //System.out.printf(" CHROMOSOME CREATED %s\n",chromosome);
                break;
            }
            int temp = getRandomInteger(V,1); //generates a random number b/w 1 and 4
            if(!chromosome.contains(String.valueOf((char)(temp + 48))))//if the number is not in string, keeps adding
            {
                chromosome +=(char)(temp + 48);
            }
        }
        return chromosome;
    }

    public void printDistanceMatrix(int[][] distance_matrix)
    {
        for (int i=0;i<distance_matrix.length;i++)
        {
            for(int j=0;j<distance_matrix[i].length;j++)
            {
                System.out.print(distance_matrix[i][j]+ "");
            }
            System.out.println();
        }
    }   

    public static int calculateFitness(int[][] distance_matrix,int V, String chromosome)
    {
            
        int total_fitness=0;
        
        //printDistanceMatrix(distance_matrix);
        
        for(int i=0;i< (chromosome.length()-1);i++)
        {
            int source = chromosome.charAt(i)-48;
            int destination = chromosome.charAt(i+1) -48;
            int distance = distance_matrix[source][destination];
            total_fitness = total_fitness + distance;
            //System.out.printf("Fitness value Source(%d) Destination(%d) = %d\n", source, destination, distance);
        }
        //System.out.printf("Total Fitness %d\n", total_fitness);
        
        return total_fitness;
    }

    void printGeneration(ChromosomeFitness[] array)
    {   
        for(int i = 0 ; i < array.length; ++i)
        {
            System.out.printf("[%s]\t(%d)\n", array[i].chromosome, array[i].fitness);
        }
    }

    //Function for TSP
    public void TSP(int[][] distance_matrix, int V, int gen_max, int total_population)
    {
        // Populate initial values
        ChromosomeFitness[] population_array = new ChromosomeFitness[total_population];
        for(int i=0;i< total_population;i++)
        {
            String new_chromosome = createChromosome(V);            
            int new_fitness_value = calculateFitness(distance_matrix,V,new_chromosome);
            ChromosomeFitness new_chromosome_fitness = new ChromosomeFitness(new_chromosome, new_fitness_value);
            population_array[i] = new_chromosome_fitness;
        }

        for(int c_gen = 0; c_gen < gen_max; ++c_gen)
        {
            System.out.printf("Generation Number : %d\n", c_gen +1);
            printGeneration(population_array);
            for(int i=0;i< total_population;i++)
            { 
                ChromosomeFitness new_c = newGeneration(population_array[i] , distance_matrix, V);
                population_array[i] = new_c;
            }
        }

        // Optimized Paths with Cost:
    }
    
    public static void main(String[] args) throws Exception
    {        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("\n\tEnter number of cities: ");
		int V = Integer.parseInt(br.readLine()); 
        
        System.out.println(V);
		System.out.println();
        
        if(V < 0)
        {
            throw new IllegalArgumentException("\n\tNumber of cities cannot be negative!!");
        }
		    
		if(V <= 2 && V>=0)
        {
            throw new IllegalArgumentException("\n\tTSP with 2 or lesser cities is trivial!!");
        }
			   
		if(V >= 15)
        {
            throw new IllegalArgumentException("\n\tComputation is explosive for N >= 15");
        }

        int [][] distance_matrix = new int[V][V];

        System.out.println("\nEnter the distances between the cities: \n");
		for(int i=0;i<V;i++)
		{
		    for(int j=0;j<V;j++)
		    {
		        if(i == j)
		        {
		            distance_matrix[i][j] = 0;
                }
                else
                {
                    if(i>j)
                    {
                        continue;
                    }   
                    else
                    {
                        System.out.print("\tA["+i+"]"+"["+j+"]: ");
                        distance_matrix[i][j] = distance_matrix[j][i] = Integer.parseInt(br.readLine());
                    }
                }
            }
            System.out.println();
        }
        
        System.out.println("\tThe distance matrix is: \n\n");
        for(int i=0;i<V;i++)
        {
            for(int j=0;j<V;j++)
            {
                System.out.print("\t"+distance_matrix[i][j]);
            }
            System.out.println();
        }

        int gen_max = 3;
        int total_population = 10;
        
        gp tspobj = new gp();
        //tspobj.createChromosome(V);
        //tspobj.calculateFitness(distance_matrix,V,"01230");
        tspobj.TSP(distance_matrix, V, gen_max, total_population);
        //tspobj.mutatedGeneration("01230");
    }   
}

