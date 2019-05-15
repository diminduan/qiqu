package com.example.duand.qiqu.JavaBean;

public class Match {

    private int MatchIcon;
    private String MatchName;
    private String MatchTime;
    private String MatchAddress;

    public Match(int MatchIcon, String MatchName, String MatchTime, String MatchAddress) {
        this.MatchIcon = MatchIcon;
        this.MatchName = MatchName;
        this.MatchTime = MatchTime;
        this.MatchAddress = MatchAddress;
    }

    public int getMatchIcon() {
        return MatchIcon;
    }

    public void setMatchIcon(int matchIcon) {
        MatchIcon = matchIcon;
    }

    public String getMatchName() {
        return MatchName;
    }

    public void setMatchName(String matchName) {
        MatchName = matchName;
    }

    public String getMatchTime() {
        return MatchTime;
    }

    public void setMatchTime(String matchTime) {
        MatchTime = matchTime;
    }

    public String getMatchAddress() {
        return MatchAddress;
    }

    public void setMatchAddress(String matchAddress) {
        MatchAddress = matchAddress;
    }

}
