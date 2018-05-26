package com.screamatthewind;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

import com.screamatthewind.HelloWorldConfiguration.ClientSecretsConfiguration;
import com.screamatthewind.auth.AuthFilter;
import com.screamatthewind.core.Contact;
import com.screamatthewind.core.Location;
import com.screamatthewind.core.User;
import com.screamatthewind.db.ContactDAO;
import com.screamatthewind.db.LocationDAO;
import com.screamatthewind.db.UserDAO;
import com.screamatthewind.resources.AuthResource;
import com.screamatthewind.resources.ClientResource;
import com.screamatthewind.resources.ContactResource;
import com.screamatthewind.resources.UserResource;
import com.screamatthewind.resources.LocationResource;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
  public static void main(final String[] args) throws Exception {
    new HelloWorldApplication().run(args);
  }

  private final HibernateBundle<HelloWorldConfiguration> hibernateBundle =
      new HibernateBundle<HelloWorldConfiguration>(User.class, Location.class, Contact.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(final HelloWorldConfiguration configuration) {
          return configuration.getDataSourceFactory();
        }
      };

  @Override
  public String getName() {
    return "hello-world";
  }

  @Override
  public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
    bootstrap.addBundle(new MigrationsBundle<HelloWorldConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(final HelloWorldConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    });

    bootstrap.addBundle(hibernateBundle);

    bootstrap.addBundle(new AssetsBundle("/assets/app.js", "/app.js", null, "app"));
    bootstrap.addBundle(new AssetsBundle("/assets/stylesheets", "/stylesheets", null, "css"));
    bootstrap.addBundle(new AssetsBundle("/assets/directives", "/directives", null, "directives"));
    bootstrap
        .addBundle(new AssetsBundle("/assets/controllers", "/controllers", null, "controllers"));
    bootstrap.addBundle(new AssetsBundle("/assets/services", "/services", null, "services"));
    bootstrap.addBundle(new AssetsBundle("/assets/vendor", "/vendor", null, "vendor"));
    bootstrap.addBundle(new AssetsBundle("/assets/partials", "/partials", null, "partials"));
  }

  @Override
  public void run(final HelloWorldConfiguration configuration, final Environment environment)
      throws ClassNotFoundException {

    final UserDAO daoUser = new UserDAO(hibernateBundle.getSessionFactory());
    final LocationDAO daoLocation = new LocationDAO(hibernateBundle.getSessionFactory());
    final ContactDAO daoContact = new ContactDAO(hibernateBundle.getSessionFactory());

    final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClient()).build(getName());
    final ClientSecretsConfiguration clientSecrets = configuration.getClientSecrets();

    environment.jersey().register(new ClientResource());
    environment.jersey().register(new UserResource(daoUser));
    environment.jersey().register(new AuthResource(client, daoUser, clientSecrets));
    environment.jersey().register(new LocationResource(daoLocation));
    environment.jersey().register(new ContactResource(daoContact));
    
    environment.servlets().addFilter("AuthFilter", new AuthFilter())
        .addMappingForUrlPatterns(null, true, "/api/*");
  }
}
