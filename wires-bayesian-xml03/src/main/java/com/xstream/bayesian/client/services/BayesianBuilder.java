package com.xstream.bayesian.client.services;

import java.util.List;

import com.google.common.collect.Lists;
import com.xstream.bayesian.client.model.BayesNetwork;
import com.xstream.bayesian.client.model.BayesVariable;
import com.xstream.bayesian.client.parser.model.Bif;
import com.xstream.bayesian.client.parser.model.Definition;
import com.xstream.bayesian.client.parser.model.Network;
import com.xstream.bayesian.client.parser.model.Variable;

public class BayesianBuilder {

    public BayesNetwork build(Bif bif) {
        BayesNetwork bayesNetwork = new BayesNetwork(bif.getNetwork().getName());
        int id = 1;
        for (Definition def : bif.getNetwork().getDefinitions()) {
            BayesVariable nodo = buildVariable(def, bif.getNetwork(), id);
            bayesNetwork.getNodos().add(nodo);
            id += 1;
        }
        this.setIncomingNodes(bayesNetwork);
        
        return bayesNetwork;
    }

    private BayesVariable buildVariable(Definition def, Network network, int id) {
        List<String> outcomes = Lists.newArrayList();
        double[][] position = new double[2][2];
        this.getOutcomesByVariable(network, def.getName(), outcomes, position);
        return new BayesVariable(def.getName(), id, outcomes, this.getProbabilities(def.getProbabilities(), outcomes), def.getGiven(),
                position);
    }

    private void getOutcomesByVariable(Network network, String nameDefinition, List<String> outcomes, double[][] position) {
        for (Variable var : network.getVariables()) {
            if (var.getName().equals(nameDefinition)) {
                for (String outcome : var.getOutComes()) {
                    outcomes.add(outcome);
                }
                // get position
                position = getPosition(var.getPosition(), position);
            }
        }
    }

    private double[][] getProbabilities(String table, List<String> outcomes) {
      double probabilities[][] = new double[table.split(" ").length][table.split(" ").length];
      String[] values = table.split(" ");
      int k = 0;
      for(int i = 0; i < values.length / outcomes.size() ; i++){
    	  for(int j = 0; j < outcomes.size(); j++){
    		  probabilities[i][j] = Double.valueOf(values[k]); 
    		  k += 1;
    	  }
      }
      return probabilities;
    }

    private double[][] getPosition(String stringPosition, double[][] position) {
        if (stringPosition != null) {
            stringPosition = this.clearStringPosticion(stringPosition);
            int i = 0;
            int j = 0;
            for (String pos : stringPosition.split(",")) {
                position[i][j] = Double.parseDouble(pos);
                if (i < j) {
                    i += 1;
                }
                j += 1;
            }
        }
        return null;
    }
    
    private String clearStringPosticion(String stringPosition){
    	stringPosition = stringPosition.replace("position", "");
        stringPosition = stringPosition.replace("=", "");
        stringPosition = stringPosition.replace("(", "");
        stringPosition = stringPosition.replace(")", "");
        stringPosition = stringPosition.trim();
        return stringPosition;
    }
    
    private void setIncomingNodes(BayesNetwork bayesNetwork){
        for(BayesVariable node : bayesNetwork.getNodos()){
            if(node.getGiven()!=null && !node.getGiven().isEmpty()){
                node.setIncomingNodes(this.getNodesByGiven(node.getGiven(), bayesNetwork.getNodos()));
            }
        }
    }
    
    private List<BayesVariable> getNodesByGiven(List<String> given, List<BayesVariable> nodes){
        List<BayesVariable> listIncoming = Lists.newArrayList();
        for(String giv : given){
            for(BayesVariable node : nodes){
                if(node.getName().equals(giv)){
                    listIncoming.add(node);
                    break;
                }
            }
        }
        return listIncoming;
    }
}
