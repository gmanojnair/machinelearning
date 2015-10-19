package com.naivebayes.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.naivebayes.model.AttributedAnomalies;
@Transactional
@Repository
public class NaiveBayesDao {

	/**
	 * Return all the users stored in the database.
	 */
	@SuppressWarnings("unchecked")
	public List<AttributedAnomalies> getAll() {
		return entityManager.createQuery("from AttributedAnomalies")
				.getResultList();
	}

	
	/**
	 * Return the user having the passed id.
	 */
	public AttributedAnomalies getById(long id) {
		return entityManager.find(AttributedAnomalies.class, id);
	}

	/**
	 * Return the user having the passed id.
	 */
	@SuppressWarnings("unchecked")
	public List<AttributedAnomalies> getByIdsForAllSixMonth(long id) {

		List<AttributedAnomalies> listOfUserConfigurations = null;

		try {

			AttributedAnomalies record = entityManager.find(AttributedAnomalies.class, id);
			String qlString = "SELECT anomalyid,SubmissionDate,status,substatus,ClientId,AnomalyCategory,type,CenterPointDataSet,CenterPointCountry,CenterPointAdminAreaOrder8,Application,EmailAddress,userid,GeoAugmentationDataVersion,ProblemDataVersion,CenterPointAdminAreaOrder7,ConsideredByCaseJourneyman FROM AttributedAnomalies t where t.substatus is not null and trim(t.substatus) != '' and status='Closed' and t.submissionDate >= DATE_SUB(:submissionDate, INTERVAL 6 MONTH)";
			listOfUserConfigurations = (List<AttributedAnomalies>) entityManager
					.createNativeQuery(qlString, AttributedAnomalies.class)
					.setParameter("submissionDate",record.getSubmissionDate()).getResultList();
			System.out.println("Total Record Size for training data " + listOfUserConfigurations.size());

		} catch (final Exception excp) {
			
			System.out.println("Error " + excp.getMessage());
		}

		return listOfUserConfigurations;
	}
	
	/**
	 * Return the user having the passed id.
	 */
	@SuppressWarnings("unchecked")
	public long  getMaxId() {

		List<AttributedAnomalies> anomaly= null;

		try {

		/*	String qlString = "select anomalyid,SubmissionDate,status,substatus,ClientId,AnomalyCategory,type,CenterPointDataSet,CenterPointCountry,CenterPointAdminAreaOrder8,Application,EmailAddress,userid,GeoAugmentationDataVersion,ProblemDataVersion,CenterPointAdminAreaOrder7,ConsideredByCaseJourneyman from AttributedAnomalies t order by t.anomalyid desc";
			anomaly = (List<AttributedAnomalies>) entityManager
					.createNativeQuery(qlString, AttributedAnomalies.class).setMaxResults(1).getResultList();
		*/	
			anomaly =  (List<AttributedAnomalies>) entityManager.createNamedQuery("AttributedAnomalies.findMaxIds", AttributedAnomalies.class).setMaxResults(1).getResultList();
			System.out.println("Max Id : " + anomaly.get(0).getAnomalyId());
		

		} catch (final Exception excp) {
			
			System.out.println("Error " + excp.getMessage());
		}

		return  anomaly.get(0).getAnomalyId();
	}


	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	// An EntityManager will be automatically injected from entityManagerFactory
	// setup on DatabaseConfig class.
	@PersistenceContext
	private EntityManager entityManager;

}
