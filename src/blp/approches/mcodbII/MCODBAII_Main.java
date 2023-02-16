package blp.approches.mcodbII;

import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.operators.crossover.UniformCrossover;
import blp.operators.mutation.UniformMutation;
import blp.operators.selection.BinaryTournament;
import blp.problems.VRP;
import blp.problems.parameters.Parameters;
import java.util.HashMap;

/**
 *
 * @author Abir
 */
public class MCODBAII_Main {

    public static void main(String[] args) {
        Problem   problem;                  // The problem to solve
        MCODBAII  algorithm;                // The algorithm to use
        Operator  uSelection;               // UpperLevel Selection operator
        Operator  uCrossover;               // UpperLevel Crossover operator
        Operator  uMutation;                // UpperLevel Mutation operator
        Operator  uNeighborhood;            // UpperLevel Neighborhood operator
        Operator  uCoEvolutionCrossover;    // UpperLevel Co-EvolutionCrossover operator
        Operator  lSelection;               // LowerLevel Decomposition operator
        Operator  lCrossover;               // LowerLevel On-wall operator
        Operator  lMutation;                // LowerLevel Synthesis operator
        Operator  lNeighborhood;            // LowerLevel Neighborhood operator
        Operator  lCoEvolutionCrossover;    // LowerLevel Co-EvolutionCrossover operator
        HashMap<String, Object> parameters; 
        HashMap<String, Operator> operators;
        
        // Problem initialization
        Parameters.lowerBoundProductionCost = 1.0;
        Parameters.upperBoundProductionCost = 4.0;
        Parameters.lowerBoundDeleveryCost = 0.36;
        Parameters.upperBoundDeleveryCost = 5.36;
        Parameters.plantProductionsAvailability = 2000;
        problem = new VRP("p01", "ArrayInt", "Int");
        
        System.out.println("Problem has initialized successfully");
        parameters = new HashMap<>();
        parameters.put("populationSize", 100);
        parameters.put("lPopulationSize", 100);
        parameters.put("maxFE", 100);
        parameters.put("lMaxFE", 100);
        parameters.put("maxNoImprovementUpperLevel", 5);
        parameters.put("lMaxNoImprovementLowerLevel", 5);
        parameters.put("numberOfGenerationSubPopulation", 10);
        parameters.put("lNumberOfGenerationSubPopulation", 10);
        parameters.put("p", 3);
        parameters.put("lP", 3);
        
        HashMap<String, Object> uCrossoverParameters = new HashMap<>();
        uCrossoverParameters.put("level", "upper");
        HashMap<String, Object> uMutationParameters = new HashMap<>();
        uMutationParameters.put("mutationProbability", 0.1);
        uMutationParameters.put("level", "upper");
        HashMap<String, Object> uNeighborhoodParameters = new HashMap<>();
        uNeighborhoodParameters.put("mutationProbability", 0.1);
        uNeighborhoodParameters.put("level", "upper");
        HashMap<String, Object> uCoEvolutionCrossoverParameters = new HashMap<>();
        uCoEvolutionCrossoverParameters.put("mutationProbability", 0.1);
        uCoEvolutionCrossoverParameters.put("level", "upper");
        
        HashMap<String, Object> lCrossoverParameters = new HashMap<>();
        lCrossoverParameters.put("level", "lower");
        HashMap<String, Object> lMutationParameters = new HashMap<>();
        lMutationParameters.put("mutationProbability", 0.1);
        lMutationParameters.put("level", "lower");
        HashMap<String, Object> lNeighborhoodParameters = new HashMap<>();
        lNeighborhoodParameters.put("mutationProbability", 0.1);
        lNeighborhoodParameters.put("level", "lower");
        HashMap<String, Object> lCoEvolutionCrossoverParameters = new HashMap<>();
        lCoEvolutionCrossoverParameters.put("mutationProbability", 0.1);
        lCoEvolutionCrossoverParameters.put("level", "lower");
        
        uSelection = new BinaryTournament(new HashMap<>());
        uCrossover = new UniformCrossover(uCrossoverParameters);
        uMutation  = new UniformMutation(uMutationParameters);
        uNeighborhood = new UniformMutation(uNeighborhoodParameters);
        uCoEvolutionCrossover = new UniformMutation(uCoEvolutionCrossoverParameters);
        
        lSelection = new BinaryTournament(new HashMap<>());
        lCrossover = new UniformCrossover(lCrossoverParameters);
        lMutation  = new UniformMutation(lMutationParameters);
        lNeighborhood = new UniformMutation(lNeighborhoodParameters);
        lCoEvolutionCrossover = new UniformMutation(lCoEvolutionCrossoverParameters);
        
        operators = new HashMap<>();
        
        operators.put("selection", uSelection);
        operators.put("crossover", uCrossover);
        operators.put("mutation", uMutation);
        operators.put("neighborhood", uNeighborhood);
        operators.put("coEvolutionCrossover", uCoEvolutionCrossover);
        
        operators.put("lSelection", lSelection);
        operators.put("lCrossover", lCrossover);
        operators.put("lMutation", lMutation);
        operators.put("lNeighborhood", lNeighborhood);
        operators.put("lCoEvolutionCrossover", lCoEvolutionCrossover);
      
        algorithm = new MCODBAII(problem);
        algorithm.setInputParameter(parameters);
        algorithm.setOperator(operators);
        
        System.out.println("Running MCODBA-II...");
        Solution result = algorithm.execute().get(0);
        System.out.println("Upper Soltion ==>");
        System.out.println(result.toString());
        System.out.println("Lower Soltion ==>");
        System.out.println(algorithm.getLowerSolution().toString());
        System.out.println("Direct Rationality ==> "+algorithm.getDirectRationality());
        System.out.println("Weighted Rationality ==> "+algorithm.getWeightedRationality());
        System.out.println("Upper FE ==> "+algorithm.getUpperFE());
        System.out.println("Lower FE ==> "+algorithm.getLowerFE());
        System.out.println("End MCODBA-II");
    }
    
}
