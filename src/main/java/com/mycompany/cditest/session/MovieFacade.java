/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cditest.session;

import com.mycompany.cditest.entity.Movie;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Juneau
 */
@Stateless
public class MovieFacade extends AbstractFacade<Movie> {

    @PersistenceContext(unitName = "com.mycompany_CdiTest_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MovieFacade() {
        super(Movie.class);
    }
    
}
