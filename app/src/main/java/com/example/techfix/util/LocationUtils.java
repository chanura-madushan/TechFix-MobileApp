package com.example.techfix.util;
public final class LocationUtils {
 private LocationUtils(){}
 public static double distanceKm(double lat1,double lon1,double lat2,double lon2){
  double r=6371.0; double dLat=Math.toRadians(lat2-lat1), dLon=Math.toRadians(lon2-lon1);
  double a=Math.sin(dLat/2)*Math.sin(dLat/2)+Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*Math.sin(dLon/2)*Math.sin(dLon/2);
  double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a)); return r*c;
 }
}