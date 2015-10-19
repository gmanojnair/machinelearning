/* 
 * Copyright (C) 2014 Vasilis Vryniotis <bbriniotis at datumbox.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.naivebayes.model;

import java.util.HashMap;
import java.util.Map;


/**
 * The NaiveBayesKnowledgeBase Object stores all the fields that the classifier
 * learns during training.
 * 
 */
public class NaiveBayesInformation {
    /**
     * number of training observations
     */
    public int number_of_training_observations=0;
    
    /**
     * number of categories
     */
    public int number_of_categories=0;
    
    /**
     * number of features
     */
    public int number_of_features=0;
    
    /**
     * log priors for log( P(c) )
     */
    public Map<String, Double> logPriors_for_categories = new HashMap<>();
    
    /**
     * log likelihood for log( P(x|c) ) 
     */
    public Map<String, Map<String, Double>> logLikelihoods_for_feature_given_categories = new HashMap<>();
}
