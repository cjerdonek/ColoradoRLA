/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * @created Aug 9, 2017
 * @copyright 2017 Free & Fair
 * @license GNU General Public License 3.0
 * @creator Daniel M. Zimmerman <dmz@galois.com>
 * @description A system to assist in conducting statewide
 * risk-limiting audits.
 */

package us.freeandfair.corla.endpoint;

import static us.freeandfair.corla.asm.ASMEvent.DoSDashboardEvent.AUTHENTICATE_STATE_ADMINISTRATOR_EVENT;

import spark.Request;
import spark.Response;

import us.freeandfair.corla.Main;
import us.freeandfair.corla.asm.ASMEvent;
import us.freeandfair.corla.asm.DoSDashboardASM;
import us.freeandfair.corla.json.ServerASMResponse;
import us.freeandfair.corla.model.Administrator.AdministratorType;

/**
 * The endpoint for authenticating a state administrator.
 * 
 * @author Daniel M Zimmerman
 * @author Joe Kiniry <kiniry@freeandfair.us>
 * @version 0.0.1
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class AuthenticateStateAdministrator extends AbstractEndpoint {
  /**
   * @return no authorization is required for this endpoints.
   */
  @Override
  public AuthorizationType requiredAuthorization() {
    return AuthorizationType.NONE;
  }
  
  /**
   * @return this endpoint uses the Department of State ASM.
   */
  @Override
  protected Class<DoSDashboardASM> asmClass() {
    return DoSDashboardASM.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EndpointType endpointType() {
    return EndpointType.POST;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String endpointName() {
    return "/auth-state-admin";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ASMEvent endpointEvent() {
    return AUTHENTICATE_STATE_ADMINISTRATOR_EVENT;
  }
  
  /**
   * @param the_request the ignored request.
   * @return null because the DoS dashboard is a singleton.
   */
  @Override
  // this method is definitely not empty, but PMD thinks it is
  @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
  protected String asmIdentity(final Request the_request) {
    return null;
  }

  /**
   * Attempts to authenticate a state administrator; if the authentication is
   * successful, authentication data is added to the session.
   * 
   * Session query parameters: <tt>username</tt>, <tt>password</tt>, 
   * <tt>second_factor</tt>
   * 
   * @param the_request The request.
   * @param the_response The response.
   */
  @Override
  public String endpoint(final Request the_request, final Response the_response) {
    ok(the_response, "Authenticated");
    if (!Authentication.authenticateAs(the_request, AdministratorType.STATE)) {
      unauthorized(the_response, "Authentication failed");
    }

    // Take the transition triggered by this successful authentication.
    Main.dosDashboardASM().stepEvent(AUTHENTICATE_STATE_ADMINISTRATOR_EVENT);

    // Build the ASM server response.
    final ServerASMResponse asm_response =
        new ServerASMResponse(Main.dosDashboardASM().currentState(),
                              Main.dosDashboardASM().enabledUIEvents());
    the_response.body(Main.GSON.toJson(asm_response));

    return my_endpoint_result;
  }
}
