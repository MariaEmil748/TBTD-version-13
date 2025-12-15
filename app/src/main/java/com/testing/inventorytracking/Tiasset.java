package com.testing.inventorytracking;

import androidx.room.Entity;

import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Asset_Information",indices = @Index(value = {"ANLN1"},unique = true))
public class Tiasset {

    @PrimaryKey(autoGenerate = true)
    int key ;

    String ANLN1;
    String ENAME;
    String TXT50;
    String BUKRS;
    String BDATU;
    String ADATU;
    String NACHN;
    String NAME2;
    String NAME1;
    String PERNR;
    String ORGEH;
    String KTEXT;
    String PLANS;
    String KOSTL;
    String WERKS;
    String ORD41;
    String ORD42;
    String ORD43;
    String ORD44;
    String STORT;
    String TTEXT;
    String ANLKL;
    String ANLHTXT;
    String DEPARTMENT;
    String TITLE;
    String  STATUS;
    String NCHMC ;
    String RAUMN ;
    String ROOMNO ;
    String ROOMNAME ;
     String CASSETS ;


    public String getCASSETS() {
        return CASSETS;
    }

    public void setCASSETS(String CASSETS) {
        this.CASSETS = CASSETS;
    }


    public String getENAME() {
        return ENAME;
    }

    public void setENAME(String ENAME) {
        this.ENAME = ENAME;
    }

    public String getROOMNO() {
        return ROOMNO;
    }

    public void setROOMNO(String ROOMNO) {
        this.ROOMNO = ROOMNO;
    }

    public String getROOMNAME() {
        return ROOMNAME;
    }

    public void setROOMNAME(String ROOMNAME) {
        this.ROOMNAME = ROOMNAME;
    }

    public String getRAUMN() {
        return RAUMN;
    }

    public void setRAUMN(String RAUMN) {
        this.RAUMN = RAUMN;
    }

    public String getORGTX() {
        return ORGTX;
    }

    public void setORGTX(String ORGTX) {
        this.ORGTX = ORGTX;
    }

    String ORGTX ;

    public String getNCHMC() {
        return NCHMC;
    }

    public void setNCHMC(String NCHMC) {
        this.NCHMC = NCHMC;
    }

    public String getVNAMC() {
        return VNAMC;
    }

    public void setVNAMC(String VNAMC) {
        this.VNAMC = VNAMC;
    }

    String VNAMC ;
    int count ;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }


    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }

    public String getKTEXT() {
        return KTEXT;
    }

    public void setKTEXT(String KTEXT) {
        this.KTEXT = KTEXT;
    }

    public String getNAME1() {
        return NAME1;
    }

    public void setNAME1(String NAME1) {
        this.NAME1 = NAME1;
    }

//    public Tiasset(String ANLN1, String PERNR, String ORD42) {
//        this.ANLN1 = ANLN1;
//        this.PERNR = PERNR;
//        this.ORD42 = ORD42;
//    }

    //    public Tiasset(String TXT50, String ANLN1, String BUKRS, String BDATU, String ADATU, String NACHN, String NAME2, String NAME1, String PERNR, String ORGEH, String VNAMC, String KTEXT, String PLANS, String KOSTL, String WERKS, String ORD41, String ORD42, String ORD43, String ORD44, String STORT, String TTEXT, String ANLKL, String ANLHTXT) {
//        this.TXT50 = TXT50;
//        this.ANLN1 = ANLN1;
//        this.BUKRS = BUKRS;
//        this.BDATU = BDATU;
//        this.ADATU = ADATU;
//        this.NACHN = NACHN;
//        this.NAME2 = NAME2;
//        this.NAME1 = NAME1;
//        this.PERNR = PERNR;
//        this.ORGEH = ORGEH;
//        this.VNAMC = VNAMC;
//        this.KTEXT = KTEXT;
//        this.PLANS = PLANS;
//        this.KOSTL = KOSTL;
//        this.WERKS = WERKS;
//        this.ORD41 = ORD41;
//        this.ORD42 = ORD42;
//        this.ORD43 = ORD43;
//        this.ORD44 = ORD44;
//        this.STORT = STORT;
//        this.TTEXT = TTEXT;
//        this.ANLKL = ANLKL;
//        this.ANLHTXT = ANLHTXT;
//    }

    public String getANLHTXT() {
        return ANLHTXT;
    }

    public void setANLHTXT(String ANLHTXT) {
        this.ANLHTXT = ANLHTXT;
    }

    public Tiasset() {

    }

    public String getORD43() {
        return ORD43;
    }

    public void setORD43(String ORD43) {
        this.ORD43 = ORD43;
    }

    public String getORD44() {
        return ORD44;
    }

    public void setORD44(String ORD44) {
        this.ORD44 = ORD44;
    }

    public String getPLANS() {
        return PLANS;
    }

    public void setPLANS(String PLANS) {
        this.PLANS = PLANS;
    }

    public String getKOSTL() {
        return KOSTL;
    }

    public void setKOSTL(String KOSTL) {
        this.KOSTL = KOSTL;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getORD41() {
        return ORD41;
    }

    public void setORD41(String ORD41) {
        this.ORD41 = ORD41;
    }

    public String getORD42() {
        return ORD42;
    }

    public void setORD42(String ORD42) {
        this.ORD42 = ORD42;
    }

    public String getSTORT() {
        return STORT;
    }

    public void setSTORT(String STORT) {
        this.STORT = STORT;
    }

    public String getTTEXT() {
        return TTEXT;
    }

    public void setTTEXT(String TTEXT) {
        this.TTEXT = TTEXT;
    }

    public String getANLKL() {
        return ANLKL;
    }

    public void setANLKL(String ANLKL) {
        this.ANLKL = ANLKL;
    }


    public String getNAME2() {
        return NAME2;
    }

    public void setNAME2(String NAME2) {
        this.NAME2 = NAME2;
    }


    public String getORGEH() {
        return ORGEH;
    }

    public void setORGEH(String ORGEH) {
        this.ORGEH = ORGEH;
    }

    public String getTXT50() {
        return TXT50;
    }

    public void setTXT50(String TXT50) {
        this.TXT50 = TXT50;
    }

    public String getANLN1() {
        return ANLN1;
    }

    public void setANLN1(String ANLN1) {
        this.ANLN1 = ANLN1;
    }

    public String getBUKRS() {
        return BUKRS;
    }

    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }

    public String getBDATU() {
        return BDATU;
    }

    public void setBDATU(String BDATU) {
        this.BDATU = BDATU;
    }

    public String getADATU() {
        return ADATU;
    }

    public void setADATU(String ADATU) {
        this.ADATU = ADATU;
    }

    public String getNACHN() {
        return NACHN;
    }

    public void setNACHN(String NACHN) {
        this.NACHN = NACHN;
    }


    public String getPERNR() {
        return PERNR;
    }

    public void setPERNR(String PERNR) {
        this.PERNR = PERNR;
    }
}
