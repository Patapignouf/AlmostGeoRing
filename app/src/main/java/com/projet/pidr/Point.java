package com.projet.pidr;

public class Point {
	private String Title = "";
	private int id = 0;
	private double lat = 0;
	private double longi = 0;
	private double altitude = 0;
	private String project = "";
	private String type_point = "";



	private Boolean est_faille = false;
	private double azimuth = 0;
	private double pendage = 0;
	
	public Point() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLongi() {
		return longi;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return this.Title; 
	}

	public void setTitle(String title) {
		Title = title;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLongi(double longi) {
		this.longi = longi;
	}

	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}

	public void setPendage(double pendage) {
		this.pendage = pendage;
	}

	public double getLong() {
		return longi;
	}

	public double getLat() {
		return lat;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public double getPendage() {
		return pendage;
	}
	
	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Boolean getEst_faille() {
		return est_faille;
	}

	public void setEst_faille(Boolean est_faille) {
		this.est_faille = est_faille;
	}

	public String getType_point() {
		return type_point;
	}

	public void setType_point(String type_point) {
		this.type_point = type_point;
	}
}
