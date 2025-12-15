package com.testing.inventorytracking;

public class AppConfig {

    // ✅ Choose environment globally
    // Change to "PROD" when you go live
//    private static final String ENVIRONMENT = "QAS";
    private static final String ENVIRONMENT = "PROD";

    // ✅ TBTD URLs
    private static final String TBTD_QAS_BASE = "http://10.12.6.188:8030/";
    private static final String TBTD_PROD_BASE = "http://10.60.100.100:8040/";

    // ✅ TEIE URLs
    private static final String TEIE_QAS_BASE = "http://10.12.40.51:8020/";
    private static final String TEIE_PROD_BASE = "http://10.60.130.8:8030/";

    // ✅ Default URLs (initialized to TBTD QAS)
    public static final String LOGIN_URL;
    public static final String ASSETS_URL;
    public static final String STAFF_URL;
    public static final String ROOMS_URL;
    public static final String SUMMARY_URL;
    public static final String ATS_URL;
    public static final String RAN_URL;

    static {
        String baseUrl;
        String ranUrl;

        if (ENVIRONMENT.equalsIgnoreCase("PROD")) {
            baseUrl = TBTD_PROD_BASE;
            ranUrl = TEIE_PROD_BASE + "ran/ran?sap-client=500";
        } else {
            baseUrl = TBTD_QAS_BASE;
            ranUrl = TEIE_QAS_BASE + "ran/ran?sap-client=500";
        }

        LOGIN_URL = baseUrl + "login/";
        ASSETS_URL = baseUrl + "assets/";
        STAFF_URL = baseUrl + "staff/";
        ROOMS_URL = baseUrl + "rooms/";
        SUMMARY_URL = baseUrl + "summary/";
        ATS_URL = baseUrl + "ats/";
        RAN_URL = ranUrl;
    }

    public static String getEnvironment() {
        return ENVIRONMENT;
    }

    // ✅ Generic helper to get correct base URL for any company
    public static String getBaseUrl(String company) {
        if (company.equalsIgnoreCase("tbtd")) {
            return ENVIRONMENT.equalsIgnoreCase("PROD") ? TBTD_PROD_BASE : TBTD_QAS_BASE;
        } else if (company.equalsIgnoreCase("teie")) {
            return ENVIRONMENT.equalsIgnoreCase("PROD") ? TEIE_PROD_BASE : TEIE_QAS_BASE;
        } else {
            return TBTD_QAS_BASE; // fallback
        }
    }

    // ✅ Dynamic endpoint builders for all modules
    public static String getLoginUrl(String company) {
        return getBaseUrl(company) + "login/";
    }

    public static String getAssetsUrl(String company) {
        if (company.equalsIgnoreCase("tbtd")) {
            return getBaseUrl(company) + "assets/?sap-client=200";
        } else if (company.equalsIgnoreCase("teie")) {
            return getBaseUrl(company) + "assets/?sap-client=500";
        } else {
            return getBaseUrl(company) + "assets/";
        }
    }

    public static String getStaffUrl(String company) {
        if (company.equalsIgnoreCase("tbtd")) {
            return getBaseUrl(company) + "staff/?sap-client=200";
        } else if (company.equalsIgnoreCase("teie")) {
            return getBaseUrl(company) + "staff/?sap-client=500";
        } else {
            return getBaseUrl(company) + "staff/";
        }
    }

    public static String getRoomsUrl(String company) {
        if (company.equalsIgnoreCase("tbtd")) {
            return getBaseUrl(company) + "rooms/?sap-client=200";
        } else if (company.equalsIgnoreCase("teie")) {
            return getBaseUrl(company) + "rooms/?sap-client=500";
        } else {
            return getBaseUrl(company) + "rooms/";
        }
    }

    public static String getSummaryUrl(String company) {
        if (company.equalsIgnoreCase("tbtd")) {
            return getBaseUrl(company) + "summary/summary?sap-client=200";
        } else if (company.equalsIgnoreCase("teie")) {
            return getBaseUrl(company) + "summary/summary?sap-client=500";
        } else {
            return getBaseUrl(company) + "summary/";
        }
    }

    public static String getAtsUrl(String company) {
        if (company.equalsIgnoreCase("tbtd")) {
            return getBaseUrl(company) + "ats/?sap-client=200";
        } else if (company.equalsIgnoreCase("teie")) {
            return getBaseUrl(company) + "ats/?sap-client=500";
        } else {
            return getBaseUrl(company) + "ats/";
        }
    }

    // ✅ FIXED: Each company now uses the correct SAP client
    public static String getRanUrl(String company) {
        if (company.equalsIgnoreCase("tbtd")) {
            return getBaseUrl(company) + "ran/ran?sap-client=200";
        } else if (company.equalsIgnoreCase("teie")) {
            return getBaseUrl(company) + "ran/ran?sap-client=500";
        } else {
            return RAN_URL;
        }
    }
}
