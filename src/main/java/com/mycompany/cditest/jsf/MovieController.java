package com.mycompany.cditest.jsf;

import com.mycompany.cditest.entity.Movie;
import com.mycompany.cditest.event.MovieEvent;
import com.mycompany.cditest.jsf.util.JsfUtil;
import com.mycompany.cditest.jsf.util.JsfUtil.PersistAction;
import com.mycompany.cditest.session.MovieFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("movieController")
@SessionScoped
public class MovieController implements Serializable {

    @EJB
    private com.mycompany.cditest.session.MovieFacade ejbFacade;
    private List<Movie> items = null;
    private Movie selected;
    
    @Inject
    Event<MovieEvent> movieEvents;

    public MovieController() {
    }

    public Movie getSelected() {
        return selected;
    }

    public void setSelected(Movie selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MovieFacade getFacade() {
        return ejbFacade;
    }

    public Movie prepareCreate() {
        selected = new Movie();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MovieCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MovieUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MovieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Movie> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                    movieEvents.fireAsync(new MovieEvent("New Movie Released", selected))
                            .whenComplete((event, throwable) -> {
                                if(throwable != null){
                                    System.out.println("Error has occurred: " + throwable.getMessage());
                                } else {
                                    System.out.println("Successful Movie Processing...");
                                }
                            });
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Movie getMovie(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Movie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Movie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Movie.class)
    public static class MovieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MovieController controller = (MovieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "movieController");
            return controller.getMovie(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Movie) {
                Movie o = (Movie) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Movie.class.getName()});
                return null;
            }
        }

    }

}
