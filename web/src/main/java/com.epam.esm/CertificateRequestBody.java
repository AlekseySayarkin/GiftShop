package com.epam.esm;

public class CertificateRequestBody {

    private String content;
    private String sort;
    private String tagName;

    public CertificateRequestBody() {
    }

    public CertificateRequestBody(String content, String sort, String tagName) {
        this.content = content;
        this.sort = sort;
        this.tagName = tagName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
