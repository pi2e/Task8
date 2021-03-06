package com.phoodie.twitter;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;

public class TwitterAPI {
	
	public static String searchapi = "https://api.twitter.com/1.1/search/tweets.json?q=%23PHOODIE";
	public static String updateapi = "https://api.twitter.com/1.1/statuses/update.json?status=";
	public static String retweetapi = "https://api.twitter.com/1.1/statuses/retweet/";
	private Token accessToken;
	private OAuthService service;

	public TwitterAPI(HttpServletRequest request) {
		service = (OAuthService) request.getSession().getAttribute("service");
		accessToken = (Token) request.getSession().getAttribute("accessToken");
	}
	
	public boolean islogin() {
		if (accessToken == null || service == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void reply(String statue, String statueid) {
		OAuthRequest trequest = new OAuthRequest(Verb.POST, updateapi+URLEncoder.encode(statue)+"&in_reply_to_status_id=" + statueid);
		service.signRequest(accessToken, trequest); 
		trequest.send();
	}
	
	public void reply(String statue, String photoid, String statueid) {
		OAuthRequest trequest = new OAuthRequest(Verb.POST, updateapi+URLEncoder.encode(statue + " #PHOODIE" + photoid) +"&in_reply_to_status_id=" + statueid);
		service.signRequest(accessToken, trequest); 
		trequest.send();
	}
	
	public void retweet(String id) {
		OAuthRequest trequest = new OAuthRequest(Verb.POST, retweetapi+ id +".json");
		service.signRequest(accessToken, trequest); 
		trequest.send();
	}
	
	public void update(String statue) {
		OAuthRequest trequest = new OAuthRequest(Verb.POST, updateapi+URLEncoder.encode(statue));
		service.signRequest(accessToken, trequest); 
		trequest.send();
	}
	
	public void update(String statue, String photoid) {
		OAuthRequest trequest = new OAuthRequest(Verb.POST, updateapi+URLEncoder.encode(statue + " #PHOODIE" + photoid));
		service.signRequest(accessToken, trequest); 
		trequest.send();
	}
	
	public List<Statuse> search(String id) {
		OAuthRequest trequest = new OAuthRequest(Verb.GET, searchapi + id);
		service.signRequest(accessToken, trequest);
		Response response = trequest.send();
		
		Gson g = new Gson();
		TwitterSearchResult sr = g.fromJson(response.getBody(), TwitterSearchResult.class);
	    return sr.getStatuses();
	}

}
