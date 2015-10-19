package com.naivebayes.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.naivebayes.common.util.TextTokenizer;

public class NaiveBayes {
	private double chisquareCriticalValue = 10.83; // equivalent to pvalue
	// 0.001. It is used by
	// feature selection
	// algorithm

	private NaiveBayesInformation knowledge;

	/**
	 * This constructor is used when we load an already train classifier
	 * 
	 * @param knowledgeBase
	 */
	public NaiveBayes(NaiveBayesInformation knowledgeBase) {
		this.knowledge = knowledgeBase;
	}

	/**
	 * This constructor is used when we plan to train a new classifier.
	 */
	public NaiveBayes() {
		this(null);
	}

	/**
	 * Gets the knowledgebase parameter
	 * 
	 * @return
	 */
	public NaiveBayesInformation getKnowledge() {
		return knowledge;
	}

	/**
	 * Gets the knowledgebase parameter
	 * 
	 * @return
	 */
	public void setKnowledge(NaiveBayesInformation knowledge) {
		this.knowledge = knowledge;
	}

	/**
	 * Gets the chisquareCriticalValue paramter.
	 * 
	 * @return
	 */
	public double getChisquareCriticalValue() {
		return chisquareCriticalValue;
	}

	/**
	 * Sets the chisquareCriticalValue parameter.
	 * 
	 * @param chisquareCriticalValue
	 */
	public void setChisquareCriticalValue(double chisquareCriticalValue) {
		this.chisquareCriticalValue = chisquareCriticalValue;
	}

	/**
	 * Preprocesses the original dataset and converts it to a List of Documents.
	 * 
	 * @param trainingDataset
	 * @return
	 */
	private List<Document> preprocessDataset(
			Map<String, String[]> trainingDataset) {

		// loop through all the categories and training examples

		int temp = 0;
		for (Map.Entry<String, String[]> entry : trainingDataset.entrySet()) {
			temp += temp + entry.getValue().length;
		}
		List<Document> dataset = new ArrayList<>(temp);

		// trainingDataset.forEach((k, v) -> addDoc1(k, v, dataset));

		// loop through all the categories and training examples

		Iterator<Map.Entry<String, String[]>> it = trainingDataset.entrySet()
				.iterator();
		TextTokenizer t = new TextTokenizer();

		while (it.hasNext()) {
			Map.Entry<String, String[]> entry = it.next();
			String category = entry.getKey().intern();
			String[] examples = entry.getValue();
			long length = examples.length;

			for (int i = 0; i < length; ++i) {
				// for each example in the category tokenize its text and //
				// convert it into a Document object.
				Document doc = t.tokenize(examples[i]);
				doc.category = category.intern();
				dataset.add(doc);

				examples[i] = null; // try freeing some memory doc = null; }

			}
			it.remove(); // try freeing some memory }

		}

		t = null;
		return dataset;
	}

	private void addDoc(final String category, final String[] examples,
			List<Document> dataset) {

		int length = examples.length;

		System.out.println("Total Record length " + length);

		for (int i = 0; i < length; ++i) {
			// for each example in the category tokenize its text and convert it
			// into a Document object.
				TextTokenizer t = new TextTokenizer();
			Document doc = t.tokenize(examples[i]);
			t = null;
			doc.category = category;
			dataset.add(doc);

			// examples[i] = null; // try freeing some memory
		}

		System.out.println("Added Doc");

	}

	/**
	 * Gathers the required counts for the features and performs feature
	 * selection on the above counts. It returns a FeatureStats object that is
	 * later used for calculating the probabilities of the model.
	 * 
	 * @param dataset
	 * @return
	 */
	private FeatureStats selectFeatures(List<Document> dataset) {
		FeatureExtraction featureExtractor = new FeatureExtraction();

		// the FeatureStats object contains statistics about all the features
		// found in the documents
		FeatureStats stats = featureExtractor.extractFeatureStats(dataset); // extract
		// the
		// stats
		// of
		// the
		// dataset

		// we pass this information to the feature selection algorithm and we
		// get a list with the selected features
		Map<String, Double> selectedFeatures = featureExtractor.chisquare(
				stats, chisquareCriticalValue);

		// clip from the stats all the features that are not selected
		Iterator<Map.Entry<String, Map<String, Integer>>> it = stats.featureCategoryJointCount
				.entrySet().iterator();
		while (it.hasNext()) {
			String feature = it.next().getKey();

			if (selectedFeatures.containsKey(feature) == false) {
				// if the feature is not in the selectedFeatures list remove it
				it.remove();
			}
		}

		return stats;
	}

