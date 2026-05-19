package top.jpower.core.asterisk.ami.service.Dto;

import lombok.Data;
import org.asteriskjava.manager.event.*;

@Data
public class PJSIPDetailDTO {

    private EndpointDetail endpoint;
    private AuthDetail auth;
    private AorDetail aor;
    private TransportDetail transport;
    private ContactStatusDetail contactStatus;

}
