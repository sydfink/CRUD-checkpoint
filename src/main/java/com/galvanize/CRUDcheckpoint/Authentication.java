package com.galvanize.CRUDcheckpoint;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Authentication {

    private boolean authenticated;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;

    public Authentication(boolean authenticated, User user) {
        this.authenticated = authenticated;
        this.user = user;
    }

    public Authentication() {
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
