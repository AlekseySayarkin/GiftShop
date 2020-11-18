package com.epam.esm.service;

public class CertificateRequestBody {

    private String content;
    private String sortType;
    private String sortBy;
    private String tagName;

    public CertificateRequestBody() {
    }

    public CertificateRequestBody(String content, String sortType, String sortBy, String tagName) {
        this.content = content;
        this.sortType = sortType;
        this.sortBy = sortBy;
        this.tagName = tagName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
