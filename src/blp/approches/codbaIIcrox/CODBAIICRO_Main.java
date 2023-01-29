package blp.approches.codbaIIcrox;

import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.operators.cro.Decomposition;
import blp.operators.cro.InterMolecular;
import blp.operators.cro.OnWall;
import blp.operators.cro.Synthesis;
import blp.problems.VRP;
import blp.problems.parameters.Parameters;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Abir
 */
public class CODBAIICRO_Main {
    
    public static void main(String args[]) {
        Problem   problem;                  // The problem to solve
        CODBAIICRO algorithm;               // The algorithm to use
        Operator  uDecomposition;           // UpperLevel Decomposition operator
        Operator  uOnWall;                  // UpperLevel On-wall operator
        Operator  uSynthesis;               // UpperLevel Synthesis operator
        Operator  uInterMolecular;          // UpperLevel Inter-Molecular operator
        Operator  uNeighborhood;            // UpperLevel Neighborhood operator
        Operator  ucoEvolutionCrossover;    // UpperLevel Co-EvolutionCrossover operator
        Operator  lDecomposition;           // LowerLevel Decomposition operator
        Operator  lOnWall;                  // LowerLevel On-wall operator
        Operator  lSynthesis;               // LowerLevel Synthesis operator
        Operator  lInterMolecular;          // LowerLevel Inter-Molecular operator
        Operator  lNeighborhood;            // LowerLevel Neighborhood operator
        Operator  lcoEvolutionCrossover;    // LowerLevel Co-EvolutionCrossover operator
        HashMap<String, Object> parameters; 
        HashMap<String, Operator> operators;
        
        // Problem initialization
        Parameters.lowerBoundProductionCost = 1.0;
        Parameters.upperBoundProductionCost = 4.0;
        Parameters.lowerBoundDeleveryCost = 0.36;
        Parameters.upperBoundDeleveryCost = 5.36;
        Parameters.plantProductionsAvailability = 2000;
        problem = new VRP("pr04", "ArrayInt", "Int");
        
        System.out.println("Problem has initialized successfully");
        parameters = new HashMap<>();
        parameters.put("populationSize", 100);
        parameters.put("lPopulationSize", 100);
        parameters.put("maxFE", 5000);
        parameters.put("lMaxFE", 5000);
        parameters.put("maxNoImprovementUpperLevel", 1);
        parameters.put("lMaxNoImprovementLowerLevel", 1);
        parameters.put("numberOfGenerationSubPopulation", 10);
        parameters.put("lNumberOfGenerationSubPopulation", 10);
        parameters.put("p", 1);
        parameters.put("lP", 1);
        parameters.put("collR", 0.7);
        parameters.put("lCollR", 0.7);
        parameters.put("decThres", 10);
        parameters.put("lDecThres", 10);
        parameters.put("synThres", 10.0);
        parameters.put("lSynThres", 10.0);
        parameters.put("enBuff", 0.0);
        parameters.put("lEnBuff", 0.0);
        parameters.put("lossR", 0.4);
        parameters.put("lLossR", 0.4);
        
        HashMap<String, Object> mutationParameters = new HashMap<>();
        mutationParameters.put("mutationProbability", 0.1);
        HashMap<String, Object> uOnWallParameters = new HashMap<>();
        uOnWallParameters.put("parentIndex", new ArrayList<>());
        uOnWallParameters.put("level", "upper");
        uOnWallParameters.put("iniKE", 10000.0);
        HashMap<String, Object> uDecParameters = new HashMap<>();
        uDecParameters.put("parentIndex", new ArrayList<>());
        uDecParameters.put("level", "upper");
        HashMap<String, Object> uInterParameters = new HashMap<>();
        uInterParameters.put("parentIndex", new ArrayList<>());
        uInterParameters.put("level", "upper");
        HashMap<String, Object> uSynthParameters = new HashMap<>();
        uSynthParameters.put("parentIndex", new ArrayList<>());
        uSynthParameters.put("level", "upper");        
        
        HashMap<String, Object> lOnWallParameters = new HashMap<>();
        lOnWallParameters.put("parentIndex", new ArrayList<>());
        lOnWallParameters.put("level", "lower");
        lOnWallParameters.put("iniKE", 10000.0);
        HashMap<String, Object> lDecParameters = new HashMap<>();
        lDecParameters.put("parentIndex", new ArrayList<>());
        lDecParameters.put("level", "lower");
        HashMap<String, Object> lInterParameters = new HashMap<>();
        lInterParameters.put("parentIndex", new ArrayList<>());
        lInterParameters.put("level", "lower");
        HashMap<String, Object> lSynthParameters = new HashMap<>();
        lSynthParameters.put("parentIndex", new ArrayList<>());
        lSynthParameters.put("level", "lower");
        
        uDecomposition = new Decomposition(uDecParameters);
        uOnWall = new OnWall(uOnWallParameters);
        uSynthesis = new Synthesis(uSynthParameters);
        uInterMolecular = new InterMolecular(uInterParameters);
        uNeighborhood = new OnWall(uOnWallParameters);
        ucoEvolutionCrossover = new Synthesis(uSynthParameters);
        
        lDecomposition = new Decomposition(lDecParameters);
        lOnWall = new OnWall(lOnWallParameters);
        lSynthesis = new Synthesis(lSynthParameters);
        lInterMolecular = new InterMolecular(lInterParameters);
        lNeighborhood = new OnWall(lOnWallParameters);
        lcoEvolutionCrossover = new Synthesis(lSynthParameters);
        
        operators = new HashMap<>();
        
        operators.put("decomposition", uDecomposition);
        operators.put("onWall", uOnWall);
        operators.put("synthesis", uSynthesis);
        operators.put("interMolecular", uInterMolecular);
        operators.put("neighborhood", uNeighborhood);
        operators.put("coEvolutionCrossover", ucoEvolutionCrossover);
        
        operators.put("lDecomposition", lDecomposition);
        operators.put("lOnWall", lOnWall);
        operators.put("lSynthesis", lSynthesis);
        operators.put("lInterMolecular", lInterMolecular);
        operators.put("lNeighborhood", lNeighborhood);
        operators.put("lCoEvolutionCrossover", lcoEvolutionCrossover);
      
        algorithm = new CODBAIICRO(problem);
        algorithm.setInputParameter(parameters);
        algorithm.setOperator(operators);
        
        System.out.println("Running CODBA-II/CRO...");
        Solution result = algorithm.execute().get(0);
        System.out.println("Upper Soltion ==>");
        System.out.println(result.toString());
        System.out.println("Lower Soltion ==>");
        System.out.println(algorithm.getLowerSolution().toString());
        System.out.println("Direct Rationality ==> "+algorithm.getDirectRationality());
        System.out.println("Weighted Rationality ==> "+algorithm.getWeightedRationality());
        System.out.println("Upper FE ==> "+algorithm.getUpperFE());
        System.out.println("Lower FE ==> "+algorithm.getLowerFE());
        System.out.println("End CODBA-II/CRO");
        //System.out.println("Best Solution : "+result.toString());
    }    
}
