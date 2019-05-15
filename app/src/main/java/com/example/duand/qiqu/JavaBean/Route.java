package com.example.duand.qiqu.JavaBean;

import java.util.Date;

public class Route {

    private int RouteIcon;
    private String RouteName;
    private String RouteDetail;
    private Integer boutiqueRouteId;
    private String boutiqueRouteCity;
    private String boutiqueRouteType;
    private double boutiqueRouteDistance;

    private Integer collect_Count;
    private Integer thumbUpCount;
    private Integer avatar;
    private Date createTime;
    private User user;

    public Route(int RouteIcon, String RouteName, String RouteDetail, Integer boutiqueRouteId,String routeCity,String routeType,
                 double routeDistance){
        this.RouteIcon = RouteIcon;
        this.RouteName = RouteName;
        this.RouteDetail = RouteDetail;
        this.boutiqueRouteId = boutiqueRouteId;
        this.boutiqueRouteCity = routeCity;
        this.boutiqueRouteType = routeType;
        this.boutiqueRouteDistance = routeDistance;
    }

    public Integer getBoutiqueRouteId() {
        return boutiqueRouteId;
    }

    public void setBoutiqueRouteId(Integer boutiqueRouteId) {
        this.boutiqueRouteId = boutiqueRouteId;
    }

    public String getBoutiqueRouteCity() {
        return boutiqueRouteCity;
    }

    public void setBoutiqueRouteCity(String boutiqueRouteCity) {
        this.boutiqueRouteCity = boutiqueRouteCity;
    }

    public String getBoutiqueRouteType() {
        return boutiqueRouteType;
    }

    public void setBoutiqueRouteType(String boutiqueRouteType) {
        this.boutiqueRouteType = boutiqueRouteType;
    }

    public double getBoutiqueRouteDistance() {
        return boutiqueRouteDistance;
    }

    public void setBoutiqueRouteDistance(double boutiqueRouteDistance) {
        this.boutiqueRouteDistance = boutiqueRouteDistance;
    }

    public int getRouteIcon() {
        return RouteIcon;
    }

    public void setRouteIcon(int routeIcon) {
        RouteIcon = routeIcon;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public String getRouteDetail() {
        return RouteDetail;
    }

    public void setRouteDetail(String routeDetail) {
        RouteDetail = routeDetail;
    }

}
