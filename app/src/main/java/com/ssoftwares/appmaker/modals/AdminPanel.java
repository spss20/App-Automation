package com.ssoftwares.appmaker.modals;

public class AdminPanel {
    private String id;
    private String subdomain;
    private Cpanel cpanel;
    private Product product;
    private String company_name;
    private Attachment company_logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cpanel getCpanel() {
        return cpanel;
    }

    public void setCpanel(Cpanel cpanel) {
        this.cpanel = cpanel;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Attachment getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(Attachment company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getBaseUrl() {
        return subdomain == null ?
                "https://" + cpanel.getDomain() + "/" :
                "https://" + subdomain + "." + cpanel.getDomain() + "/";
    }
}