	/**
	 * Trains a Naive Bayes classifier by using the Multinomial Model by passing
	 * the trainingDataset and the prior probabilities.
	 * 
	 * @param trainingDataset
	 * @param categoryPriors
	 * @throws IllegalArgumentException
	 */
	public void train(Map<String, String[]> trainingDataset,
			Map<String, Double> categoryPriors) throws IllegalArgumentException {
		// preprocess the given dataset
		List<Document> dataset = preprocessDataset(trainingDataset);

		// produce the feature stats and select the best features
		FeatureStats featureStats = selectFeatures(dataset);

		// intiliaze the knowledgeBase of the classifier
		knowledge = new NaiveBayesInformation();
		knowledge.number_of_training_observations = featureStats.n; // number of
		// observations
		knowledge.number_of_features = featureStats.featureCategoryJointCount
				.size(); // number of features

		// check is prior probabilities are given
		if (categoryPriors == null) {
			// if not estimate the priors from the sample
			knowledge.number_of_categories = featureStats.categoryCounts.size(); // number
			// of
			// cateogries
			knowledge.logPriors_for_categories = new HashMap<>();

			String category;
			int count;
			for (Map.Entry<String, Integer> entry : featureStats.categoryCounts
					.entrySet()) {
				category = entry.getKey();
				count = entry.getValue();
				// log ( P(c) ) where c = Accepted , Others => n(c) / no of
				// total observation
				knowledge.logPriors_for_categories.put(
						category,
						Math.log((double) count
								/ knowledge.number_of_training_observations));
			}
		} else {
			// if they are provided then use the given priors
			knowledge.number_of_categories = categoryPriors.size();

			// make sure that the given priors are valid
			if (knowledge.number_of_categories != featureStats.categoryCounts
					.size()) {
				throw new IllegalArgumentException(
						"Invalid priors Array: Make sure you pass a prior probability for every supported category.");
			}

			String category;
			Double priorProbability;
			for (Map.Entry<String, Double> entry : categoryPriors.entrySet()) {
				category = entry.getKey();
				priorProbability = entry.getValue();
				if (priorProbability == null) {
					throw new IllegalArgumentException(
							"Invalid priors Array: Make sure you pass a prior probability for every supported category.");
				} else if (priorProbability < 0 || priorProbability > 1) {
					throw new IllegalArgumentException(
							"Invalid priors Array: Prior probabilities should be between 0 and 1.");
				}

				knowledge.logPriors_for_categories.put(category,
						Math.log(priorProbability));
			}
		}
		// P(X1=1|y=0)= (1+ # of examples with y=0, X1=1 ) /(k+ # of examples
		// with y=0)
		// We are performing laplace smoothing (also known as add-1). This
		// requires to estimate the total feature occurrences in each category
		Map<String, Double> featureOccurrencesInCategory = new HashMap<>();

		Integer occurrences;
		Double featureOccSum;
		for (String category : knowledge.logPriors_for_categories.keySet()) {
			featureOccSum = 0.0;
			for (Map<String, Integer> categoryListOccurrences : featureStats.featureCategoryJointCount
					.values()) {
				occurrences = categoryListOccurrences.get(category);
				if (occurrences != null) {
					featureOccSum += occurrences;
				}
			}
			featureOccurrencesInCategory.put(category, featureOccSum);
		}

		// estimate log likelihoods
		String feature;
		Integer count;
		Map<String, Integer> featureCategoryCounts;
		double logLikelihood;
		for (String category : knowledge.logPriors_for_categories.keySet()) {
			for (Map.Entry<String, Map<String, Integer>> entry : featureStats.featureCategoryJointCount
					.entrySet()) {
				feature = entry.getKey();
				featureCategoryCounts = entry.getValue();

				count = featureCategoryCounts.get(category);
				if (count == null) {
					count = 0;
				}
				// estimate log likelihoods (P(E1|Bi))
				logLikelihood = Math
						.log((count + 1.0)
								/ (featureOccurrencesInCategory.get(category) + knowledge.number_of_features));
				if (knowledge.logLikelihoods_for_feature_given_categories
						.containsKey(feature) == false) {
					knowledge.logLikelihoods_for_feature_given_categories.put(
							feature, new HashMap<String, Double>());
				}
				knowledge.logLikelihoods_for_feature_given_categories.get(
						feature).put(category, logLikelihood);
			}
		}
		featureOccurrencesInCategory = null;
	}

	/**
	 * Wrapper method of train() which enables the estimation of the prior
	 * probabilities based on the sample.
	 * 
	 * @param trainingDataset
	 */
	public void train(Map<String, String[]> trainingDataset) {
		train(trainingDataset, null);
	}

	/**
	 * Predicts the category of a text by using an already trained classifier
	 * and returns its category.
	 * 
	 * @param text
	 * @return
	 * @throws IllegalArgumentException
	 */
	public String predict(String text) throws IllegalArgumentException {
		if (knowledge == null) {
			throw new IllegalArgumentException(
					"Knowledge Bases missing: Make sure you train first a classifier before you use it.");
		}

		TextTokenizer t = new TextTokenizer();
		// Tokenizes the text and creates a new document
		Document doc = t.tokenize(text);
		t = null;

		String category;
		String feature;
		Integer occurrences;
		Double logprob;

		String maxScoreCategory = null;
		Double maxScore = Double.NEGATIVE_INFINITY;

		// Map<String, Double> predictionScores = new HashMap<>();
		for (Map.Entry<String, Double> entry1 : knowledge.logPriors_for_categories
				.entrySet()) {
			category = entry1.getKey();
			logprob = entry1.getValue(); // intialize the scores with the priors

			// foreach feature of the document
			for (Map.Entry<String, Integer> entry2 : doc.tokens.entrySet()) {
				feature = entry2.getKey();

				if (!knowledge.logLikelihoods_for_feature_given_categories
						.containsKey(feature)) {
					continue; // if the feature does not exist in the knowledge
					// base skip it
				}

				occurrences = entry2.getValue(); // get its occurrences in text

				// log P(Bi|E) = log P(E1|Bi) + log P(E2|Bi) + ... + P(Eo|Bi) +
				logprob += occurrences
						* knowledge.logLikelihoods_for_feature_given_categories
								.get(feature).get(category); // multiply
			}
			// chooses the largest value of log P(Bi|E) among all category
			if (logprob > maxScore) {
				maxScore = logprob;
				maxScoreCategory = category;
			}
		}

		return maxScoreCategory; // return the category with heighest score
	}
}
