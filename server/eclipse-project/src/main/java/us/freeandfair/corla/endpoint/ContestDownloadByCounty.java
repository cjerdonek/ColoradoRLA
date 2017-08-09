/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * @created Jul 27, 2017
 * @copyright 2017 Free & Fair
 * @license GNU General Public License 3.0
 * @author Daniel M. Zimmerman <dmz@freeandfair.us>
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.endpoint;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.eclipse.jetty.http.HttpStatus;

import spark.Request;
import spark.Response;

import us.freeandfair.corla.Main;
import us.freeandfair.corla.hibernate.Persistence;
import us.freeandfair.corla.model.Contest;
import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.CountyQueries;
import us.freeandfair.corla.util.SparkHelper;

/**
 * The contest by county download endpoint.
 * 
 * @author Daniel M. Zimmerman
 * @version 0.0.1
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class ContestDownloadByCounty implements Endpoint {
  /**
   * {@inheritDoc}
   */
  @Override
  public EndpointType endpointType() {
    return EndpointType.GET;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String endpointName() {
    return "/contest/county";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String endpoint(final Request the_request, final Response the_response) {
    String result = "";
    int status = HttpStatus.OK_200;
    
    if (validateParameters(the_request)) {
      final Set<Integer> county_set = new HashSet<Integer>();
      for (final String s : the_request.queryParams()) {
        county_set.add(Integer.valueOf(s));
      }
      final Set<Contest> contest_set = getMatchingContests(county_set);
      if (contest_set == null) {
        status = HttpStatus.INTERNAL_SERVER_ERROR_500;
        result = "Error retrieving records from database";
      } else {
        try {
          final OutputStream os = SparkHelper.getRaw(the_response).getOutputStream();
          final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

          Main.GSON.toJson(contest_set, bw);
          bw.flush();
        } catch (final IOException e) {
          status = HttpStatus.INTERNAL_SERVER_ERROR_500;
          result = "Unable to stream response";
        }
      }
    } else {
      status = HttpStatus.BAD_REQUEST_400;
      result = "Invalid county ID specified";
    }
    the_response.status(status);
    return result;
  }
  
  /**
   * Validates the parameters of a request. For this endpoint, 
   * the paramter names must all be integers.
   * 
   * @param the_request The request.
   * @return true if the parameters are valid, false otherwise.
   */
  private boolean validateParameters(final Request the_request) {
    boolean result = true;
    
    for (final String s : the_request.queryParams()) {
      try {
        Integer.parseInt(s);
      } catch (final NumberFormatException e) {
        result = false;
        break;
      }
    }
    
    return result;
  }
  
  /**
   * Gets contests that are in the specified set of counties.
   * 
   * @param the_county_ids The counties.
   * @return the matching contests, or null if the query fails.
   */
  private Set<Contest> getMatchingContests(final Set<Integer> the_county_ids) {
    Set<Contest> result = null;
    
    try {
      Persistence.beginTransaction();
      final Set<Contest> query_results = new HashSet<Contest>();
      for (final Integer county_id : the_county_ids) {
        final County c = CountyQueries.byID(county_id);
        if (c != null) {
          query_results.addAll(c.contests());
        }
      }
      result = query_results;
      try {
        Persistence.commitTransaction();
      } catch (final RollbackException e) {
        Persistence.rollbackTransaction();
      }
    } catch (final PersistenceException e) {
      Main.LOGGER.error("Exception when reading contests from database: " + e);
    }

    return result;
  }
}