package com.naivebayes.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "attributedAnomalies")
@NamedQueries({
		@NamedQuery(name = "AttributedAnomalies.findAllBySubStatus", query = "SELECT t FROM AttributedAnomalies t where substatus is not null and trim(substatus) != ''"),
		
	//	@NamedQuery(name = "AttributedAnomalies.findLastMonthRecords", query = "select t from AttributedAnomalies t where substatus is not null and trim(substatus) != '' and submissionDate >= DATE_SUB(:submissionDate, INTERVAL 24 MONTH)"),

		
		@NamedQuery(name = "AttributedAnomalies.findMaxIds", query = "select t from AttributedAnomalies t order by anomalyId desc ")

		
})
public class AttributedAnomalies {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Anomalyid")
	private long anomalyId;

	public long getAnomalyId() {
		return anomalyId;
	}

	public void setAnomalyId(long anomalyId) {
		this.anomalyId = anomalyId;
	}

	@Column(name = "substatus")
	private String substatus;

	public String getSubstatus() {
		return substatus;
	}

	public void setSubstatus(String substatus) {
		this.substatus = substatus;
	}

	@Column(name = "ClientId")
	private String clientId;

	@Column(name = "AnomalyCategory")
	private String anomalyCategory;

	@Column(name = "SubmissionDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  submissionDate;

	public Date  getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date  submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAnomalyCategory() {
		return anomalyCategory;
	}

	public void setAnomalyCategory(String anomalyCategory) {
		this.anomalyCategory = anomalyCategory;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCenterPointDataSet() {
		return centerPointDataSet;
	}

	public void setCenterPointDataSet(String centerPointDataSet) {
		this.centerPointDataSet = centerPointDataSet;
	}

	public String getCenterPointCountry() {
		return centerPointCountry;
	}

	public void setCenterPointCountry(String centerPointCountry) {
		this.centerPointCountry = centerPointCountry;
	}

	public String getCenterPointAdminAreaOrder8() {
		return centerPointAdminAreaOrder8;
	}

	public void setCenterPointAdminAreaOrder8(String centerPointAdminAreaOrder8) {
		this.centerPointAdminAreaOrder8 = centerPointAdminAreaOrder8;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGeoAugmentationDataVersion() {
		return geoAugmentationDataVersion;
	}

	public void setGeoAugmentationDataVersion(String geoAugmentationDataVersion) {
		this.geoAugmentationDataVersion = geoAugmentationDataVersion;
	}

	public String getProblemDataVersion() {
		return problemDataVersion;
	}

	public void setProblemDataVersion(String problemDataVersion) {
		this.problemDataVersion = problemDataVersion;
	}

	public String getCenterPointAdminAreaOrder7() {
		return centerPointAdminAreaOrder7;
	}

	public void setCenterPointAdminAreaOrder7(String centerPointAdminAreaOrder7) {
		this.centerPointAdminAreaOrder7 = centerPointAdminAreaOrder7;
	}

	public String getConsideredByCaseJourneyman() {
		return consideredByCaseJourneyman;
	}

	public void setConsideredByCaseJourneyman(String consideredByCaseJourneyman) {
		this.consideredByCaseJourneyman = consideredByCaseJourneyman;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toCommaSeperatedString()
	{
		return "anomalyId=" + anomalyId + ", clientId=" + clientId + ", anomalyCategory="
		+ anomalyCategory + ", type=" + type + ", centerPointDataSet="
		+ centerPointDataSet + ", centerPointCountry="
		+ centerPointCountry + ", centerPointAdminAreaOrder8="
		+ centerPointAdminAreaOrder8 + ", application=" + application
		+ ", emailAddress=" + emailAddress + ", userid=" + userid
		+ ", geoAugmentationDataVersion=" + geoAugmentationDataVersion
		+ ", problemDataVersion=" + problemDataVersion
		+ ", centerPointAdminAreaOrder7=" + centerPointAdminAreaOrder7
		+ ", consideredByCaseJourneyman=" + consideredByCaseJourneyman
		+ ", status=" + status ;
	}
	
	

	@Override
	public String toString() {
		return "AttributedAnomalies [anomalyId=" + anomalyId + ", substatus="
				+ substatus + ", clientId=" + clientId + ", anomalyCategory="
				+ anomalyCategory + ", submissionDate=" + submissionDate
				+ ", type=" + type + ", centerPointDataSet="
				+ centerPointDataSet + ", centerPointCountry="
				+ centerPointCountry + ", centerPointAdminAreaOrder8="
				+ centerPointAdminAreaOrder8 + ", application=" + application
				+ ", emailAddress=" + emailAddress + ", userid=" + userid
				+ ", geoAugmentationDataVersion=" + geoAugmentationDataVersion
				+ ", problemDataVersion=" + problemDataVersion
				+ ", centerPointAdminAreaOrder7=" + centerPointAdminAreaOrder7
				+ ", consideredByCaseJourneyman=" + consideredByCaseJourneyman
				+ ", status=" + status + "]";
	}

	@Column(name = "type")
	private String type;

	@Column(name = "CenterPointDataSet")
	private String centerPointDataSet;

	@Column(name = "CenterPointCountry")
	private String centerPointCountry;

	@Column(name = "CenterPointAdminAreaOrder8")
	private String centerPointAdminAreaOrder8;

	@Column(name = "Application")
	private String application;
	@Column(name = "EmailAddress")
	private String emailAddress;
	@Column(name = "userid")
	private String userid;
	@Column(name = "GeoAugmentationDataVersion")
	private String geoAugmentationDataVersion;
	@Column(name = "ProblemDataVersion")
	private String problemDataVersion;
	@Column(name = "CenterPointAdminAreaOrder7")
	private String centerPointAdminAreaOrder7;

	@Column(name = "ConsideredByCaseJourneyman")
	private String consideredByCaseJourneyman;

	@Column(name = "status")
	private String status;

}
