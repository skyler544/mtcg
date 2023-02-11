package org.mtcg.application.model;

public class User {
    private String token;
    private Credentials credentials;
    private String name;
	private String bio;
	private String image;

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

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
