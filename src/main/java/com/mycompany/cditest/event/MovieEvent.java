/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cditest.event;

import com.mycompany.cditest.entity.Movie;

/**
 *
 * @author Juneau
 */
public class MovieEvent {
    
    private String message;
    private Movie movie;
    
    public MovieEvent(String message, Movie movie){
        this.message = message;
        this.movie = movie;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public Movie getMovie(){
        return this.movie;
    }
    
    public void setMovie(Movie movie){
        this.movie = movie;
    }
    
}
