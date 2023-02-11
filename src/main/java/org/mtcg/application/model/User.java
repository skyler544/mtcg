package org.mtcg.application.model;

public class User {
    private String token;
    private Credentials credentials;
    private UserProfile userProfile;

	public User(Credentials credentials) {
        this.credentials = credentials;
        this.setToken();
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getToken() {
        return token;
    }

    // If we were serious about this, the token would be a hash
    private void setToken() {
        this.token = this.credentials.getUsername() + "-mtcgToken";
    }

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
}
