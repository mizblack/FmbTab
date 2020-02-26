package com.eye3.golfpay.fmb_tab.util;

import android.location.Location;

public class GPSUtil {
    //두지점(위도,경도) 사이의 거리
    public static double DistanceByDegree(double _latitude1, double _longitude1, double _latitude2, double _longitude2){
        double theta, dist;
        theta = _longitude1 - _longitude2;
        dist = Math.sin(DegreeToRadian(_latitude1)) * Math.sin(DegreeToRadian(_latitude2)) + Math.cos(DegreeToRadian(_latitude1))
                * Math.cos(DegreeToRadian(_latitude2)) * Math.cos(DegreeToRadian(theta));
        dist = Math.acos(dist);
        dist = RadianToDegree(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    //안드로이드 - 두지점(위도,경도) 사이의 거리
    public static double DistanceByDegreeAndroid(double _latitude1, double _longitude1, double _latitude2, double _longitude2){
        Location startPos = new Location("PointA");
        Location endPos = new Location("PointB");

        startPos.setLatitude(_latitude1);
        startPos.setLongitude(_longitude1);
        endPos.setLatitude(_latitude2);
        endPos.setLongitude(_longitude2);

        double distance = startPos.distanceTo(endPos);
                distance = (Math.round(distance *100) / 100.0);
        return distance;
    }

    //degree->radian 변환
    public static double DegreeToRadian(double degree){
        return degree * Math.PI / 180.0;
    }

    //randian -> degree 변환
    public static double RadianToDegree(double radian){
        return radian * 180d / Math.PI;
    }

}
