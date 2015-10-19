package com.naivebayes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.naivebayes.cache.NaiveBayesCache;
import com.naivebayes.dao.NaiveBayesDao;
import com.naivebayes.model.AttributedAnomalies;
import com.naivebayes.model.NaiveBayes;
import com.naivebayes.model.NaiveBayesInformation;

@Service
@Validated

public class NaiveBayesService {

	@Autowired
	public NaiveBayesDao naivedao;
	
	@Autowired
	public NaiveBayesCache knowledgebasedcache;
	

	public NaiveBayes trainData(Long id) {
		// train classifier
		NaiveBayes nb = new NaiveBayes();
		nb.setChisquareCriticalValue(6.63); // 0.01 pvalue ,6.63)
		List<AttributedAnomalies> testData = naivedao
				.getByIdsForAllSixMonth(id);
		Map<String, String[]> trainingExamples = fetchTrainingData(testData);
	
		
		nb.train(trainingExamples);
		try {
			knowledgebasedcache.getCache("NaiveBayes").put(
					"NaiveBayesInformation", nb.getKnowledge());
			//getNaiveBayesInformation(nb);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getNaiveBayes();
	}

	public long getLatestAnomalyId() {
		return naivedao.getMaxId();
	}

	@CachePut(value="NaiveBayes" , key = "NaiveBayesInformation")
	public NaiveBayesInformation getNaiveBayesInformation(NaiveBayes nb) {
		System.out.println("Cached" + nb.getKnowledge());
		return nb.getKnowledge();
	}
	
	@Value("#{cacheManager.getCache('NaiveBayes')}")
	private Cache myCache;

	public NaiveBayes getNaiveBayes() {

		NaiveBayes nb = new NaiveBayes();
		ValueWrapper knowledge = null;
		try {
			
			Cache cache = (Cache) knowledgebasedcache.getCache("NaiveBayes");
			knowledge = cache.get("NaiveBayesInformation");
			System.out.println("Cache object " + knowledge);
			
			//System.out.println("Cache object from annotations " + myCache.get("NaiveBayesInformation"));
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			System.out.println("Knowledge Not Found");
		}
		if (knowledge != null) {
			System.out.println("Knowledge Found");
			nb.setKnowledge((NaiveBayesInformation) knowledge.get());
		}

		return nb;
	}

	private Map<String, String[]> fetchTrainingData(
			List<AttributedAnomalies> data) {

		Map<String, String[]> trainingExamples = new HashMap<>();
		final String ACCEPTED = "ACCEPTED";
		final String REJECTED = "REJECTED";
		List<Object> accepted_item = new ArrayList<>();
		List<Object> rejected_item = new ArrayList<>();
		// TODO Fetch last 3 months data
		for (AttributedAnomalies record : data) {
			if (record.getSubstatus().trim()
					.equalsIgnoreCase("DatabaseUpdated"))
				accepted_item.add(record.toCommaSeperatedString());

			else if (record.getSubstatus().trim()
					.equalsIgnoreCase("DatabaseAlternativelyUpdated"))
				accepted_item.add(record.toCommaSeperatedString());

			else if (record.getSubstatus().trim()
					.equalsIgnoreCase("AlreadyFixed"))

				accepted_item.add(record.toCommaSeperatedString());

			else

				rejected_item.add(record.toCommaSeperatedString());
		}

		trainingExamples.put(ACCEPTED, (String[]) accepted_item
				.toArray(new String[accepted_item.size()]));
		trainingExamples.put(REJECTED, (String[]) rejected_item
				.toArray(new String[rejected_item.size()]));

		return trainingExamples;
	}

	public String predictData(String id, NaiveBayes nb) {

		String details = this.findById(new Long(id)).toCommaSeperatedString();

		String answer = nb.predict(details);

		return answer;
	}

	public String predictData(String id) {

		String details = this.findById(new Long(id)).toCommaSeperatedString();

		NaiveBayes nb = getNaiveBayes();

		if (nb != null)
			return getNaiveBayes().predict(details);
		return "NONE";
	}

	public long getByIdsForAllSixMonth(Long id) {
		List<?> listOfUserConfigurations = naivedao.getByIdsForAllSixMonth(id);

		return listOfUserConfigurations.size();

	}

	public AttributedAnomalies findById(Long id) {
		System.out.println("Value" + id);
		AttributedAnomalies answer = naivedao.getById(id);
		return answer;
	}

}
