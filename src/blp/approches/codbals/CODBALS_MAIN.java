package blp.approches.codbals;

import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.operators.crossover.UniformCrossover;
import blp.operators.ls.OneOpt;
import blp.operators.ls.TwoMove;
import blp.operators.ls.eXchange;
import blp.operators.mutation.UniformMutation;
import blp.operators.selection.BinaryTournament;
import blp.problems.VRP;
import blp.problems.parameters.Parameters;
import java.util.HashMap;

public class CODBALS_MAIN {

    public static void main(String[] args) {
        Problem problem;                   // The problem to solve
        CODBALS algorithm;                 // The algorithm to use
        Operator uSelection;               // UpperLevel Selection operator
        Operator uCrossover;               // UpperLevel Crossover operator
        Operator uMutation;                // UpperLevel Mutation operator
        Operator lOneOpt;                  // LowerLevel OneOpt operator
        Operator lTwoMove;                 // LowerLevel Two Move operator
        Operator leXchange;                // LowerLevel eXchange operator
        Operator lCoEvolutionCrossover;    // LowerLevel Co-EvolutionCrossover operator
        HashMap<String, Object> parameters;
        HashMap<String, Operator> operators;

        // Problem initialization
        Parameters.lowerBoundProductionCost = 1.0;
        Parameters.upperBoundProductionCost = 4.0;
        Parameters.lowerBoundDeleveryCost = 0.36;
        Parameters.upperBoundDeleveryCost = 5.36;
        Parameters.plantProductionsAvailability = 2000;
        VRP vrp = new VRP("pr01", "ArrayInt", "Int");
        problem = vrp;

        vrp.getNumberOfDepots();

        System.out.println("Problem has initialized successfully");
        parameters = new HashMap<>();
        parameters.put("populationSize", 100);
        parameters.put("maxFE", 100);
        parameters.put("lMaxFE", 100);
        //parameters.put("maxNoImprovementUpperLevel", 5);
        //parameters.put("lMaxNoImprovementLowerLevel", 5);
        parameters.put("lP", 2);

        HashMap<String, Object> uCrossoverParameters = new HashMap<>();
        uCrossoverParameters.put("level", "upper");
        HashMap<String, Object> uMutationParameters = new HashMap<>();
        uMutationParameters.put("mutationProbability", 0.1);
        uMutationParameters.put("level", "upper");

        uSelection = new BinaryTournament(new HashMap<>());
        uCrossover = new UniformCrossover(uCrossoverParameters);
        uMutation = new UniformMutation(uMutationParameters);

        String[] lOperatorName = {"oneopt", "twomove", "exchange"};
        parameters.put("neighborhoods", lOperatorName);

        lOneOpt = new OneOpt(new HashMap<>());
        HashMap<String, Object> lTwoMoveParameters = new HashMap<>();
        lTwoMoveParameters.put("numberOfDepots", vrp.getNumberOfDepots());
        lTwoMoveParameters.put("numberOfPlants", vrp.getNumberOfPlants());
        lTwoMove = new TwoMove(lTwoMoveParameters);
        HashMap<String, Object> leXchangeParameters = new HashMap<>();
        leXchangeParameters.put("numberOfDepots", vrp.getNumberOfDepots());
        leXchangeParameters.put("numberOfPlants", vrp.getNumberOfPlants());
        leXchange = new eXchange(leXchangeParameters);

        HashMap<String, Object> lUniformCrossoverParameters = new HashMap<>();
        lUniformCrossoverParameters.put("level", "lower");
        lCoEvolutionCrossover = new UniformCrossover(lUniformCrossoverParameters);

        operators = new HashMap<>();

        operators.put("selection", uSelection);
        operators.put("crossover", uCrossover);
        operators.put("mutation", uMutation);

        operators.put("oneopt", lOneOpt);
        operators.put("twomove", lTwoMove);
        operators.put("exchange", leXchange);
//        operators.put("lNeighborhood", lNeighborhood);
        operators.put("lCoEvolutionCrossover", lCoEvolutionCrossover);

        algorithm = new CODBALS(problem);
        algorithm.setInputParameter(parameters);
        algorithm.setOperator(operators);

        System.out.println("Running CODBALS...");
        Solution result = algorithm.execute().get(0);
        System.out.println("Upper Soltion ==>");
        System.out.println(result.toString());
        System.out.println("Lower Soltion ==>");
        System.out.println(algorithm.getLowerSolution().toString());
        System.out.println("Direct Rationality ==> " + algorithm.getDirectRationality());
        System.out.println("Weighted Rationality ==> " + algorithm.getWeightedRationality());
        System.out.println("Upper FE ==> " + algorithm.getUpperFE());
        System.out.println("Lower FE ==> " + algorithm.getLowerFE());
        System.out.println("End CODBALS");
    }
}
