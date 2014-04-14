package org.barisakkurt.istanbulandroid;

class Problem {
	private String problemId;
	private String latitude;
	private String longitude;
	private String reportDate;
	private String category;
	private String description;
	private String imagePath;
	
	public boolean equals(Object o)
	{
		Problem p = (Problem) o;
		//return this.latitude.equals(p.latitude) && this.longitude.equals(p.longitude) && this.reportDate.equals(p.reportDate) && this.description.equals(p.description);
		return this.imagePath.equals(p.imagePath);
	}
	
	public int hashCode()
	{
		return this.imagePath.hashCode();
	}
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Problem(String latitude, String longitude, String reportDate) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.reportDate = reportDate;
	}
	
	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
